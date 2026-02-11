package com.echcoding.carcaremanager.data.repository

import com.echcoding.carcaremanager.data.database.dao.MaintenanceDao
import com.echcoding.carcaremanager.data.mappers.toMaintenance
import com.echcoding.carcaremanager.data.mappers.toMaintenanceEntity
import com.echcoding.carcaremanager.domain.model.Maintenance
import com.echcoding.carcaremanager.domain.repository.MaintenanceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * A repository is a design pattern used to combine local and remote data sources.
 * Data layer is allowed to access classes from the domain layer
 */
class MaintenanceRepositoryImpl(
    private val maintenanceDao: MaintenanceDao
): MaintenanceRepository {
    override suspend fun upsertMaintenance(maintenance: Maintenance) {
        maintenanceDao.upsertMaintenance(maintenance.toMaintenanceEntity())
    }

    override suspend fun deleteMaintenanceById(id: Long) {
        maintenanceDao.deleteMaintenanceById(id)
    }

    override suspend fun insertMultipleMaintenances(maintenances: List<Maintenance>) {
        maintenanceDao.insertMultipleMaintenances(maintenances.map { it.toMaintenanceEntity() })
    }

    override suspend fun getMaintenanceById(id: Long): Maintenance {
        return maintenanceDao.getMaintenanceById(id).toMaintenance()
    }

    override fun getMaintenancesForVehicle(vehicleId: Int): Flow<List<Maintenance>> {
        return maintenanceDao.getMaintenanceByVehicleId(vehicleId)
            .map { entities ->
                entities.map { it.toMaintenance() }
            }
    }

}