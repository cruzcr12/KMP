package com.echcoding.carcaremanager.data.repository

import com.echcoding.carcaremanager.data.database.dao.MaintenanceDao
import com.echcoding.carcaremanager.data.database.dao.VehicleDao
import com.echcoding.carcaremanager.data.mappers.toMaintenance
import com.echcoding.carcaremanager.data.mappers.toVehicle
import com.echcoding.carcaremanager.data.mappers.toVehicleEntity
import com.echcoding.carcaremanager.domain.model.Vehicle
import com.echcoding.carcaremanager.domain.repository.VehicleRepository
import com.echcoding.carcaremanager.presentation.core.extensions.calculateStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * A repository is a design pattern used to combine local and remote data sources.
 * Data layer is allowed to access classes from the domain layer
 */
class VehicleRepositoryImpl(
    private val vehicleDao: VehicleDao,
    private val maintenanceDao: MaintenanceDao
): VehicleRepository {
    override suspend fun upsertVehicle(vehicle: Vehicle) {
        // If saving an active vehicle, deactivate all other vehicles
        if (vehicle.active) {
            vehicleDao.deactivateAllVehicles()
        }
        vehicleDao.upsertVehicle(vehicle.toVehicleEntity())
    }

    override suspend fun deleteVehicleById(id: Int) {
        vehicleDao.deleteAllByVehicle(id)
    }

    override suspend fun getVehicleById(id: Int): Vehicle? {
        return vehicleDao.getVehicleById(id)?.toVehicle()
    }

    override fun getAllVehicles(): Flow<List<Vehicle>> {
        return vehicleDao.getAllVehicles()
            .map { entities ->
                entities.map { it.toVehicle() }
            }
    }

    override fun getActiveVehicle(): Flow<Vehicle?> {
        return vehicleDao.getActiveVehicle().map { it?.toVehicle() }
    }

    override suspend fun setActiveVehicle(vehicleId: Int) {
        // Uses the database transaction to ensure that the active vehicle is updated atomically
        vehicleDao.selectVehicle(vehicleId)
    }

    // Gets the last vehicle added to the database
    override suspend fun getLastVehicleAdded(): Vehicle? {
        return vehicleDao.getLastVehicleAdded()?.toVehicle()
    }

    override suspend fun updateVehicleOdometer(vehicleId: Int, newOdometer: Int) {
        // Update the vehicle's mileage first
        vehicleDao.updateVehicleOdometer(vehicleId, newOdometer)
        // Fetch all maintenance tasks for the vehicle
        val maintenanceTasks = maintenanceDao.getMaintenanceByVehicleIdSync(vehicleId)
        // Update the status of each maintenance task
        val updatedTasks = maintenanceTasks.map { task ->
            val domainModel = task.toMaintenance()
            val newStatus = domainModel.calculateStatus(newOdometer)
            // Updates the status of the maintenance task
            task.copy(status = newStatus.status.name.uppercase() )
        }
        // Update the maintenance tasks in the database
        maintenanceDao.upsertAll(updatedTasks)
    }
}
