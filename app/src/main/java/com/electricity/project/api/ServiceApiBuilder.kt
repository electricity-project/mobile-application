package com.electricity.project.api

import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.electricity.project.api.service.AggregatedPowerProductionService
import com.electricity.project.api.service.PowerStationService
import com.electricity.project.api.viewmodel.PowerProductionViewModel
import com.electricity.project.api.viewmodel.PowerStationViewModel
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

object ServiceApiBuilder {
    private const val LOGIC_URL: String = "http://localhost:8084/"
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(LOGIC_URL)
        .addConverterFactory(
            JacksonConverterFactory.create(
                ObjectMapper()
                    .registerKotlinModule()
                    .registerModule(JavaTimeModule())
            )
        )
        .build()

    val viewModelFactory = viewModelFactory {
        initializer {
            PowerProductionViewModel(retrofit.create(AggregatedPowerProductionService::class.java))
        }
        initializer {
            PowerStationViewModel(retrofit.create(PowerStationService::class.java))
        }
    }
}