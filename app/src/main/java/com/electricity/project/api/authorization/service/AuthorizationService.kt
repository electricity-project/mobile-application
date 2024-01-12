package com.electricity.project.api.authorization.service

import com.electricity.project.api.authorization.entity.LoginRequestDTO
import com.electricity.project.api.authorization.entity.LoginResponseDTO
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthorizationService {

    companion object {
        const val BASE_URL: String = "api/login"
    }

    @POST(BASE_URL)
    fun login(@Body loginRequest: LoginRequestDTO): Response<LoginResponseDTO>

    @POST("$BASE_URL/refresh-token")
    fun refreshToken(
        @Header("Authorization") token: String,
        @Query("token") refreshToken: String
    ): Response<LoginResponseDTO>
}