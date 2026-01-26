package com.echcoding.carcaremanager.presentation.core.mocks

import com.echcoding.carcaremanager.domain.model.ControlType
import com.echcoding.carcaremanager.domain.model.Maintenance
import kotlinx.datetime.LocalDate

fun getMockMaintenances() = listOf(
    Maintenance(
        id = 1,
        vehicleId = 1,
        name = "Oil Change",
        description = "Replace oil and filter.",
        initialOdometer = 130000,
        initialDate = LocalDate(2026, 1, 1),
        odometerInterval = 2000,
        dateInterval = 120,
        controlType = ControlType.MILEAGE
    ),
    Maintenance(
        id = 2,
        vehicleId = 1,
        name = "Tire Rotation",
        description = "Rotate tires and align them. Due in 500 miles",
        initialOdometer = 130000,
        initialDate = LocalDate(2023, 8, 1),
        odometerInterval = 500,
        dateInterval = 120,
        controlType = ControlType.TIME
    ),
    Maintenance(
        id = 3,
        vehicleId = 1,
        name = "Brake Inspection",
        description = "Inspect brakes and apply brake fluid",
        initialOdometer = 128000,
        initialDate = LocalDate(2025, 8, 10),
        odometerInterval = 2000,
        dateInterval = 90,
        controlType = ControlType.BOTH
    ),
    Maintenance(
        id = 4,
        vehicleId = 1,
        name = "Cabin Air Filter",
        description = "Replace cabin air filter. Due in 2500 miles",
        initialOdometer = 128000,
        initialDate = LocalDate(2025, 4, 10),
        odometerInterval = 2500,
        dateInterval = 500,
        controlType = ControlType.BOTH
    ),
    Maintenance(
        id = 5,
        vehicleId = 1,
        name = "Tire Pressure Check",
        description = "Maintains fuel efficiency and prevents premature wear or blowouts.",
        initialOdometer = 285000,
        initialDate = LocalDate(2025, 1, 1),
        odometerInterval = 200,
        dateInterval = 30,
        controlType = ControlType.TIME
    )
)