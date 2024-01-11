package com.electricity.project.api.entity

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

data class AggregatedPowerProductionDTO(
    @JsonProperty("aggregatedValue") val aggregatedValue: Long?,
    @JsonProperty("powerStations") val powerStations: Long?,
    @JsonProperty("timestamp") val timestamp: LocalDateTime
)
