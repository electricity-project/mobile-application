package com.electricity.project.api.service

import com.electricity.project.api.entity.AggregatedPowerProductionDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


//http://localhost:8084/aggregated-power-production?duration=61&aggregationPeriodType=MINUTE


interface AggregatedPowerProductionService {
    @GET("aggregated-power-production")
    fun getAggregatedPowerProduction(
        @Query("duration") duration: Long,
        @Query("aggregationPeriodType") periodType: String
    ): Call<List<AggregatedPowerProductionDTO>>
}