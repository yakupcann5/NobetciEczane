package com.yakupcan.nobetcieczane.ui.splash

import android.Manifest
import android.annotation.SuppressLint
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.tasks.Task
import com.yakupcan.nobetcieczane.R
import com.yakupcan.nobetcieczane.databinding.FragmentSplashBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class SplashFragment : Fragment(), LocationListener {
    private lateinit var binding : FragmentSplashBinding
    private val splashViewModel : SplashViewModel by viewModels()
    private lateinit var flpc : FusedLocationProviderClient
    private lateinit var locationTask : Task<Location>

    override fun onCreateView(
        inflater : LayoutInflater, container : ViewGroup?,
        savedInstanceState : Bundle?
    ) : View {
        binding = FragmentSplashBinding.inflate(inflater)
        flpc = LocationServices.getFusedLocationProviderClient(this.requireContext())
        return binding.root
    }

    @SuppressLint("HardwareIds")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        //onLocationChanged(splashViewModel.getLat(),splashViewModel.getLng())
        val androidID = Settings.Secure.getString(
            context?.contentResolver,
            Settings.Secure.ANDROID_ID
        )
        if (androidID.equals("a144708fc62a59c4")) {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Push Screen")
            builder.setMessage(
                "Do you want to open the push screen?"
            )
            builder.setPositiveButton("Yes") { _, _ ->
                findNavController().navigate(R.id.action_splashFragment_to_pushFragment)
            }

            builder.setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }

            builder.show()
        } else {
            checkPermissions()
            savePushToken()
        }
    }

    @SuppressLint("HardwareIds")
    private fun savePushToken() {
        val androidID = Settings.Secure.getString(
            context?.contentResolver,
            Settings.Secure.ANDROID_ID
        )
        splashViewModel.savePushToken(androidID)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun checkPermissions() {
        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    // Precise location access granted.
                    checked()
                }

                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    // Only approximate location access granted.
                    checked()
                }

                else -> {
                    // No location access granted.
                    findNavController().navigate(R.id.action_splashFragment_to_filterFragment)
                }
            }
        }

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionRequest.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        } else {
            checked()
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocationInformation() {
        flpc.lastLocation.addOnSuccessListener(requireActivity()) {
            if (it != null) {
                onLocationChanged(it)
                //checked()
                splashViewModel.saveLocationLatLng(it.latitude, it.longitude)
            } else {
            }
        }.addOnFailureListener { it -> Log.d("Location Error", it.localizedMessage.toString()) }
    }

    override fun onLocationChanged(location : Location) {
        Log.d("Onlocationchanged: ", "Girdi")
        val geocoder = Geocoder(this.requireContext(), Locale.getDefault())
        val locationList = geocoder.getFromLocation(location.latitude, location.longitude, 1)
        locationList.forEach {
            it?.toString()?.let { it1 -> Log.d("Address: ", it1) }
            if (it == null) {
                val alertDialog =
                    AlertDialog.Builder(this.requireContext()).setTitle(R.string.error)
                        .setMessage(R.string.not_found_pharmacy).setCancelable(true)
                        .setPositiveButton(R.string.cancel) { dialogInterface, _ ->
                            dialogInterface.cancel()
                        }.setNegativeButton(
                            R.string.try_again
                        ) { _, _ ->
                            onStart()
                        }
                alertDialog.show()
            } else {
                Log.d("Latitude: ", it.latitude.toString())
                Log.d("Longitude: ", it.longitude.toString())
                //splashViewModel.saveLocationLatLng(it.latitude, it.longitude)
                splashViewModel.saveCityTown(it.adminArea, it.subAdminArea)
                findNavController().navigate(R.id.action_splashFragment_to_mapsFragment2)
            }
        }
    }

    private fun checked() {
        val location = LocationRequest.create().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(60000).setFastestInterval(5000)
        val builder = LocationSettingsRequest.Builder().addLocationRequest(location)
        val client = LocationServices.getSettingsClient(this.requireContext())
        val task = client.checkLocationSettings(builder.build())
        task.addOnSuccessListener {
            checkPermissionAndOpenMaps()
        }
        task.addOnFailureListener {
            if (it is ResolvableApiException) {
                try {
                    val intentSenderRequest : IntentSenderRequest =
                        IntentSenderRequest.Builder(it.resolution.intentSender)
                            .build()
                    locationRequestHandler.launch(intentSenderRequest)
                } catch (sendEx : IntentSender.SendIntentException) {
                    // Ignore the error.
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private val locationRequestHandler = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) {
        flpc.getCurrentLocation(100, null)
            .addOnSuccessListener {
                if (it != null)
                    onLocationChanged(it)
                else
                    Log.d("Olmadı be usta. ", "Çalışmadı")
            }
    }

    private fun checkPermissionAndOpenMaps() {
        if (ActivityCompat.checkSelfPermission(
                this.requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this.requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("Location: ", "Location open failed!")
            findNavController().navigate(R.id.action_splashFragment_to_filterFragment)
        } else {
            Log.d("Location: ", "Location opened!")
            locationTask = flpc.lastLocation
            getLocationInformation()
        }
    }
}