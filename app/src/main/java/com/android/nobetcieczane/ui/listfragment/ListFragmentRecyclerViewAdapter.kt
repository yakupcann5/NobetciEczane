package com.yakupcan.nobetcieczane.ui.listfragment

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.yakupcan.nobetcieczane.R
import com.yakupcan.nobetcieczane.data.model.DataDto
import com.yakupcan.nobetcieczane.databinding.PharmacyOnDutyRecyclerRowBinding
import com.yakupcan.nobetcieczane.util.Tools
import com.yakupcan.nobetcieczane.util.ViewAnimation
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds

class ListFragmentRecyclerViewAdapter(
    private var pharmacyList: ArrayList<DataDto>, val context: Context
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class ViewHolder(view: PharmacyOnDutyRecyclerRowBinding) :
        RecyclerView.ViewHolder(view.root) {
        var sentBinding: PharmacyOnDutyRecyclerRowBinding = view
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<PharmacyOnDutyRecyclerRowBinding>(
            inflater,
            R.layout.pharmacy_on_duty_recycler_row,
            parent,
            false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).sentBinding.pharmacy = pharmacyList[position]
        holder.sentBinding.pharmacyName.text = pharmacyList[position].eczaneAdi
        holder.sentBinding.pharmacyAddress.text = pharmacyList[position].adresi
        holder.sentBinding.pharmacyPhoneNumber.text = pharmacyList[position].telefon
        holder.sentBinding.listFragButtonsLinear.visibility = View.GONE
        MobileAds.initialize(context)
        val adRequest = AdRequest.Builder().build()
        holder.sentBinding.adMob.loadAd(adRequest)
        holder.sentBinding.btToggleText.setOnClickListener {
            toggleSectionText(it, holder.sentBinding)
            holder.sentBinding.listFragButtonsLinear.visibility = View.VISIBLE
        }
        holder.sentBinding.pharmacyCall.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL);
            intent.data = Uri.parse("tel:${holder.sentBinding.pharmacyPhoneNumber.text}")
            startActivity(context, intent, null)
        }
        holder.sentBinding.pharmacyDirections.setOnClickListener {
            val gmmIntentUri =
                Uri.parse("google.navigation:q=${pharmacyList[position].latitude}, ${pharmacyList[position].longitude}")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(context, mapIntent, null)
        }
    }

    private fun toggleSectionText(view: View, binding: PharmacyOnDutyRecyclerRowBinding) {
        val show: Boolean = toggleArrow(view)
        if (show) {
            ViewAnimation.expand(
                binding.listFragButtonsLinear,
                object : ViewAnimation.AnimListener {
                    override fun onFinish() {
                        Tools.nestedScrollTo(
                            binding.nestedScrollView,
                            binding.listFragButtonsLinear
                        )
                    }
                })
        } else {
            ViewAnimation.collapse(binding.listFragButtonsLinear)
        }
    }

    private fun toggleArrow(view: View): Boolean {
        return if (view.rotation == 0f) {
            view.animate().setDuration(200).rotation(180f)
            true
        } else {
            view.animate().setDuration(200).rotation(0f)
            false
        }
    }

    override fun getItemCount(): Int {
        return pharmacyList.size
    }
}
