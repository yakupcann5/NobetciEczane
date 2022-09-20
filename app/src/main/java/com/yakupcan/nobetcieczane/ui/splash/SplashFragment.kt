package com.yakupcan.nobetcieczane.ui.splash

import android.Manifest
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.yakupcan.nobetcieczane.R
import com.yakupcan.nobetcieczane.databinding.FragmentSplashBinding
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.tasks.Task
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class SplashFragment : Fragment(), LocationListener {
    private lateinit var binding: FragmentSplashBinding
    private val splashViewModel: SplashViewModel by viewModels()
    private var permissionControl = 0
    private lateinit var flpc: FusedLocationProviderClient
    private lateinit var locationTask: Task<Location>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSplashBinding.inflate(inflater)
        flpc = LocationServices.getFusedLocationProviderClient(this.requireContext())
        Handler(Looper.getMainLooper()).postDelayed({
            findNavController().navigate(R.id.action_splashFragment_to_mapsFragment2)
        }, 5000)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //onLocationChanged(splashViewModel.getLat(),splashViewModel.getLng())
        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    // Precise location access granted.
                    location()
                    checked()
                }
                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    // Only approximate location access granted.
                    location()
                }
                else -> {
                    // No location access granted.
                    findNavController().navigate(R.id.action_splashFragment_to_filterFragment)
                }
            }
        }
        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }
    private fun getLocationInformation() {
        locationTask.addOnSuccessListener {
            if (it != null) {
                onLocationChanged(it)
                checked()
                splashViewModel.saveLocationLatLng(it.latitude, it.longitude)
            }
        }.addOnFailureListener { it -> Log.d("Location Error", it.localizedMessage) }
    }

//    @SuppressLint("MissingSuperCall")
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        if (requestCode == 100) {
//            if (permissions.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Log.d("yakup", "izin alındı")
//                getLocationInformation()
//
//            } else {
//                permissionControl = ContextCompat.checkSelfPermission(
//                    this.requireContext(),
//                    Manifest.permission.ACCESS_FINE_LOCATION
//                )
//                locationTask = flpc.lastLocation
//                getLocationInformation()
//            }
//        }
//    }


    private fun location() {
        permissionControl = ContextCompat.checkSelfPermission(
            this.requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (permissionControl != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this.requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                100
            )
        } else {
            locationTask = flpc.lastLocation
            getLocationInformation()

        }
    }

    override fun onLocationChanged(location: Location) {
        val geocoder = Geocoder(this.requireContext(), Locale.getDefault())
        val locationList = geocoder.getFromLocation(location.latitude, location.longitude, 1)
        locationList.forEach {
            splashViewModel.saveCityTown(it.adminArea,it.subAdminArea)
        }
    }

    private fun checked() {
        val location = LocationRequest.create().setInterval(10000).setFastestInterval(5000)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        val builder = LocationSettingsRequest.Builder().addLocationRequest(location)
        val client = LocationServices.getSettingsClient(this.requireContext())
        val task = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener {
            Log.d("location settings", it.toString())
            //location()
        }
        task.addOnFailureListener {
            if (it is ResolvableApiException) {
                try {
                    val resolvable = it as ResolvableApiException
                    resolvable.startResolutionForResult(this.requireActivity(), 2)
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }
            }
        }
    }

    @Deprecated(
        "Deprecated in Java", ReplaceWith(
            "super.onActivityResult(requestCode, resultCode, data)",
            "androidx.fragment.app.Fragment"
        )
    )
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == AppCompatActivity.RESULT_OK && requestCode == 2) {
            Log.d("result ok", data.toString())
            location()
        } else if (resultCode == AppCompatActivity.RESULT_CANCELED) {
            Log.d("result cancelled", data.toString())
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}