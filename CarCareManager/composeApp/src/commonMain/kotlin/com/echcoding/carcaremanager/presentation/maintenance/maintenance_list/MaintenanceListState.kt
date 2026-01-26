package com.echcoding.carcaremanager.presentation.maintenance.maintenance_list

import com.echcoding.carcaremanager.domain.model.Maintenance
import com.echcoding.carcaremanager.domain.model.Vehicle

/**
 * The UI in this project will use MVI architecture.
 * This class represents the Model or the entire state of the UI for the vehicle lis screen.
 * It bundles the state with all the actions that can be made in the View
 */
data class MaintenanceListState(
    var selectedVehicle: Vehicle? = null, // The selected vehicle to show the maintenance tasks for
    val tasks: List<Maintenance> = emptyList(), // The list of maintenance tasks for the selected vehicle
    val overdueTasks: Int? = null, // The amount of tasks that need to be taken care
    val isLoading: Boolean = false, // Whether the list of tasks is being loaded or not
    val errorMessage: String? = null, // Error message if any error occurs
    val maintenanceToDeleteId: Long? = null, // When this is not null, show confirmation dialog
    val showDeleteConfirmationDialog: Boolean = false, // When this is true, show the confirmation dialog
    val snackbarMessage: String? = null // To display any informating messages to the user
)
