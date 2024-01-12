package com.electricity.project.api.authorization.entity

import com.fasterxml.jackson.annotation.JsonProperty

data class LoginRequestDTO(
    @JsonProperty("username") val username: String,
    @JsonProperty("password") val password: String
)


