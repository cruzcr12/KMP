package com.echcoding.carcaremanager.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.echcoding.carcaremanager.data.database.entity.ExpenseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    @Upsert
    suspend fun upsertExpense(expense: ExpenseEntity)

    @Query("DELETE FROM expense WHERE id = :id")
    suspend fun deleteExpenseById(id: Long)

    @Query("SELECT * FROM expense WHERE vehicleId= :vehicleId")
    fun getExpensesByVehicle(vehicleId: Int): Flow<List<ExpenseEntity>>

    @Query("SELECT * FROM expense WHERE id = :id")
    suspend fun getExpenseById(id: Long): ExpenseEntity

    @Query("SELECT * FROM expense WHERE vehicleId= :vehicleId AND maintenanceName LIKE :maintenanceName")
    fun getExpensesByMaintenance(vehicleId: Int, maintenanceName: String): Flow<List<ExpenseEntity>>

}