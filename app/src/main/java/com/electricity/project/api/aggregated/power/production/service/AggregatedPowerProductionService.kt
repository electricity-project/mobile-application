package com.electricity.project.api.aggregated.power.production.service

import com.electricity.project.api.aggregated.power.production.entity.AggregatedPowerProductionDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface AggregatedPowerProductionService {
    @GET("aggregated-power-production")
    fun getAggregatedPowerProduction(
        @Query("duration") duration: Long,
        @Query("aggregationPeriodType") periodType: String
    ): Call<List<AggregatedPowerProductionDTO>>
}