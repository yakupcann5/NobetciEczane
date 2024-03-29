package com.yakupcan.nobetcieczane.ui.listfragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yakupcan.nobetcieczane.data.model.DataDto
import com.yakupcan.nobetcieczane.domain.use_case.getPharmcy.GetPharmacyUseCase
import com.yakupcan.nobetcieczane.common.RequestState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ListFragmentViewModel @Inject constructor(private var getPharmacyUseCase: GetPharmacyUseCase) :
    ViewModel() {

    private val _pharmacyState = MutableStateFlow<RequestState<ArrayList<DataDto>>?>(null)
    val pharmacyState: StateFlow<RequestState<ArrayList<DataDto>>?> = _pharmacyState

    fun getPharmacy() {
        getPharmacyUseCase().onEach { result ->
            _pharmacyState.value = result
        }.launchIn(viewModelScope)
    }
}