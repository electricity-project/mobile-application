package com.electricity.project.api

import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.electricity.project.api.aggregated.power.production.service.AggregatedPowerProductionService
import com.electricity.project.api.authorization.service.AuthorizationService
import com.electricity.project.api.power.station.service.PowerStationService
import com.electricity.project.api.authorization.viewmodel.AuthorizationViewModel
import com.electricity.project.api.aggregated.power.production.viewmodel.PowerProductionViewModel
import com.electricity.project.api.power.station.viewmodel.PowerStationViewModel
import com.electricity.project.api.token.TokenViewModel
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

object ServiceApiBuilder {
    const val LOGIC_URL: String = "http://192.168.1.128:8084/"

    val retrofit: Retrofit = Retrofit.Builder()
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
        initializer {
            AuthorizationViewModel(retrofit.create(AuthorizationService::class.java))
        }
    }
}