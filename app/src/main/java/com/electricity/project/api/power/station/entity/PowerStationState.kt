package com.electricity.project.api.power.station.entity

import androidx.annotation.StringRes
import com.electricity.project.R

enum class PowerStationState(@StringRes val stateName: Int) {
    WORKING(R.string.working_state),
    STOPPED(R.string.stopped_state),
    DAMAGED(R.string.damaged_state),
    MAINTENANCE(R.string.maintenance_state)
}
