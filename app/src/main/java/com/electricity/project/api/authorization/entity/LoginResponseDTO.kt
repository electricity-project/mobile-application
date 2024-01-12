package com.electricity.project.api.authorization.entity

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class LoginResponseDTO(
    @JsonProperty("access_token") val accessToken: String,
    @JsonProperty("expires_in") val expiresInSeconds: Long,
    @JsonProperty("refresh_expires_in") val refreshExpiresInSeconds: Long,
    @JsonProperty("refresh_token") val refreshToken: String,
)
