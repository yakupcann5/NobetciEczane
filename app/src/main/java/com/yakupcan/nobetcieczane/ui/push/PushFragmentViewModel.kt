package com.yakupcan.nobetcieczane.ui.push

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yakupcan.nobetcieczane.common.RequestState
import com.yakupcan.nobetcieczane.data.model.PushResponseDTO
import com.yakupcan.nobetcieczane.domain.model.PushModel
import com.yakupcan.nobetcieczane.domain.use_case.UseCases
import com.yakupcan.nobetcieczane.util.MyPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PushFragmentViewModel @Inject constructor(
    private val preferences : MyPreferences,
    private val pushUseCases : UseCases
) :
    ViewModel() {
    var serverKey = MutableLiveData<String>()
    var lastToken = MutableLiveData<String>()

    private val _pushState = MutableStateFlow<RequestState<PushResponseDTO>?>(null)
    val pushState : StateFlow<RequestState<PushResponseDTO>?> = _pushState

    init {
        serverKey.value = preferences.getServerKey()
        lastToken.value = preferences.getLastPushToken()
    }

    fun saveServerKey(key : String) {
        serverKey.value = key
        preferences.setServerKey(key)
    }

    fun savePushToken(token : String) {
        lastToken.value = token
        preferences.setLastPushToken(token)
    }

    fun pushToDevices(pushModel : PushModel, serverKey : String) {
        viewModelScope.launch {
            pushUseCases.sendPushToAllDevicesUseCase.invoke(pushModel, serverKey).collect {
                _pushState.value = it
            }
        }

    }
}
