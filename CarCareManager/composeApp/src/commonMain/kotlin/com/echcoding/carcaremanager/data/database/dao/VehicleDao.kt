package com.echcoding.carcaremanager.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.echcoding.carcaremanager.data.database.entity.VehicleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VehicleDao {

    @Upsert
    suspend fun upsertVehicle(vehicle: VehicleEntity)

    @Query("DELETE FROM vehicles WHERE id = :id")
    suspend fun deleteVehicleById(id: Int)

    @Query("SELECT * FROM vehicles")
    fun getAllVehicles(): Flow<List<VehicleEntity>>

    @Query("SELECT * FROM vehicles WHERE id = :id")
    suspend fun getVehicleById(id: Int): VehicleEntity?

    @Query("SELECT * FROM vehicles WHERE active = 1 LIMIT 1")
    fun getActiveVehicle(): Flow<VehicleEntity?>

    @Query("UPDATE vehicles SET active = 0")
    suspend fun deactivateAllVehicles()

    @Query("UPDATE vehicles SET active = 1 WHERE id = :id")
    suspend fun activateVehicle(id: Int)

    @Transaction
    suspend fun selectVehicle(id: Int) {
        deactivateAllVehicles()
        activateVehicle(id)
    }
}
