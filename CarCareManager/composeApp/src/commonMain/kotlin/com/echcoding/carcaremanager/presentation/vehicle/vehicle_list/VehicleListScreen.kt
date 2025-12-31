package com.echcoding.carcaremanager.presentation.vehicle.vehicle_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import carcaremanager.composeapp.generated.resources.Res
import carcaremanager.composeapp.generated.resources.history
import carcaremanager.composeapp.generated.resources.services
import carcaremanager.composeapp.generated.resources.vehicles
import com.echcoding.carcaremanager.domain.model.Vehicle
import com.echcoding.carcaremanager.presentation.core.AthensGray
import com.echcoding.carcaremanager.presentation.core.vehicles
import com.echcoding.carcaremanager.presentation.navigation.TabMenuItem
import com.echcoding.carcaremanager.presentation.navigation.TitleBarHeader
import com.echcoding.carcaremanager.presentation.navigation.VehicleSelector
import com.echcoding.carcaremanager.presentation.vehicle.vehicle_list.components.VehicleList
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun VehicleListScreenRoot(
    onAddVehicle: () -> Unit
){
    //val state by viewModel.state.collectAsStateWithLifecycle()

    VehicleListScreen(
        onAddVehicle = onAddVehicle,
        onVehicleChange = {}
    )
}

@Composable
fun VehicleListScreen(
    //state: VehicleListState,
    onAddVehicle: () -> Unit,
    onVehicleChange: () -> Unit,
    modifier: Modifier = Modifier
){
    Scaffold(
        topBar = {
            Column(modifier = Modifier.background(Color.White)) {
                // Header
                TitleBarHeader()

                // Tabs
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    TabMenuItem(stringResource(Res.string.services), Icons.Default.Build, false)
                    TabMenuItem(stringResource(Res.string.history), Icons.Default.History, false)
                    TabMenuItem(stringResource(Res.string.vehicles), Icons.Default.DirectionsCar, true)
                }

                HorizontalDivider(color = AthensGray, thickness = 1.dp)

                // Active Vehicle Selector (Dropdown style)
                VehicleSelector(
                    vehicle = vehicles.first(),
                    onClick = onVehicleChange,
                    modifier = modifier
                )
            }
        },
        containerColor = AthensGray
    ){ innerPadding ->
        VehicleList(
            vehicles,
            onAddVehicle = onAddVehicle,
            onVehicleClick = { },
            padding = innerPadding,
            modifier = modifier
        )
    }
}

@Preview
@Composable
fun VehicleListScreenRootPreview(){
    VehicleListScreenRoot(
        onAddVehicle = {}
    )
}