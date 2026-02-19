package com.echcoding.carcaremanager.domain.repository

import com.echcoding.carcaremanager.domain.model.Expense
import kotlinx.coroutines.flow.Flow

/**
 * This is an abstraction layer between the data layer and the domain layer.
 * It allows us to change the implementation of the data layer without changing the domain layer.
 * Domain layer is not allowed to anything outside
 */
interface ExpenseRepository {
    suspend fun upsertExpense(expense: Expense)
    suspend fun deleteExpenseById(id: Long)
    fun getExpensesByVehicle(vehicleId: Int): Flow<List<Expense>>
    suspend fun getExpenseById(id: Long): Expense?
    fun getExpensesByMaintenance(vehicleId: Int, maintenanceId: Long): Flow<List<Expense>>
}