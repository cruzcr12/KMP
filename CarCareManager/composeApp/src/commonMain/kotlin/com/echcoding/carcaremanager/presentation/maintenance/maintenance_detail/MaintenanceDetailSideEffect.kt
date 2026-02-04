package com.echcoding.carcaremanager.presentation.maintenance.maintenance_detail

sealed interface MaintenanceDetailSideEffect {
    data object NavigateBack: MaintenanceDetailSideEffect
    data class ShowToast(val message: String): MaintenanceDetailSideEffect
}