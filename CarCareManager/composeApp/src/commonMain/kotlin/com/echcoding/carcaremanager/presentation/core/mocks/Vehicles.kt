package com.echcoding.carcaremanager.presentation.core.mocks

import com.echcoding.carcaremanager.domain.model.FuelType
import com.echcoding.carcaremanager.domain.model.OdometerUnit
import com.echcoding.carcaremanager.domain.model.Vehicle

val vehicles = listOf(
    Vehicle(
        id = 1,
        name = "My Daily Driver",
        maker = "Toyota",
        model = "Camry",
        year = 2019,
        licensePlate = "ABC-123",
        fuelType = FuelType.GAS,
        odometer = 45230,
        odometerUnit = OdometerUnit.MILES,
        active = true
    ),
    Vehicle(
        id = 2,
        name = "Weekend Cruiser",
        maker = "Mazda",
        model = "MX-5",
        year = 2016,
        licensePlate = "XYZ-789",
        fuelType = FuelType.GAS,
        odometer = 28100,
        odometerUnit = OdometerUnit.MILES,
        active = false
    ),
    Vehicle(
        id = 3,
        name = "Family Hauler",
        maker = "Honda",
        model = "Odyssey",
        year = 2021,
        licensePlate = "FAM-001",
        fuelType = FuelType.GAS,
        odometer = 32500,
        odometerUnit = OdometerUnit.MILES,
        active = false
    )
)