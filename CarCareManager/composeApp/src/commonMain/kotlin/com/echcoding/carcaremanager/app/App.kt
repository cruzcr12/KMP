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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.echcoding.carcaremanager.presentation.vehicle.vehicle_detail.VehicleDetailScreenRoot
import com.echcoding.carcaremanager.presentation.vehicle.vehicle_detail.VehicleDetailState
import com.echcoding.carcaremanager.presentation.vehicle.vehicle_list.VehicleListScreenRoot
import com.echcoding.carcaremanager.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    AppTheme {
        // Create the controller to handle the app navigation
        val navController = rememberNavController()
        // The NavHost is a container that displays the current screen based on the navigation state
        NavHost(
            navController = navController,
            startDestination = Route.VehicleGraph
        ) {
            // Group related screens in a navigation graph
            // This is important for shared view models between the screens
            navigation<Route.VehicleGraph>(
                startDestination = Route.VehicleList
            ) {
                composable<Route.VehicleList>(
                    exitTransition = { slideOutHorizontally() + fadeOut() },
                    popEnterTransition = { slideInHorizontally() + fadeIn() }
                ) {
                    VehicleListScreenRoot(
                        onAddVehicle = {
                            navController.navigate(
                                Route.AddVehicle
                            )
                        }
                    )
                }
                composable<Route.AddVehicle>(
                    enterTransition = { slideInHorizontally{ initialOffset ->
                        initialOffset } },
                    exitTransition = { slideOutHorizontally { initialOffset ->
                        initialOffset } }
                ){
                    VehicleDetailScreenRoot(
                        onBackClick = {
                            navController.popBackStack()
                        }
                    )
                }
            }
        }
    }
}
