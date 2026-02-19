package com.echcoding.carcaremanager.data.database.model

import androidx.room.Embedded
import com.echcoding.carcaremanager.data.database.entity.ExpenseEntity

/**
 * This class will be used to combine the expense with the maintenance name
 * Allowing to map the maintenance name for every expense
 */
data class ExpenseWithMaintenance(
    @Embedded val expense: ExpenseEntity,
    val maintenanceName: String
)