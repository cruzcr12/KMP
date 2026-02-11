package com.echcoding.carcaremanager.presentation.maintenance.maintenance_list

import com.echcoding.carcaremanager.domain.model.Maintenance
import com.echcoding.carcaremanager.domain.model.Vehicle

/**
 * This interface represents the intent in the MVI architecture.
 * The intent represents an user action or event that triggers a state change
 */
sealed interface MaintenanceListAction {
    data object OnAddMaintenanceClick : MaintenanceListAction
    data class OnEditMaintenanceClick(val maintenance: Maintenance) : MaintenanceListAction
    data class OnActiveVehicleChanged(val vehicle: Vehicle?) : MaintenanceListAction

}