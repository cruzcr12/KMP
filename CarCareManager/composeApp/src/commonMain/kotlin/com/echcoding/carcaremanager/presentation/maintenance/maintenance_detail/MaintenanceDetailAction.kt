package com.echcoding.carcaremanager.presentation.maintenance.maintenance_detail

import com.echcoding.carcaremanager.domain.model.Maintenance
import kotlinx.datetime.LocalDate

sealed interface MaintenanceDetailAction {
    data object OnBackClick: MaintenanceDetailAction
    data object OnSaveClick: MaintenanceDetailAction
    data class OnDeleteClick(var maintenanceId: Long?): MaintenanceDetailAction
    data class OnStateChange(var maintenance: Maintenance?): MaintenanceDetailAction
    data class OnInitialDateChange(val date: LocalDate) : MaintenanceDetailAction
    data class OnSelectedMaintenanceChange(val maintenance: Maintenance): MaintenanceDetailAction
}