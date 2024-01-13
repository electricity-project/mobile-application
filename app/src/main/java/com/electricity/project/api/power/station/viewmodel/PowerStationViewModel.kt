package com.electricity.project.api.power.station.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.electricity.project.api.base.ApiResponse
import com.electricity.project.api.base.BaseViewModel
import com.electricity.project.api.base.CoroutinesErrorHandler
import com.electricity.project.api.power.station.entity.PowerStationState
import com.electricity.project.api.power.station.service.PowerStationService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class PowerStationViewModel @Inject constructor(
    private val powerStationService: PowerStationService
) : BaseViewModel() {

    private val _powerStationsCount = MutableLiveData<ApiResponse<Map<PowerStationState, Int>>>()
    val powerStationsCount: LiveData<Map<PowerStationState, Int>> = _powerStationsCount.map {
        return@map when (it) {
            is ApiResponse.Success -> it.data
            else -> emptyMap<PowerStationState, Int>()
        }
    }

    fun getPowerStationsStatusCount() = baseRequest(_powerStationsCount,
        object : CoroutinesErrorHandler {
            override fun onError(message: String) {
                Log.w(PowerStationViewModel::class.java.toString(), message)
            }
        }) { powerStationService.getPowerStationsStatusCount() }

}