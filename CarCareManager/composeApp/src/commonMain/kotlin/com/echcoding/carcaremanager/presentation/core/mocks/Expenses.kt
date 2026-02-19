package com.echcoding.carcaremanager.presentation.core.mocks

import com.echcoding.carcaremanager.domain.model.Expense
import com.echcoding.carcaremanager.domain.model.TypeOfService
import kotlinx.datetime.LocalDate

fun getMockExpenses() = listOf(
    Expense(
        id = 1,
        vehicleId = 1,
        maintenanceId = 1,
        date = LocalDate(2026, 1, 1),
        mileage = 125000,
        mileageUnit = "mi",
        amount = 55.0,
        typeOfService = TypeOfService.MAINTENANCE,
        note = "Oil changed and oil filter changed in Six Flags Automotive Workshop"
    ),
    Expense(
        id = 2,
        vehicleId = 1,
        maintenanceId = 2,
        date = LocalDate(2025, 10, 15),
        mileage = 130000,
        mileageUnit = "mi",
        amount = 80.0,
        typeOfService = TypeOfService.MAINTENANCE,
        note = "Tire rotation was done at a 20% of discount"
    ),
    Expense(
        id = 3,
        vehicleId = 1,
        maintenanceId = 3,
        date = LocalDate(2025, 12, 15),
        mileage = 10000,
        mileageUnit = "mi",
        amount = 35.0,
        typeOfService = TypeOfService.REPAIRMENT,
        note = "Brake inspection was done at a 20% of discount"
    ),
)