package com.electricity.project.api.power.station.service

import com.electricity.project.api.power.station.entity.PowerStationState
import retrofit2.Call
import retrofit2.http.GET


interface PowerStationService {

    companion object {
        const val BASE_URL: String = "power-station"
    }

    @GET("$BASE_URL/count")
    fun getPowerStationsStatusCount(): Call<Map<PowerStationState, Int>>
}