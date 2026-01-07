package com.echcoding.carcaremanager.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.echcoding.carcaremanager.data.database.entity.VehicleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VehicleDao {

    // This function will insert or update a vehicle in the database
    @Upsert
    suspend fun upsertVehicle(vehicle: VehicleEntity)

    // This function will delete a vehicle from the database
    @Query("DELETE FROM vehicles WHERE id = :id")
    suspend fun deleteVehicleById(id: Int)

    // This function will retrieve a list of all vehicles from the database
    @Query("SELECT * FROM vehicles")
    fun getAllVehicles(): Flow<List<VehicleEntity>>

    // This function will retrieve a specific vehicle from the database
    @Query("SELECT * FROM vehicles WHERE id = :id")
    suspend fun getVehicleById(id: Int): VehicleEntity?

}