package com.echcoding.carcaremanager.presentation.maintenance.maintenance_list

/***
 * This interface represents the intent in the MVI architecture.
 * The intent represents an user action or event that triggers a state change
 */
sealed interface MaintenanceListAction {
    data class OnSelectMaintenanceClick(val maintenanceId: Long?) : MaintenanceListAction
    data object OnAddMaintenanceClick : MaintenanceListAction
    data class OnDeleteMaintenanceClick(val maintenanceId: Long) : MaintenanceListAction
    data object OnConfirmDeleteMaintenance: MaintenanceListAction
    data object OnDismissDeleteDialog: MaintenanceListAction
}