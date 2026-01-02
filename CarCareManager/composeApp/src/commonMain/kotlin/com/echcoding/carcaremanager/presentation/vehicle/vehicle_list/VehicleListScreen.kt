package com.echcoding.carcaremanager.presentation.vehicle.vehicle_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabIndicatorScope
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import carcaremanager.composeapp.generated.resources.Res
import carcaremanager.composeapp.generated.resources.history
import carcaremanager.composeapp.generated.resources.no_vehicles_registered
import carcaremanager.composeapp.generated.resources.services
import carcaremanager.composeapp.generated.resources.vehicles
import com.echcoding.carcaremanager.domain.model.Vehicle
import com.echcoding.carcaremanager.presentation.core.AthensGray
import com.echcoding.carcaremanager.presentation.core.vehicles
import com.echcoding.carcaremanager.presentation.core.components.TabMenuItem
import com.echcoding.carcaremanager.presentation.core.components.TitleBarHeader
import com.echcoding.carcaremanager.presentation.core.components.VehicleSelector
import com.echcoding.carcaremanager.presentation.vehicle.vehicle_list.components.AddNewVehicleButton
import com.echcoding.carcaremanager.presentation.vehicle.vehicle_list.components.EmptyVehicleState
import com.echcoding.carcaremanager.presentation.vehicle.vehicle_list.components.VehicleList
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun VehicleListScreenRoot(
    viewModel: VehicleListViewModel = koinViewModel(),
    onAddVehicle: () -> Unit,
    onSelectVehicle: () -> Unit
){
    val state by viewModel.state.collectAsStateWithLifecycle()

    VehicleListScreen(
        state = state,
        onAction = { action ->
            when(action){
                is VehicleListAction.OnAddVehicleClick -> onAddVehicle()
                is VehicleListAction.OnSelectVehicleClick -> onSelectVehicle()
                else -> Unit
            }
            // Forward action to ViewModel
            viewModel.onAction(action)
        }
    )
}

@Composable
private fun VehicleListScreen(
    state: VehicleListState,
    onAction: (VehicleListAction) -> Unit,
    modifier: Modifier = Modifier
){
    val pagerState = rememberPagerState { 3 }
    // Creates a LazyListState to be used in the list of vehicles
    val vehiclesListState = rememberLazyListState()

    // Adds an effect to scroll the list to the top when the vehicle list changes
    LaunchedEffect(state.vehicles){
        vehiclesListState.animateScrollToItem(0)
    }
    // Adds an effect when the selected tab index changes
    LaunchedEffect(state.selectedTabIndex){
        pagerState.animateScrollToPage(state.selectedTabIndex)
    }
    // Adds an effect when the current page has changed
    LaunchedEffect(pagerState.currentPage){
        onAction(VehicleListAction.OnTabSelected(pagerState.currentPage))
    }

    Scaffold(
        topBar = {
            Column(modifier = Modifier.background(Color.White)) {
                // Header
                TitleBarHeader()

                // Menu Tabs
                PrimaryTabRow(
                    selectedTabIndex = state.selectedTabIndex,
                    indicator = {
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(state.selectedTabIndex)
                        )
                    },
                    containerColor = Color.White,
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 18.dp)

                ){
                    // Services Tab
                    Tab(
                        selected = state.selectedTabIndex == 0,
                        onClick = { onAction(VehicleListAction.OnTabSelected(0)) },
                        modifier = Modifier.weight(1f)
                    ){
                        TabMenuItem(stringResource(Res.string.services), Icons.Default.Build, state.selectedTabIndex == 0)
                    }
                    // History Tab
                    Tab(
                        selected = state.selectedTabIndex == 0,
                        onClick = { onAction(VehicleListAction.OnTabSelected(1)) },
                        modifier = Modifier.weight(1f)
                    ){
                        TabMenuItem(stringResource(Res.string.history), Icons.Default.History, state.selectedTabIndex == 1)
                    }
                    // Vehicles Tab
                    Tab(
                        selected = state.selectedTabIndex == 0,
                        onClick = { onAction(VehicleListAction.OnTabSelected(2)) },
                        modifier = Modifier.weight(1f)
                    ){
                        TabMenuItem(stringResource(Res.string.vehicles), Icons.Default.DirectionsCar, state.selectedTabIndex == 2)
                    }
                }


/*
                // Tabs
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    TabMenuItem(stringResource(Res.string.services), Icons.Default.Build, state.selectedTabIndex == 0)
                    TabMenuItem(stringResource(Res.string.history), Icons.Default.History, state.selectedTabIndex == 1)
                    TabMenuItem(stringResource(Res.string.vehicles), Icons.Default.DirectionsCar, state.selectedTabIndex == 2)
                }
*/

                HorizontalDivider(color = AthensGray, thickness = 1.dp)

                // Active Vehicle Selector (Dropdown style)
                VehicleSelector(
                    vehicle = state.selectedVehicle,
                    onClick = {
                        //onAction(VehicleListAction.OnSelectVehicleClick(it) ),
                    },
                    modifier = modifier
                )
            }
        },
        containerColor = AthensGray
    ){ innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                //.padding(innerPadding)
        )
        {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ){ pageIndex ->
                when(pageIndex){
                    0 -> {
                        // Services
                        Text(
                            text = stringResource(Res.string.services),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.headlineSmall,
                        )
                    }
                    1 -> {
                        // History
                        Text(
                            text = stringResource(Res.string.history),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.headlineSmall,
                        )
                    }
                    2 -> {
                        // Vehicles
                        if(state.isLoading){
                            CircularProgressIndicator()
                        } else{
                            when {
                                state.errorMessage != null -> {
                                    Text(
                                        text = state.errorMessage.asString(),
                                        textAlign = TextAlign.Center,
                                        style = MaterialTheme.typography.headlineSmall,
                                        color = MaterialTheme.colorScheme.error
                                    )
                                }
                                state.vehicles.isEmpty() -> {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(20.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ){
                                        EmptyVehicleState(modifier = Modifier.weight(2f))

                                        AddNewVehicleButton(
                                            onClick = { onAction(VehicleListAction.OnAddVehicleClick) },
                                            modifier = Modifier.weight(1f)
                                        )
                                        Spacer(modifier = Modifier.height(20.dp))
                                    }

/*
                                    Text(
                                        text = stringResource(Res.string.no_vehicles_registered),
                                        textAlign = TextAlign.Center,
                                        style = MaterialTheme.typography.headlineSmall,
                                        color = MaterialTheme.colorScheme.error
                                    ) */
                                }
                                else -> {
                                    VehicleList(
                                        vehicles = state.vehicles,
                                        onAddVehicle = { onAction(VehicleListAction.OnAddVehicleClick) },
                                        onVehicleClick = { },
                                        padding = innerPadding,
                                        modifier = modifier,
                                        scrollState = vehiclesListState
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                    AddNewVehicleButton(onClick = { onAction(VehicleListAction.OnAddVehicleClick) })
                                    Spacer(modifier = Modifier.height(10.dp))
                                }
                            }
                        }
                    }
                }

            }





        }

    }
}

@Preview
@Composable
fun VehicleListScreenPreview(){
    VehicleListScreen(
        state = VehicleListState(
            selectedTabIndex = 2,
        ),
        onAction = {},
    )
}