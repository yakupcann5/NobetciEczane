package com.android.nobetcieczane.ui.maps

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.nobetcieczane.common.RequestState
import com.android.nobetcieczane.data.model.DataDto
import com.android.nobetcieczane.domain.model.Info
import com.android.nobetcieczane.domain.use_case.getPharmcy.GetPharmacyUseCase
import com.android.nobetcieczane.util.MyPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import com.android.nobetcieczane.domain.model.Marker
import com.android.nobetcieczane.domain.model.NowLocation
import com.android.nobetcieczane.domain.model.Pharmacy
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory

@HiltViewModel
class MapsFragmentViewModel @Inject constructor(
    private var getPharmacyUseCase: GetPharmacyUseCase,
    private val preferences: MyPreferences
) :
    ViewModel() {
    var infoList = MutableLiveData<ArrayList<Info>>()
    var markerList = MutableLiveData<ArrayList<Marker>>()
    private val _pharmacy = MutableStateFlow<RequestState<ArrayList<DataDto>>?>(null)
    var pharmacy: StateFlow<RequestState<ArrayList<DataDto>>?> = _pharmacy

    fun getPharmacy() {
        getPharmacyUseCase().onEach { result ->
            result.data?.let { it ->
                val list = arrayListOf<Marker>()
                val info = arrayListOf<Info>()
                it.forEach { dto ->
                    list.add(
                        Marker(
                            dto.eczaneAdi,
                            dto.latitude,
                            dto.longitude
                        )
                    )
                }
                it.forEach { dataDto ->
                    info.add(
                        Info(
                            dataDto.eczaneAdi,
                            dataDto.adresi,
                            dataDto.telefon
                        )
                    )
                }
                infoList.value = info
                markerList.value = list
                preferences.setData(it)
            }
        }.launchIn(viewModelScope)
    }

    fun bitmapDescriptor(context: Context, vectorID: Int): BitmapDescriptor {
        val vectorDrawable: Drawable? = ContextCompat.getDrawable(context, vectorID)
        vectorDrawable?.setBounds(
            0,
            0,
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight
        )
        val bitmap: Bitmap = Bitmap.createBitmap(
            vectorDrawable!!.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    fun saveLocationLatLng(lat: Double, lng: Double) {
        preferences.setLatitude(lat)
        preferences.setLongitude(lng)
    }

    fun getLat(): Float {
        return preferences.getLatitude()
    }

    fun getLng(): Float {
        return preferences.getLongitude()
    }

    fun getPhoneNum(): String? {
        return preferences.getPhoneNumber()
    }

    fun setPhoneNum(phoneNumber: String) {
        preferences.setPhoneNumber(phoneNumber)
    }

    fun saveMarkerLocation(lat: Double, lng: Double) {
        preferences.setMarkerLocationLat(lat)
        preferences.setMarkerLocationLng(lng)
    }

    fun getMarkerLat(): Float {
        return preferences.getMarkerLocationLat()
    }

    fun getMarkerLng():Float {
        return preferences.getMarkerLocationLng()
    }
}