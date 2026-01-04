package com.echcoding.carcaremanager.app

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.echcoding.carcaremanager.presentation.core.vehicles
import com.echcoding.carcaremanager.presentation.vehicle.vehicle_detail.VehicleDetailAction
import com.echcoding.carcaremanager.presentation.vehicle.vehicle_detail.VehicleDetailScreenRoot
import com.echcoding.carcaremanager.presentation.vehicle.vehicle_detail.VehicleDetailState
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
                // Define the route for Vehicle List screen
                composable<Route.VehicleList>(
                    exitTransition = { slideOutHorizontally() + fadeOut() },
                    popEnterTransition = { slideInHorizontally() + fadeIn() }
                ) {
                    // Fetches a VM scoped to this screen. When you leave the screen, the VM is cleared
                    val viewModel = koinViewModel<VehicleListViewModel>()
                    // Fetches a VM scoped to the parent graph. This allows the data to persist across screens
                    val selectedVehicleViewModel = it.sharedKoinViewModel<SelectedVehicleViewModel>(navController)

                    VehicleListScreenRoot(
                        viewModel = viewModel,
                        onAddVehicle = {
                            navController.navigate(
                                Route.VehicleDetails(vehicleId = -1)
                            )
                        },
                        onSelectVehicle = { vehicle ->
                            selectedVehicleViewModel.onSelectVehicle(vehicle)
                            navController.navigate(
                                Route.VehicleDetails(vehicleId = vehicle.id)
                            )
                        }
                    )
                }

                // Vehicle Details screen route
                composable<Route.VehicleDetails>(
                    enterTransition = { slideInHorizontally{ initialOffset ->
                        initialOffset } },
                    exitTransition = { slideOutHorizontally { initialOffset ->
                        initialOffset } }
                ){
                    // Fetches a VM scoped to this screen. When you leave the screen, the VM is cleared
                    val viewModel = koinViewModel<VehicleDetailViewModel>()
                    // Retrieves the exact same instance of SelectedVehicleViewModel that the list screen used
                    val selectedVehicleViewModel = it.sharedKoinViewModel<SelectedVehicleViewModel>(navController)
                    val selectedVehicle by selectedVehicleViewModel.selectedVehicle.collectAsStateWithLifecycle()

                    LaunchedEffect(selectedVehicle){
                        selectedVehicle?.let {
                            viewModel.onAction(VehicleDetailAction.OnSelectedVehicleChange(it))
                        }
                    }

                    VehicleDetailScreenRoot(
                        viewModel = viewModel,
                        onBackClick = {
                            navController.popBackStack()
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