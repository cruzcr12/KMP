package com.echcoding.carcaremanager.domain.model

data class Vehicle(
    val id: Long?,
    val name: String,
    val maker: String,
    val model: String,
    val year: Int,
    val licensePlate: String?,
    val fuelType: FuelType,
    val odometer: Int,
    val odometerUnit: OdometerUnit,
    val active: Boolean
)

enum class FuelType { GAS, DIESEL, ELECTRIC, HYBRID }
enum class OdometerUnit { MILES, KILOMETERS }
