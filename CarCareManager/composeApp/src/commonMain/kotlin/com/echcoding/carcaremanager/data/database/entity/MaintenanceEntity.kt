package com.echcoding.carcaremanager.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Describes the structure of the maintenance in the database
 */
@Entity(tableName = "maintenance")
data class MaintenanceEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val vehicleId: Int,
    val name: String,
    val description: String?,
    val initialOdometer: Int,
    val initialDate: Long, // Using Long as Room doesn't support LocalDate
    val odometerInterval: Int,
    val dateInterval: Int,
    val controlType: String
)
