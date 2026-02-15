package com.echcoding.carcaremanager.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Describes the structure of the service expenses in the database
 */
@Entity(tableName = "expense")
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val vehicleId: Int,
    val maintenanceName: String,
    val date: Long,
    val mileage: Int,
    val mileageUnit: String,
    val amount: Double?,
    val typeOfService: String,
    val note: String?
)
