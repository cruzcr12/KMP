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
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import carcaremanager.composeapp.generated.resources.add_maintenance_task
import carcaremanager.composeapp.generated.resources.cancel
import carcaremanager.composeapp.generated.resources.delete
import carcaremanager.composeapp.generated.resources.delete_maintenance
import carcaremanager.composeapp.generated.resources.delete_maintenance_confirmation
import com.echcoding.carcaremanager.domain.model.Vehicle
import com.echcoding.carcaremanager.presentation.core.mocks.getMockMaintenances
import com.echcoding.carcaremanager.presentation.core.mocks.vehicles
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
    modifier: Modifier = Modifier
) {
    // This automatically reacts to the Flow in the VM
    val state by viewModel.state.collectAsStateWithLifecycle()
    // Collect the active vehicle state
    val activeVehicle by selectedVehicleViewModel.selectedVehicle.collectAsStateWithLifecycle()
    state.selectedVehicle = activeVehicle

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
            FloatingActionButton(
                onClick = { onAction(MaintenanceListAction.OnAddMaintenanceClick) },
                containerColor = Color(0xFF2563EB),
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Build,
                    contentDescription = stringResource(Res.string.add_maintenance_task ))
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
                            overdueTasks = state.overdueTasks ?: 0,
                            onSelectMaintenance = { onAction(MaintenanceListAction.OnSelectMaintenanceClick(it))},
                            padding = padding,
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(),
                            scrollState = maintenanceListState
                        )
                    }
                    // Confirmation dialog to delete the vehicle
                    if(state.showDeleteConfirmationDialog){
                        AlertDialog(
                            onDismissRequest = { onAction(MaintenanceListAction.OnDismissDeleteDialog) },
                            title = { Text(text = stringResource(Res.string.delete_maintenance)) },
                            text = { Text(text = stringResource(Res.string.delete_maintenance_confirmation)) },
                            confirmButton = {
                                TextButton(onClick = { onAction(MaintenanceListAction.OnConfirmDeleteMaintenance) }) {
                                    Text(text = stringResource(Res.string.delete),
                                        color = MaterialTheme.colorScheme.error)
                                }
                            },
                            dismissButton = {
                                TextButton(onClick = { onAction(MaintenanceListAction.OnDismissDeleteDialog) }) {
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