package com.echcoding.carcaremanager.data.repository

import com.echcoding.carcaremanager.data.database.dao.VehicleDao
import com.echcoding.carcaremanager.data.mappers.toVehicle
import com.echcoding.carcaremanager.data.mappers.toVehicleEntity
import com.echcoding.carcaremanager.domain.model.Vehicle
import com.echcoding.carcaremanager.domain.repository.VehicleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * A repository is a design pattern used to combine local and remote data sources.
 * Data layer is allowed to access classes from the domain layer
 */
class VehicleRepositoryImpl(
    private val vehicleDao: VehicleDao
): VehicleRepository {
    override suspend fun upsertVehicle(vehicle: Vehicle) {
        vehicleDao.upsertVehicle(vehicle.toVehicleEntity())
    }

    override suspend fun deleteVehicleById(id: Long) {
        vehicleDao.deleteVehicleById(id)
    }

    override suspend fun getVehicleById(id: Int): Vehicle? {
        return vehicleDao.getVehicleById(id)?.toVehicle()
    }

    override suspend fun getAllVehicles(): Flow<List<Vehicle>> {
        return vehicleDao.getAllVehicles()
            .map { vehicle ->
                vehicle.map { it.toVehicle() }
            }
    }

}