package com.echcoding.carcaremanager.app

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.echcoding.carcaremanager.presentation.maintenance.maintenance_list.MaintenanceListScreenRoot
import com.echcoding.carcaremanager.presentation.maintenance.maintenance_list.MaintenanceListViewModel
import com.echcoding.carcaremanager.presentation.navigation.NavigationScreen
import com.echcoding.carcaremanager.presentation.navigation.NavigationViewModel
import com.echcoding.carcaremanager.presentation.vehicle.vehicle_detail.VehicleDetailScreenRoot
import com.echcoding.carcaremanager.presentation.vehicle.vehicle_detail.VehicleDetailViewModel
import com.echcoding.carcaremanager.presentation.vehicle.vehicle_list.VehicleListScreenRoot
import com.echcoding.carcaremanager.presentation.vehicle.vehicle_list.VehicleListViewModel
import com.echcoding.carcaremanager.presentation.vehicle.vehicle_selected.SelectedVehicleViewModel
import com.echcoding.carcaremanager.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App() {
    AppTheme {
        // Create the controller to handle the app navigation
        val navController = rememberNavController()
        // The NavHost is a container that displays the current screen based on the navigation state
        NavHost(
            navController = navController,
            startDestination = Route.CarAppGraph
        ) {
            // Group related screens in a navigation graph
            // This is important for shared view models between the screens
            navigation<Route.CarAppGraph>(
                startDestination = Route.VehicleList
            ) {

                // Define Vehicle List Route which also contains the Maintenance and History list routes
                composable<Route.VehicleList>(
                    exitTransition = { slideOutHorizontally() + fadeOut() },
                    popEnterTransition = { slideInHorizontally() + fadeIn() }
                ) { entry ->
                    // Shared ViewModels
                    // Using the sharedKoinViewModel to get the same instance shared with the parent graph
                    val sharedSelectedVehicleViewModel = entry.sharedKoinViewModel<SelectedVehicleViewModel>(navController)
                    val navigationViewModel = koinViewModel<NavigationViewModel>()

                    // Feature ViewModels
                    val vehicleViewModel = koinViewModel<VehicleListViewModel>()
                    val maintenanceViewModel = koinViewModel<MaintenanceListViewModel>()

                    // Get the active vehicle from the shared view model
                    val activeVehicle by sharedSelectedVehicleViewModel.selectedVehicle.collectAsStateWithLifecycle()

                    NavigationScreen(
                        navigationViewModel = navigationViewModel,
                        activeVehicle = activeVehicle,
                        maintenanceListContent = {
                            MaintenanceListScreenRoot(
                                viewModel = maintenanceViewModel,
                                selectedVehicleViewModel = sharedSelectedVehicleViewModel,
                                onAddMaintenance = { /*TODO - Add Maintenance event */ }
                            )
                        },
                        historyListContent = {
                            Text("History Screen Placeholder")
                        },
                        vehicleListContent = {
                            VehicleListScreenRoot(
                                viewModel = vehicleViewModel,
                                selectedVehicleViewModel = sharedSelectedVehicleViewModel,
                                onAddVehicle = {
                                    navController.navigate(Route.VehicleDetails(vehicleId = null))
                                },
                                onEditVehicle = { vehicle ->
                                    navController.navigate(Route.VehicleDetails(vehicleId = vehicle.id))
                                }
                            )
                        }

                    )
                }

                // Vehicle Details screen route
                composable<Route.VehicleDetails>(
                    enterTransition = { slideInHorizontally{ initialOffset -> initialOffset } },
                    exitTransition = { slideOutHorizontally { initialOffset -> initialOffset } }
                ){ it ->
                    // Fetches a VM scoped to this screen. When you leave the screen, the VM is cleared
                    val viewModel = koinViewModel<VehicleDetailViewModel>()

                    VehicleDetailScreenRoot(
                        viewModel = viewModel,
                        onBackClick = {
                            navController.navigateUp()
                        }
                    )
                }
            }
        }
    }
}


/**
 * This extension of the NavBackStackEntry returns the shared viewmodel of the parent graph
 * This is used to share data between screens in the same graph
 */
@Composable
private inline fun <reified T : ViewModel> NavBackStackEntry.sharedKoinViewModel(
    navController: NavController
): T {
    // Looks at the current screen's destination and retrieves the parent route
    val navGraphRoute = destination.parent?.route ?: return koinViewModel<T>()
    // Gets the BackStackEntry associated with that parent route
    val parentEntry = remember(this){
        navController.getBackStackEntry(navGraphRoute)
    }
    // It tells Koin to use the parent's lifecycle rather than the current screen's one
    return koinViewModel(viewModelStoreOwner = parentEntry)
}