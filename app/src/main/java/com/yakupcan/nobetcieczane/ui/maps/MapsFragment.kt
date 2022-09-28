package com.yakupcan.nobetcieczane.ui.maps

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.play.core.review.ReviewManagerFactory
import com.yakupcan.nobetcieczane.R
import com.yakupcan.nobetcieczane.databinding.FragmentMapWithBottomsheetBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MapsFragment : Fragment(), View.OnClickListener {
    private lateinit var binding : FragmentMapWithBottomsheetBinding
    private val viewModel : MapsFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater : LayoutInflater, container : ViewGroup?,
        savedInstanceState : Bundle?
    ) : View {
        binding = FragmentMapWithBottomsheetBinding.inflate(layoutInflater)
        MobileAds.initialize(this.requireContext()) {}
        return binding.root
    }

    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        initViews()
        mapReady()
        setBottomSettingSheet(false)
        viewModel.getPharmacy()
        MobileAds.initialize(this.requireContext())
        val adRequest = AdRequest.Builder().build()
        binding.bottomsheet.adMob.loadAd(adRequest)
        inAppReviews()
    }

    override fun onClick(v : View?) {
        when (v?.id) {
            R.id.filter_fragment_open_bttn -> {
                findNavController().navigate(R.id.action_mapsFragment2_to_listFragment2)
            }

            R.id.settings_button -> {
                setBottomSettingSheet(true)
            }

            R.id.setting_sheet_settings_text -> {
                findNavController().navigate(R.id.action_mapsFragment2_to_settingsFragment)
            }

            R.id.setting_sheet_vote_text -> {
                Toast.makeText(requireContext(), "oylama", Toast.LENGTH_SHORT).show()
            }

            R.id.setting_sheet_share_text -> {
                Toast.makeText(requireContext(), "paylaş", Toast.LENGTH_SHORT).show()
            }

            R.id.pharmacyCall -> {
                val intent = Intent(Intent.ACTION_DIAL);
                intent.data = Uri.parse("tel:${viewModel.getPhoneNum()}")
                startActivity(intent)
            }

            R.id.pharmacyDirections -> {
                val gmmIntentUri =
                    Uri.parse("google.navigation:q=${viewModel.getMarkerLat()}, ${viewModel.getMarkerLng()}")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                startActivity(mapIntent)
            }
        }
    }

    private fun initViews() {
        binding.mainLayout.filterFragmentOpenBttn.setOnClickListener(this)
        binding.mainLayout.settingsButton.setOnClickListener(this)
        binding.settingsSheet.settingSheetSettingsText.setOnClickListener(this)
        binding.settingsSheet.settingSheetVoteText.setOnClickListener(this)
        binding.settingsSheet.settingSheetShareText.setOnClickListener(this)
        binding.mainLayout.nowLocation.setOnClickListener(this)
        binding.bottomsheet.pharmacyCall.setOnClickListener(this)
        binding.bottomsheet.pharmacyDirections.setOnClickListener(this)
    }

    private fun mapReady() {
        val bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomsheet.root)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        bottomSheetBehavior.peekHeight = binding.bottomsheet.constraint.height
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync { map ->
            map.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    this.requireContext(),
                    R.raw.style_json
                )
            )
            val pharmacyList = arrayListOf<LatLng>()
            val nowLocation = arrayListOf<LatLng>()
            viewModel.markerList.observe(viewLifecycleOwner, Observer { list ->
                list.forEach {
                    pharmacyList.add(LatLng(it.latitude!!, it.longitude!!))
                    map.addMarker(
                        MarkerOptions().position(LatLng(it.latitude, it.longitude))
                            .title(it.eczaneAdi).icon(
                                viewModel.bitmapDescriptor(
                                    requireContext(),
                                    R.drawable.ic_marker
                                )
                            )
                    )
                    nowLocation.add(
                        LatLng(
                            viewModel.getLat().toDouble(),
                            viewModel.getLng().toDouble()
                        )
                    )
                    map.addMarker(
                        MarkerOptions().position(
                            LatLng(
                                viewModel.getLat().toDouble(),
                                viewModel.getLng().toDouble()
                            )
                        ).icon(viewModel.bitmapDescriptor(requireContext(), R.drawable.ic_nowhere))
                            .title(getString(R.string.now_here)).flat(true)
                    )
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(nowLocation[0], 13f))
                    map.mapType = GoogleMap.MAP_TYPE_NORMAL
                }
                if (pharmacyList.size == 0) {
                    val alertDialog =
                        AlertDialog.Builder(this.requireContext()).setTitle(R.string.error)
                            .setMessage(R.string.not_found_pharmacy).setCancelable(true)
                            .setPositiveButton(R.string.cancel,
                                DialogInterface.OnClickListener { dialogInterface, i ->
                                    dialogInterface.cancel()
                                }).setNegativeButton(
                                R.string.try_again,
                                DialogInterface.OnClickListener { dialogInterface, i ->
                                    findNavController().navigate(R.id.action_mapsFragment2_to_splashFragment)
                                })
                    alertDialog.show()
                }
            })
            binding.mainLayout.nowLocation.setOnClickListener {
                if (nowLocation.size == 0) {
                    val alertDialog =
                        AlertDialog.Builder(this.requireContext()).setTitle(R.string.error)
                            .setMessage(R.string.now_location_failed).setCancelable(true)
                            .setPositiveButton(R.string.cancel,
                                DialogInterface.OnClickListener { dialogInterface, i ->
                                    dialogInterface.cancel()
                                })
                    alertDialog.show()
                } else {
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(nowLocation[0], 15f))
                }
            }
            map.setOnMarkerClickListener { marker ->
                viewModel.infoList.observe(viewLifecycleOwner, Observer { list ->
                    list.forEach {
                        if (marker.title == it.eczaneAdi) {
                            binding.bottomsheet.constraint.visibility = View.VISIBLE
                            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                            binding.bottomsheet.pharmacyName.text = it.eczaneAdi
                            binding.bottomsheet.pharmacyAddress.text = it.adres
                            binding.bottomsheet.pharmacyPhone.text = it.telefon
                            viewModel.setPhoneNum(it.telefon!!)
                            viewModel.saveMarkerLocation(
                                marker.position.latitude,
                                marker.position.longitude
                            )

                            bottomSheetBehavior.setBottomSheetCallback(object :
                                BottomSheetBehavior.BottomSheetCallback() {
                                override fun onStateChanged(bottomSheet : View, newState : Int) {
                                    when (newState) {
                                        BottomSheetBehavior.STATE_DRAGGING, BottomSheetBehavior.STATE_SETTLING -> {}

                                        BottomSheetBehavior.STATE_HIDDEN -> {
                                            bottomSheetBehavior.peekHeight = 0
                                            binding.bottomsheet.constraint.visibility = View.GONE
                                        }

                                        else -> {}
                                    }
                                }

                                override fun onSlide(bottomSheet : View, slideOffset : Float) {}
                            })
                        }

                    }
                })
                setBottomSettingSheet(false)
                return@setOnMarkerClickListener false
            }
            map.setOnMapClickListener {
                setBottomSettingSheet(false)
                if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_HIDDEN)
                    binding.bottomsheet.constraint.visibility = View.GONE
            }
        }
    }

    private fun setBottomSettingSheet(isVisible : Boolean) {
        val bottomSheetSetting = BottomSheetBehavior.from(binding.settingsSheet.root)
        bottomSheetSetting.state = BottomSheetBehavior.STATE_HIDDEN
        bottomSheetSetting.peekHeight = binding.settingsSheet.constraint.height
        if (isVisible) {
            binding.settingsSheet.constraint.visibility = View.VISIBLE
            bottomSheetSetting.state = BottomSheetBehavior.STATE_EXPANDED
            bottomSheetSetting.setBottomSheetCallback(object :
                BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet : View, newState : Int) {
                    when (newState) {
                        BottomSheetBehavior.STATE_DRAGGING, BottomSheetBehavior.STATE_SETTLING -> {}

                        BottomSheetBehavior.STATE_HIDDEN -> {}

                        else -> {}
                    }
                }

                override fun onSlide(bottomSheet : View, slideOffset : Float) {}
            })
        } else {
            binding.settingsSheet.constraint.visibility = View.GONE
        }
    }

    private fun inAppReviews() {
        val manager = ReviewManagerFactory.create(this.requireContext())
        val request = manager.requestReviewFlow()
        request.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // We got the ReviewInfo object
                val reviewInfo = task.result
                val flow = manager.launchReviewFlow(this.requireActivity(), reviewInfo)
                flow.addOnCompleteListener {

                }
            } else {
                // There was some problem, log or handle the error code.
            }
        }
    }
}