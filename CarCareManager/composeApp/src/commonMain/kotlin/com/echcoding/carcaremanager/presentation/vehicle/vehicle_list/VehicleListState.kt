package com.echcoding.carcaremanager.presentation.vehicle.vehicle_list

import com.echcoding.carcaremanager.domain.model.Vehicle

/**
 * The UI in this project will use MVI architecture.
 * This class represents the Model or the entire state of the UI for the vehicle lis screen.
 * It bundles the state with all the actions that can be made in the View
 */
data class VehicleListState(
    val vehicles: List<Vehicle> = emptyList(),
    val isActive: Boolean = false,
    val selectedTabIndex: Int = 0,
    val isLoading: Boolean = false, // Determine if the list of vehicles is being fetched
    val errorMessage: String? = null, // To display any error when displaying the list of vehicles
    val vehicleToDeleteId: Int? = null, // When this is not null, show confirmation dialog
    val showDeleteConfirmationDialog: Boolean = false, // When this is true, show the confirmation dialog
    val snackbarMessage: String? = null // To display any informating messages

)
