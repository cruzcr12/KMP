package com.echcoding.carcaremanager.presentation.maintenance.maintenance_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
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
import carcaremanager.composeapp.generated.resources.add_maintenance_task
import com.echcoding.carcaremanager.domain.model.Maintenance
import com.echcoding.carcaremanager.presentation.core.RoyalBlue
import com.echcoding.carcaremanager.presentation.core.mocks.getMockMaintenances
import com.echcoding.carcaremanager.presentation.maintenance.maintenance_list.components.EmptyMaintenanceList
import com.echcoding.carcaremanager.presentation.maintenance.maintenance_list.components.MaintenanceList
import com.echcoding.carcaremanager.presentation.vehicle.vehicle_selected.SelectedVehicleViewModel
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MaintenanceListScreenRoot(
    viewModel: MaintenanceListViewModel = koinViewModel(),
    selectedVehicleViewModel: SelectedVehicleViewModel,
    onAddMaintenance: () -> Unit,
    onEditMaintenance: (Maintenance) -> Unit,
    modifier: Modifier = Modifier
) {
    // This automatically reacts to the Flow in the VM
    val state by viewModel.state.collectAsStateWithLifecycle()
    // Collect the active vehicle state
    val activeVehicle by selectedVehicleViewModel.selectedVehicle.collectAsStateWithLifecycle()
    // Notify the viewModel when the active vehicle changes so it can update the maintenance list
    LaunchedEffect(activeVehicle){
        viewModel.onAction(MaintenanceListAction.OnActiveVehicleChanged(activeVehicle))
    }

    val snackbarHostState  = remember { SnackbarHostState() }

    Scaffold(
        // Attach the host to the scaffold
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ){
        MaintenanceListScreen(
            state = state,
            onAction = { action ->
                when(action){
                    is MaintenanceListAction.OnAddMaintenanceClick -> onAddMaintenance()
                    is MaintenanceListAction.OnEditMaintenanceClick -> onEditMaintenance(action.maintenance)
                    else -> Unit
                }
                // Forward action to ViewModel
                viewModel.onAction(action)
            },
            modifier = modifier
        )
    }
}

@Composable
fun MaintenanceListScreen(
    state: MaintenanceListState,
    onAction: (MaintenanceListAction) -> Unit,
    modifier: Modifier = Modifier
) {
    // Creates a LazyListState to be used in the list of maintenances
    val maintenanceListState = rememberLazyListState()

    // Adds an effect to scroll the list to the top when the maintenance list changes
    LaunchedEffect(state.tasks.size){
        if(state.tasks.isNotEmpty()) {
            maintenanceListState.animateScrollToItem(0)
        }
    }

    Scaffold(
        floatingActionButton = {
            if(state.selectedVehicle != null) {
                FloatingActionButton(
                    onClick = { onAction(MaintenanceListAction.OnAddMaintenanceClick) },
                    containerColor = RoyalBlue,
                    contentColor = Color.White,
                    shape = CircleShape,

                    ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = stringResource(Res.string.add_maintenance_task)
                    )
                }
            }
        }
    ) { padding ->
        if(state.isLoading) {
            CircularProgressIndicator()
        }else {
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
                state.tasks.isEmpty() -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ){
                        EmptyMaintenanceList(modifier = Modifier.weight(2f))
                    }
                }
                else -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top
                    ){
                        MaintenanceList(
                            maintenanceTasks = state.tasks,
                            currentOdometer = state.selectedVehicle?.odometer ?: 0,
                            onEditMaintenanceClick = { onAction(MaintenanceListAction.OnEditMaintenanceClick(it)) },
                            padding = padding,
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(),
                            scrollState = maintenanceListState
                        )
                    }
                    
                }
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun MaintenanceScreenPreview() {
    MaintenanceListScreen(
        state = MaintenanceListState(
            tasks = getMockMaintenances()
        ),
        onAction = {}
    )
}