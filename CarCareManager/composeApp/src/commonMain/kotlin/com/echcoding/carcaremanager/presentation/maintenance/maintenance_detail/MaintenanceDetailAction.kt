package com.echcoding.carcaremanager.presentation.maintenance.maintenance_detail

import com.echcoding.carcaremanager.domain.model.Maintenance

sealed interface MaintenanceDetailAction {
    data object OnBackClick: MaintenanceDetailAction
    data object OnSaveMaintenanceClick: MaintenanceDetailAction
    data class OnStateChange(var maintenance: Maintenance?): MaintenanceDetailAction
    data class OnSelectedMaintenanceChange(val maintenance: Maintenance): MaintenanceDetailAction
    data class OnDeleteMaintenanceClick(var maintenanceId: Long?): MaintenanceDetailAction
    data object OnConfirmDeleteMaintenance : MaintenanceDetailAction
    data object OnDismissDeleteDialog : MaintenanceDetailAction
}