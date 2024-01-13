package com.electricity.project.api.aggregated.power.production.service

import com.electricity.project.api.aggregated.power.production.entity.AggregatedPowerProductionDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AggregatedPowerProductionService {
    @GET("aggregated-power-production")
    suspend fun getAggregatedPowerProduction(
        @Query("duration") duration: Long,
        @Query("aggregationPeriodType") periodType: String
    ): Response<List<AggregatedPowerProductionDTO>>
}