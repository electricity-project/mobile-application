package com.electricity.project.api.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.electricity.project.api.entity.PowerStationState
import com.electricity.project.api.service.PowerStationService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class PowerStationViewModel(
    private val powerStationService: PowerStationService
) : ViewModel() {

    private val _powerStationsCount = MutableStateFlow<Map<PowerStationState, Int>>(emptyMap())
    val powerStationsCount: StateFlow<Map<PowerStationState, Int>> = _powerStationsCount

    fun getPowerStationsStatusCount() {
        viewModelScope.launch(context = Dispatchers.IO) {
            try {
                val response = powerStationService.getPowerStationsStatusCount().execute().body()
                if (response != null) _powerStationsCount.value = response
            } catch (e: Exception) {
                Log.e(
                    PowerStationViewModel::class.java.toString(),
                    "getPowerStationsStatusCount: ",
                    e
                )
            }

        }
    }
}