package com.echcoding.carcaremanager.presentation.maintenance.maintenance_detail

import com.echcoding.carcaremanager.domain.model.Maintenance

data class MaintenanceDetailState(
    val maintenance: Maintenance? = null,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val isEditing: Boolean = false,
    val errorMessage: String? = null,
    val maintenanceToDeleteId: Long? = null, // When this is not null, show confirmation dialog
    val showDeleteConfirmationDialog: Boolean = false, // When this is true, show the confirmation dialog
    val snackbarMessage: String? = null // To display any informative messages to the user
)
