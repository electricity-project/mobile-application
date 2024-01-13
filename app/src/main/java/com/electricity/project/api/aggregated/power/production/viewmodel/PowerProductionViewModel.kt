package com.electricity.project.api.aggregated.power.production.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.electricity.project.api.aggregated.power.production.entity.AggregatedPowerProductionDTO
import com.electricity.project.api.aggregated.power.production.service.AggregatedPowerProductionService
import com.electricity.project.api.base.ApiResponse
import com.electricity.project.api.base.BaseViewModel
import com.electricity.project.api.base.CoroutinesErrorHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class PowerProductionViewModel @Inject constructor(
    private val powerProductionService: AggregatedPowerProductionService
) : BaseViewModel() {

    private val _aggregatedPowerProduction =
        MutableLiveData<ApiResponse<List<AggregatedPowerProductionDTO>>>(ApiResponse.Empty)

    val aggregatedPowerProduction: LiveData<AggregatedPowerProductionDTO> =
        _aggregatedPowerProduction.map {
            return@map when (it) {
                is ApiResponse.Success -> {
                    val production = it.data.filter { powerProduction ->
                        powerProduction.powerStations != null && powerProduction.aggregatedValue != null
                    }

                    if (production.isEmpty()) {
                        AggregatedPowerProductionDTO()
                    } else {
                        production.first()
                    }
                }

                else -> AggregatedPowerProductionDTO()
            }

        }

    fun getAggregatedPowerProduction() = baseRequest(_aggregatedPowerProduction,
        object : CoroutinesErrorHandler {
            override fun onError(message: String) {
                Log.w(PowerProductionViewModel::class.java.toString(), message)
            }
        }) {
        powerProductionService.getAggregatedPowerProduction(2, "MINUTE")
    }
}