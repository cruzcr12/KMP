package com.echcoding.carcaremanager.data.mappers

import com.echcoding.carcaremanager.data.database.entity.MaintenanceEntity
import com.echcoding.carcaremanager.domain.model.ControlType
import com.echcoding.carcaremanager.domain.model.Maintenance
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime


@OptIn(ExperimentalTime::class)
fun MaintenanceEntity.toMaintenance(): Maintenance {
    return Maintenance(
        id = id,
        vehicleId = vehicleId,
        name = name,
        description = description,
        initialOdometer = initialOdometer,
        // Convert Long (Millis) back to LocalDate
        initialDate = Instant.fromEpochMilliseconds(initialDate)
            .toLocalDateTime(TimeZone.currentSystemDefault()).date,
        odometerInterval = odometerInterval,
        dateInterval = dateInterval,
        controlType = ControlType.valueOf(controlType)
    )
}

@OptIn(ExperimentalTime::class)
fun Maintenance.toMaintenanceEntity(): MaintenanceEntity {
    return MaintenanceEntity(
        id = id ?: 0,
        vehicleId = vehicleId,
        name = name,
        description = description,
        initialOdometer = initialOdometer,
        // Convert LocalDate to Long (Millis) for storage
        initialDate = initialDate.atStartOfDayIn(TimeZone.currentSystemDefault()).toEpochMilliseconds(),
        odometerInterval = odometerInterval,
        dateInterval = dateInterval,
        controlType = controlType.name
    )
}