package com.electricity.project.api

import android.content.Context
import com.electricity.project.api.aggregated.power.production.service.AggregatedPowerProductionService
import com.electricity.project.api.authorization.service.AuthorizationService
import com.electricity.project.api.power.station.service.PowerStationService
import com.electricity.project.api.token.AuthAuthenticator
import com.electricity.project.api.token.AuthorizationInterceptor
import com.electricity.project.api.token.TokenManager
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SingletonModule {

    @Singleton
    @Provides
    fun provideTokenManager(@ApplicationContext context: Context): TokenManager =
        TokenManager(context)

    @Singleton
    @Provides
    fun provideOkHttpClient(
        authInterceptor: AuthorizationInterceptor,
        authAuthenticator: AuthAuthenticator,
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .authenticator(authAuthenticator)
            .build()
    }

    @Singleton
    @Provides
    fun provideAuthInterceptor(tokenManager: TokenManager): AuthorizationInterceptor =
        AuthorizationInterceptor(tokenManager)

    @Singleton
    @Provides
    fun provideAuthAuthenticator(tokenManager: TokenManager): AuthAuthenticator =
        AuthAuthenticator(tokenManager)

    @Singleton
    @Provides
    fun provideRetrofitBuilder(): Retrofit.Builder =
        Retrofit.Builder()
            .baseUrl(ServiceApiBuilder.LOGIC_URL)
            .addConverterFactory(
                JacksonConverterFactory.create(
                    ObjectMapper()
                        .registerKotlinModule()
                        .registerModule(JavaTimeModule())
                )
            )

    @Singleton
    @Provides
    fun provideAuthAPIService(retrofit: Retrofit.Builder): AuthorizationService =
        retrofit.build()
            .create(AuthorizationService::class.java)

    @Singleton
    @Provides
    fun provideAggregatedPowerProductionService(
        okHttpClient: OkHttpClient,
        retrofit: Retrofit.Builder
    ): AggregatedPowerProductionService = retrofit
        .client(okHttpClient)
        .build()
        .create(AggregatedPowerProductionService::class.java)


    @Singleton
    @Provides
    fun providePowerStationService(
        okHttpClient: OkHttpClient,
        retrofit: Retrofit.Builder
    ): PowerStationService = retrofit
        .client(okHttpClient)
        .build()
        .create(PowerStationService::class.java)

}