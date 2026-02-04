package com.echcoding.carcaremanager.presentation.maintenance.maintenance_detail

import com.echcoding.carcaremanager.domain.model.Maintenance

data class MaintenanceDetailState(
    val maintenance: Maintenance? = null,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val isEditing: Boolean = false,
    val errorMessage: String? = null
)
