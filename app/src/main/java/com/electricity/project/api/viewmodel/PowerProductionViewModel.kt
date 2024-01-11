package com.electricity.project.api.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.electricity.project.api.entity.AggregatedPowerProductionDTO
import com.electricity.project.api.entity.PowerStationState
import com.electricity.project.api.service.AggregatedPowerProductionService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime


class PowerProductionViewModel(
    private val powerProductionService: AggregatedPowerProductionService
) : ViewModel() {

    private val _aggregatedPowerProduction = MutableStateFlow(
        AggregatedPowerProductionDTO(0, 0, LocalDateTime.now())
    )
    val aggregatedPowerProduction: StateFlow<AggregatedPowerProductionDTO> = _aggregatedPowerProduction

    fun getAggregatedPowerProduction() {
        viewModelScope.launch(context = Dispatchers.IO) {
            try {
                val response = powerProductionService.getAggregatedPowerProduction(2, "MINUTE")
                    .execute()
                    .body()?.first {
                        it.powerStations != null && it.aggregatedValue != null
                    }
                if (response != null) _aggregatedPowerProduction.value = response
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