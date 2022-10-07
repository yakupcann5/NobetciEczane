package com.yakupcan.nobetcieczane.ui.listfragment

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.yakupcan.nobetcieczane.R
import com.yakupcan.nobetcieczane.common.RequestState
import com.yakupcan.nobetcieczane.data.model.DataDto
import com.yakupcan.nobetcieczane.databinding.FragmentListBinding
import com.yakupcan.nobetcieczane.databinding.TabBarLayoutBinding
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ListFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentListBinding
    private lateinit var toolbarBinding: TabBarLayoutBinding
    private val viewModel: ListFragmentViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewAdapter: ListFragmentRecyclerViewAdapter
    private var pharmacyArray = arrayListOf<DataDto>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(inflater)
        getDataFromApi()
        toolbarBinding = binding.listFragmentTabBar
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        recyclerView = view.findViewById(R.id.pharmacy_recyclerView)
        recyclerViewAdapter = ListFragmentRecyclerViewAdapter(pharmacyArray, requireContext())
        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = recyclerViewAdapter
        (recyclerView.itemAnimator as SimpleItemAnimator?)!!.supportsChangeAnimations = false
        recyclerView.setHasFixedSize(true);
        MobileAds.initialize(this.requireContext())
        val adRequest = AdRequest.Builder().build()
        binding.listFragAdMob.loadAd(adRequest)
    }

    private fun getDataFromApi() {
        viewModel.getPharmacy()
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.pharmacyState.collect { requestState ->
                when (requestState) {
                    is RequestState.Success -> {
                        requestState.data?.let {
                            if (it.size == 0) {
                                binding.notFoundPharmacy.visibility = View.VISIBLE
                            } else {
                                initRecycler(it)
                            }
                        }
                    }
                    else -> {}
                }
            }
        }
    }

    private fun initRecycler(list: ArrayList<DataDto>) {
        binding.pharmacyRecyclerView.apply {
            adapter = ListFragmentRecyclerViewAdapter(list, requireContext())
        }
    }

    private fun initViews() {
        binding.listFragmentTabBar.listFragTabBarLocationButton.setOnClickListener(this)
        binding.listFragmentTabBar.listFragmentSettingButton.setOnClickListener(this)
        binding.filterFragmentOpenBttn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.list_frag_tab_bar_location_button -> {
                findNavController().navigate(R.id.action_listFragment2_to_mapsFragment2)
            }
            R.id.list_fragment_setting_button -> {
                findNavController().navigate(R.id.action_listFragment2_to_settingsFragment)
                /*val alertDialog =
                    AlertDialog.Builder(this.requireContext()).setTitle(R.string.settings)
                        .setMessage(R.string.new_update).setCancelable(true)
                        .setPositiveButton(R.string.cancel,
                            DialogInterface.OnClickListener { dialogInterface, i ->
                                dialogInterface.cancel()
                            })
                alertDialog.show()*/
            }
            R.id.filter_fragment_open_bttn -> {
                findNavController().navigate(R.id.action_listFragment2_to_filterFragment)
            }
        }
    }
}