package com.echcoding.carcaremanager.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.echcoding.carcaremanager.data.database.entity.ExpenseEntity
import com.echcoding.carcaremanager.data.database.model.ExpenseWithMaintenance
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    @Upsert
    suspend fun upsertExpense(expense: ExpenseEntity)

    @Query("DELETE FROM expense WHERE id = :id")
    suspend fun deleteExpenseById(id: Long)

    @Query("""
        SELECT expense.*, maintenance.name AS maintenanceName 
        FROM expense 
        INNER JOIN maintenance ON expense.maintenanceId = maintenance.id 
        WHERE expense.vehicleId= :vehicleId
        ORDER BY expense.date DESC
        """)
    fun getExpensesByVehicle(vehicleId: Int): Flow<List<ExpenseWithMaintenance>>

    @Query("""
        SELECT expense.*, maintenance.name AS maintenanceName
        FROM expense 
        INNER JOIN maintenance ON expense.maintenanceId = maintenance.id
        WHERE expense.id = :id
        """)
    suspend fun getExpenseById(id: Long): ExpenseWithMaintenance

    @Query("""
        SELECT expense.*, maintenance.name AS maintenanceName
        FROM expense
        INNER JOIN maintenance ON expense.maintenanceId = maintenance.id
        WHERE expense.vehicleId= :vehicleId AND expense.maintenanceId= :maintenanceId
        ORDER BY expense.date DESC
        """)
    fun getExpensesByMaintenance(vehicleId: Int, maintenanceId: Long): Flow<List<ExpenseWithMaintenance>>

}