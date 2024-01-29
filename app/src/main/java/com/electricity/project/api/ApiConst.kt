package com.electricity.project.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

object ApiConst {
    //Emulator address
   // private const val LOGIC_URL: String = "http://10.0.2.2:8084/api/"

    // Production address
    private const val LOGIC_URL: String = "http://localhost:8084/api/"

    val retrofitBasicBuilder: Retrofit.Builder = Retrofit.Builder()
        .baseUrl(LOGIC_URL)
        .addConverterFactory(
            JacksonConverterFactory.create(
                ObjectMapper()
                    .registerKotlinModule()
                    .registerModule(JavaTimeModule())
            )
        )
}