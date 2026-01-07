package com.echcoding.carcaremanager.data.mappers

import com.echcoding.carcaremanager.data.database.entity.VehicleEntity
import com.echcoding.carcaremanager.domain.model.FuelType
import com.echcoding.carcaremanager.domain.model.OdometerUnit
import com.echcoding.carcaremanager.domain.model.Vehicle

/**
 * This extension function converts a Vehicle object (domain object) to a VehicleEntity object.
 */
fun Vehicle.toVehicleEntity(): VehicleEntity{
    return VehicleEntity(
        id = id ?: 0,
        name = name,
        maker = maker,
        model = model,
        year = year,
        licensePlate = licensePlate,
        fuelType = fuelType.name,
        odometer = odometer,
        odometerUnit = odometerUnit.name,
        active = active
    )
}

/**
 * This extension function converts a VehicleEntity object (database object) to a Vehicle object.
 */
fun VehicleEntity.toVehicle(): Vehicle {
    return Vehicle(
        id = id,
        name = name,
        maker = maker,
        model = model,
        year = year,
        licensePlate = licensePlate,
        fuelType = when(fuelType){
            FuelType.GAS.name -> FuelType.GAS
            FuelType.DIESEL.name -> FuelType.DIESEL
            FuelType.ELECTRIC.name -> FuelType.ELECTRIC
            else -> FuelType.HYBRID
        } ,
        odometer = odometer,
        odometerUnit = when(odometerUnit) {
            OdometerUnit.MILES.name -> OdometerUnit.MILES
            OdometerUnit.KILOMETERS.name -> OdometerUnit.KILOMETERS
            else -> OdometerUnit.MILES
        },
        active = active
    )
}