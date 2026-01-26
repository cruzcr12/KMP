package com.echcoding.carcaremanager.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import carcaremanager.composeapp.generated.resources.Res
import carcaremanager.composeapp.generated.resources.history
import carcaremanager.composeapp.generated.resources.maintenance
import carcaremanager.composeapp.generated.resources.vehicles
import com.echcoding.carcaremanager.domain.model.Vehicle
import com.echcoding.carcaremanager.presentation.core.AthensGray
import com.echcoding.carcaremanager.presentation.core.components.TitleBarHeader
import com.echcoding.carcaremanager.presentation.core.mocks.vehicles
import com.echcoding.carcaremanager.presentation.navigation.components.TabMenuItem
import com.echcoding.carcaremanager.presentation.vehicle.vehicle_selected.components.VehicleSelector
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun NavigationScreen(
    navigationViewModel: NavigationViewModel = koinViewModel(),
    activeVehicle: Vehicle?,
    maintenanceListContent: @Composable () -> Unit,
    historyListContent: @Composable () -> Unit,
    vehicleListContent: @Composable () -> Unit
    ){

    val state by navigationViewModel.state.collectAsStateWithLifecycle()
    val pagerState = rememberPagerState { 3 }

    // Sync pager with navigationViewModel
    LaunchedEffect(state.selectedTabIndex){
        pagerState.animateScrollToPage(state.selectedTabIndex)
    }
    LaunchedEffect(pagerState.currentPage) {
        navigationViewModel.onTabSelected(pagerState.currentPage)
    }

    Scaffold(
        topBar = {
            Column(modifier = Modifier.background(Color.White)){
                TitleBarHeader()

                PrimaryTabRow(
                    selectedTabIndex = state.selectedTabIndex,
                    containerColor = Color.White
                ){
                    // Maintenance Tab
                    Tab(
                        selected = state.selectedTabIndex == 0,
                        onClick = { navigationViewModel.onTabSelected(0) },
                        modifier = Modifier.height(48.dp)
                    ){
                        TabMenuItem(stringResource(Res.string.maintenance), Icons.Default.Build, state.selectedTabIndex == 0)
                    }
                    // History Tab
                    Tab(
                        selected = state.selectedTabIndex == 1,
                        onClick = { navigationViewModel.onTabSelected(1) },
                        modifier = Modifier.height(48.dp)
                    ){
                        TabMenuItem(stringResource(Res.string.history), Icons.Default.History, state.selectedTabIndex == 1)
                    }
                    // Vehicles Tab
                    Tab(
                        selected = state.selectedTabIndex == 2,
                        onClick = { navigationViewModel.onTabSelected(2) },
                        modifier = Modifier.height(48.dp)
                    ){
                        TabMenuItem(stringResource(Res.string.vehicles), Icons.Default.DirectionsCar, state.selectedTabIndex == 2)
                    }
                }
                VehicleSelector(
                    vehicle = activeVehicle,
                    onClick = { navigationViewModel.onTabSelected(2) }
                )
            }
        },
        containerColor = AthensGray
    ) { innerPadding ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.padding(innerPadding)
        ){ page ->
            when(page) {
                0 -> maintenanceListContent()
                1 -> historyListContent()
                2 -> vehicleListContent()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NavigationScreenPreview()
{
    NavigationScreen(
        navigationViewModel = koinViewModel(),
        activeVehicle = vehicles.first(),
        maintenanceListContent = {},
        historyListContent = {},
        vehicleListContent = {}
    )
}