package com.electricity.project.api.token

import com.electricity.project.api.ApiConst
import com.electricity.project.api.authorization.entity.LoginResponseDTO
import com.electricity.project.api.authorization.service.AuthorizationService
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Inject

class AuthAuthenticator @Inject constructor(
    private val tokenManager: TokenManager
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        val refreshToken = runBlocking {
            tokenManager.getToken(TokenTypes.REFRESH_BEARER_TOKEN_KEY).first()
        }
        return runBlocking {
            val newToken = getNewToken(refreshToken)

            if (!newToken.isSuccessful || newToken.body() == null) {
                tokenManager.deleteToken(TokenTypes.AUTHORIZATION_BEARER_TOKEN_KEY)
                tokenManager.deleteToken(TokenTypes.REFRESH_BEARER_TOKEN_KEY)
            }

            newToken.body()?.let {
                tokenManager.saveToken(TokenTypes.AUTHORIZATION_BEARER_TOKEN_KEY, it.accessToken)
                tokenManager.saveToken(TokenTypes.REFRESH_BEARER_TOKEN_KEY, it.refreshToken)
                response.request.newBuilder()
                    .header("Authorization", "Bearer ${it.accessToken}")
                    .build()
            }
        }
    }

    private suspend fun getNewToken(refreshToken: String?): retrofit2.Response<LoginResponseDTO> {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val okHttpClient = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()

        val retrofit = ApiConst.retrofitBasicBuilder
            .client(okHttpClient)
            .build()

        val service = retrofit.create(AuthorizationService::class.java)
        return service.refreshToken("$refreshToken")
    }
}