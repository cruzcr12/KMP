package com.echcoding.carcaremanager.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.echcoding.carcaremanager.data.database.entity.MaintenanceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MaintenanceDao {

    @Upsert
    suspend fun upsertMaintenance(maintenance: MaintenanceEntity)

    @Query("DELETE FROM maintenance WHERE id = :id")
    suspend fun deleteMaintenanceById(id: Long)

    @Query("SELECT * FROM maintenance WHERE vehicleId = :vehicleId")
    fun getMaintenanceByVehicleId(vehicleId: Int): Flow<List<MaintenanceEntity>>

    @Query("SELECT * FROM maintenance WHERE id = :id")
    suspend fun getMaintenanceById(id: Long): MaintenanceEntity


}