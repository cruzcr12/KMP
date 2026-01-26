package com.echcoding.carcaremanager.presentation.maintenance.maintenance_list

sealed interface MaintenanceListSideEffect {
    data class ShowSnackbar(val message: String): MaintenanceListSideEffect
}