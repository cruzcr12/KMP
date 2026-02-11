package com.echcoding.carcaremanager.domain.repository

import com.echcoding.carcaremanager.domain.model.Vehicle
import kotlinx.coroutines.flow.Flow

/**
 * This is an abstraction layer between the dta layer and the domain layer.
 * It allows us to change the implementation of the data layer without changing the domain layer.
 * Domain layer is not allowed to anything outside
 */
interface VehicleRepository {
    suspend fun upsertVehicle(vehicle: Vehicle)
    suspend fun deleteVehicleById(id: Int)
    suspend fun getVehicleById(id: Int): Vehicle?
    suspend fun getAllVehicles(): Flow<List<Vehicle>>

    fun getActiveVehicle(): Flow<Vehicle?>
    suspend fun setActiveVehicle(vehicleId: Int)
    suspend fun getLastVehicleAdded(): Vehicle?
}
