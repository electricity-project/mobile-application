package com.electricity.project.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

object ApiConst {
    private const val LOGIC_URL: String = "http://192.168.1.128:8084/api/"

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