package com.echcoding.carcaremanager.presentation.vehicle.vehicle_list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import carcaremanager.composeapp.generated.resources.Res
import carcaremanager.composeapp.generated.resources.cancel
import carcaremanager.composeapp.generated.resources.delete
import carcaremanager.composeapp.generated.resources.delete_vehicle
import carcaremanager.composeapp.generated.resources.delete_vehicle_confirmation
import com.echcoding.carcaremanager.domain.model.Vehicle
import com.echcoding.carcaremanager.presentation.core.mocks.vehicles
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

    VehicleListScreen(
        state = state,
        onAction = { action ->
            when(action){
                is VehicleListAction.OnAddVehicleClick -> onAddVehicle()
                is VehicleListAction.OnEditVehicle -> onEditVehicle(action.vehicle)
                else -> Unit
            }
            viewModel.onAction(action)
        },
        innerPadding = PaddingValues(0.dp),
        modifier = Modifier
    )

}

@Composable
private fun VehicleListScreen(
    state: VehicleListState,
    onAction: (VehicleListAction) -> Unit,
    innerPadding: PaddingValues, // Padding values for the content
    modifier: Modifier = Modifier
){
    // Creates a LazyListState to be used in the list of vehicles
    val vehiclesListState = rememberLazyListState()

    // Adds an effect to scroll the list to the top when the vehicle list changes
    LaunchedEffect(state.vehicles.size){
        if(state.vehicles.isNotEmpty()) {
            vehiclesListState.animateScrollToItem(0)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(innerPadding),
        contentAlignment = Alignment.TopStart,
    ){
        when {
            state.isLoading -> {
                CircularProgressIndicator()
            }
            state.errorMessage != null -> {
                Text(
                    text = state.errorMessage,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
            state.vehicles.isEmpty() -> {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    EmptyVehicleList(modifier = Modifier.weight(2f))
                    AddNewVehicleButton(
                        onClick = { onAction(VehicleListAction.OnAddVehicleClick) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
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

@Preview(showBackground = true)
@Composable
fun VehicleListScreenPreview(){
    VehicleListScreen(
        state = VehicleListState(
            vehicles = vehicles,
            isLoading = false,
            errorMessage = null,
            selectedTabIndex = 2,
        ),
        onAction = {},
        innerPadding = PaddingValues(0.dp)
    )
}