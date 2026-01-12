package com.echcoding.carcaremanager.presentation.vehicle.vehicle_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import carcaremanager.composeapp.generated.resources.Res
import carcaremanager.composeapp.generated.resources.cancel
import carcaremanager.composeapp.generated.resources.delete
import carcaremanager.composeapp.generated.resources.delete_vehicle
import carcaremanager.composeapp.generated.resources.delete_vehicle_confirmation
import carcaremanager.composeapp.generated.resources.history
import carcaremanager.composeapp.generated.resources.services
import carcaremanager.composeapp.generated.resources.vehicles
import com.echcoding.carcaremanager.domain.model.Vehicle
import com.echcoding.carcaremanager.presentation.core.AthensGray
import com.echcoding.carcaremanager.presentation.core.vehicles
import com.echcoding.carcaremanager.presentation.core.components.TabMenuItem
import com.echcoding.carcaremanager.presentation.core.components.TitleBarHeader
import com.echcoding.carcaremanager.presentation.vehicle.vehicle_selected.components.VehicleSelector
import com.echcoding.carcaremanager.presentation.vehicle.vehicle_list.components.AddNewVehicleButton
import com.echcoding.carcaremanager.presentation.vehicle.vehicle_list.components.EmptyVehicleList
import com.echcoding.carcaremanager.presentation.vehicle.vehicle_list.components.VehicleList
import com.echcoding.carcaremanager.presentation.vehicle.vehicle_selected.SelectedVehicleViewModel
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun VehicleListScreenRoot(
    viewModel: VehicleListViewModel = koinViewModel(),
    selectedVehicleViewModel: SelectedVehicleViewModel = koinViewModel(),
    onAddVehicle: () -> Unit,
    onEditVehicle: (Vehicle) -> Unit
){
    // This automatically reacts to the Flow in the VM
    val state by viewModel.state.collectAsStateWithLifecycle()
    // Collect the active vehicle state
    val activeVehicle by selectedVehicleViewModel.selectedVehicle.collectAsStateWithLifecycle()

    val snackbarHostState  = remember { SnackbarHostState() }

    // Listen for the Delete Success side effect
    LaunchedEffect(true){
        viewModel.events.collect { event ->
            when(event){
                is VehicleListSideEffect.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = event.message,
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }
    }

    Scaffold(
        // Attach the host to the scaffold
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ){
        VehicleListScreen(
            state = state,
            activeVehicle = activeVehicle,
            onAction = { action ->
                when(action){
                    is VehicleListAction.OnAddVehicleClick -> onAddVehicle()
                    is VehicleListAction.OnEditVehicle -> onEditVehicle(action.vehicle)
                    else -> Unit
                }
                // Forward action to ViewModel
                viewModel.onAction(action)
            }
        )
    }

}

@Composable
private fun VehicleListScreen(
    state: VehicleListState,
    activeVehicle: Vehicle?,
    onAction: (VehicleListAction) -> Unit,
    modifier: Modifier = Modifier
){
    val pagerState = rememberPagerState { 3 }
    // Creates a LazyListState to be used in the list of vehicles
    val vehiclesListState = rememberLazyListState()

    // Adds an effect to scroll the list to the top when the vehicle list changes
    LaunchedEffect(state.vehicles.size){
        if(state.vehicles.isNotEmpty()) {
            vehiclesListState.animateScrollToItem(0)
        }
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
                        selected = state.selectedTabIndex == 1,
                        onClick = { onAction(VehicleListAction.OnTabSelected(1)) },
                        modifier = Modifier.weight(1f)
                    ){
                        TabMenuItem(stringResource(Res.string.history), Icons.Default.History, state.selectedTabIndex == 1)
                    }
                    // Vehicles Tab
                    Tab(
                        selected = state.selectedTabIndex == 2,
                        onClick = { onAction(VehicleListAction.OnTabSelected(2)) },
                        modifier = Modifier.weight(1f)
                    ){
                        TabMenuItem(stringResource(Res.string.vehicles), Icons.Default.DirectionsCar, state.selectedTabIndex == 2)
                    }
                }

                HorizontalDivider(color = AthensGray, thickness = 1.dp)

                // Active Vehicle Selector (Dropdown style)
                VehicleSelector(
                    vehicle = activeVehicle,
                    onClick = {
                        onAction(VehicleListAction.OnTabSelected(2))
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
            ){ pageIndex ->
                when(pageIndex){
                    0 -> {
                        // Services
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            Text(
                                text = stringResource(Res.string.services),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.headlineSmall,
                            )
                        }

                    }
                    1 -> {
                        // History
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            Text(
                                text = stringResource(Res.string.history),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.headlineSmall,
                            )
                        }
                    }
                    2 -> {
                        // Vehicles
                        if(state.isLoading){
                            CircularProgressIndicator()
                        } else{
                            when {
                                state.errorMessage != null -> {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center,
                                        modifier = Modifier
                                            .fillMaxSize()
                                    ) {
                                        Text(
                                            text = state.errorMessage,
                                            textAlign = TextAlign.Center,
                                            style = MaterialTheme.typography.headlineSmall,
                                            color = MaterialTheme.colorScheme.error
                                        )
                                    }
                                }
                                state.vehicles.isEmpty() -> {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(20.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ){
                                        EmptyVehicleList(modifier = Modifier.weight(2f))

                                        AddNewVehicleButton(
                                            onClick = { onAction(VehicleListAction.OnAddVehicleClick) },
                                            modifier = Modifier.weight(1f)
                                        )
                                        Spacer(modifier = Modifier.height(20.dp))
                                    }
                                }
                                else -> {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Top
                                    ) {
                                        VehicleList(
                                            vehicles = state.vehicles,
                                            onAddVehicleClick = { onAction(VehicleListAction.OnAddVehicleClick) },
                                            onEditVehicleClick = { onAction(VehicleListAction.OnEditVehicle(it))},
                                            onDeleteVehicleClick = { onAction(VehicleListAction.OnDeleteVehicle(it?:-1))},
                                            onSelectVehicleClick = { onAction(VehicleListAction.OnSelectVehicleClick(it?:-1))},
                                            padding = innerPadding,
                                            modifier = modifier
                                                .fillMaxWidth()
                                                .fillMaxHeight()
                                            ,
                                            scrollState = vehiclesListState
                                        )
                                    }
                                    // Confirmation dialog to delete the vehicle
                                    if(state.showDeleteConfirmationDialog){
                                        AlertDialog(
                                            onDismissRequest = { onAction(VehicleListAction.OnDismissDeleteDialog) },
                                            title = { Text(text = stringResource(Res.string.delete_vehicle)) },
                                            text = { Text(text = stringResource(Res.string.delete_vehicle_confirmation)) },
                                            confirmButton = {
                                                TextButton(onClick = { onAction(VehicleListAction.OnConfirmDeleteVehicle) }) {
                                                    Text(text = stringResource(Res.string.delete),
                                                        color = MaterialTheme.colorScheme.error)
                                                }
                                            },
                                            dismissButton = {
                                                TextButton(onClick = { onAction(VehicleListAction.OnDismissDeleteDialog) }) {
                                                    Text(stringResource(Res.string.cancel))
                                                }
                                            }
                                        )
                                    }

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
            vehicles = vehicles,
            isLoading = false,
            errorMessage = null,
            selectedTabIndex = 2,
        ),
        activeVehicle = vehicles.firstOrNull(),
        onAction = {},
    )
}