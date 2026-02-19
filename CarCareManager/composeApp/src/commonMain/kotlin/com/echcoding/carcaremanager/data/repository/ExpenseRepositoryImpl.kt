package com.echcoding.carcaremanager.data.repository

import com.echcoding.carcaremanager.data.database.dao.ExpenseDao
import com.echcoding.carcaremanager.data.mappers.toExpense
import com.echcoding.carcaremanager.data.mappers.toExpenseEntity
import com.echcoding.carcaremanager.domain.model.Expense
import com.echcoding.carcaremanager.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ExpenseRepositoryImpl(
    private val expenseDao: ExpenseDao
): ExpenseRepository {
    override suspend fun upsertExpense(expense: Expense) {
        expenseDao.upsertExpense(expense.toExpenseEntity())
    }

    override suspend fun deleteExpenseById(id: Long) {
        expenseDao.deleteExpenseById(id)
    }

    override fun getExpensesByVehicle(vehicleId: Int): Flow<List<Expense>> {
        return expenseDao.getExpensesByVehicle(vehicleId)
            .map { entities ->
                entities.map { it.toExpense() }
            }
    }

    override suspend fun getExpenseById(id: Long): Expense {
        return expenseDao.getExpenseById(id).toExpense()
    }

    override fun getExpensesByMaintenance(
        vehicleId: Int,
        maintenanceId: Long
    ): Flow<List<Expense>> {
        return expenseDao.getExpensesByMaintenance(vehicleId, maintenanceId)
            .map { entities ->
                entities.map { it.toExpense() }
            }
    }

}