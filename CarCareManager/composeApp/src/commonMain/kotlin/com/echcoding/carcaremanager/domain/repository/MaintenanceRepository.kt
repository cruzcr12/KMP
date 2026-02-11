package com.echcoding.carcaremanager.domain.repository

import com.echcoding.carcaremanager.domain.model.Maintenance
import kotlinx.coroutines.flow.Flow

/**
 * This is an abstraction layer between the data layer and the domain layer.
 * It allows us to change the implementation of the data layer without changing the domain layer.
 * Domain layer is not allowed to anything outside
 */
interface MaintenanceRepository {
    suspend fun upsertMaintenance(maintenance: Maintenance)
    suspend fun insertMultipleMaintenances(maintenances: List<Maintenance>)
    suspend fun deleteMaintenanceById(id: Long)
    suspend fun getMaintenanceById(id: Long): Maintenance?

    // Get all the maintenances tasks for a specific vehicle
    fun getMaintenancesForVehicle(vehicleId: Int): Flow<List<Maintenance>>

}