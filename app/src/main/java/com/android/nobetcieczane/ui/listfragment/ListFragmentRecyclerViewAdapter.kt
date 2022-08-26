package com.android.nobetcieczane.ui.listfragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.nobetcieczane.R
import com.android.nobetcieczane.data.model.DataDto
import com.android.nobetcieczane.databinding.PharmacyOnDutyRecyclerRowBinding

class ListFragmentRecyclerViewAdapter(
    var pharmacyList: ArrayList<DataDto>
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
    }

    override fun getItemCount(): Int {
        return pharmacyList.size
    }
}
