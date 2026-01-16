package com.echcoding.carcaremanager.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Describes the structure of the vehicle in the database
 */
@Entity(tableName = "vehicles")
data class VehicleEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val maker: String,
    val model: String,
    val year: Int,
    val licensePlate: String?,
    val fuelType: String,
    val odometer: Int,
    val odometerUnit: String,
    val active: Boolean
)