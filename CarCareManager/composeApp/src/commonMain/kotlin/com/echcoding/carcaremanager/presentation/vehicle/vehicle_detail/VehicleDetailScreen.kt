package com.echcoding.carcaremanager.presentation.vehicle.vehicle_detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.GasMeter
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.Pin
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import carcaremanager.composeapp.generated.resources.Res
import carcaremanager.composeapp.generated.resources.edit_vehicle
import carcaremanager.composeapp.generated.resources.fuel_type
import carcaremanager.composeapp.generated.resources.license_plate
import carcaremanager.composeapp.generated.resources.license_plate_placeholder
import carcaremanager.composeapp.generated.resources.make
import carcaremanager.composeapp.generated.resources.make_placeholder
import carcaremanager.composeapp.generated.resources.model
import carcaremanager.composeapp.generated.resources.model_placeholder
import carcaremanager.composeapp.generated.resources.new_vehicle
import carcaremanager.composeapp.generated.resources.odometer
import carcaremanager.composeapp.generated.resources.odometer_placeholder
import carcaremanager.composeapp.generated.resources.save_vehicle
import carcaremanager.composeapp.generated.resources.vehicle_name
import carcaremanager.composeapp.generated.resources.vehicle_name_placeholder
import carcaremanager.composeapp.generated.resources.year
import carcaremanager.composeapp.generated.resources.year_placeholder
import com.echcoding.carcaremanager.domain.model.FuelType
import com.echcoding.carcaremanager.domain.model.OdometerUnit
import com.echcoding.carcaremanager.presentation.core.components.DetailBottomBar
import com.echcoding.carcaremanager.presentation.core.components.DetailField
import com.echcoding.carcaremanager.presentation.core.components.DetailTopBar
import com.echcoding.carcaremanager.themes.customapp.CustomAppTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun VehicleDetailScreenRoot(
    viewModel: VehicleDetailViewModel,
    onBackClick: () -> Unit
){
    val state by viewModel.state.collectAsStateWithLifecycle()
    // Observe the side effects independently of the state
    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when(effect){
                is VehicleDetailSideEffect.NavigateBack -> {
                    onBackClick()
                }
                else -> {}
            }
        }
    }

    VehicleDetailScreen(
        state = state,
        onAction = { action ->
            when(action){
                is VehicleDetailAction.OnBackClick -> onBackClick()
                else -> Unit
            }
            viewModel.onAction(action)
        },
        modifier = Modifier.fillMaxSize()
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun VehicleDetailScreen(
    state: VehicleDetailState,
    onAction: (VehicleDetailAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    val snackbarHostState = remember { SnackbarHostState() }


    // This will intercept the back press
    BackHandler(enabled = true) {
        // Check if a field is focused or just clear focus generally.
        // If focus is cleared, the keyboard hides, and the screen stays open.
        focusManager.clearFocus()
    }

    // Observe the error message from the state
    LaunchedEffect(state.errorMessage){
        state.errorMessage?.let { error ->
            snackbarHostState.showSnackbar(
                message = error,
                withDismissAction = true
            )
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            DetailTopBar(
                title = if (state.isEditing) Res.string.edit_vehicle else Res.string.new_vehicle,
                onBackClick = { onAction(VehicleDetailAction.OnBackClick) },
                actions = {}
            )
        },
        bottomBar = {
            DetailBottomBar(
                onSaveButtonClick = { onAction(VehicleDetailAction.OnSaveVehicleClick) },
                isSaving = state.isSaving,
                saveButtonText = stringResource(Res.string.save_vehicle)
            )
        }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DetailField(
                label = stringResource(Res.string.vehicle_name),
                value = state.vehicle?.name,
                onValueChange = { onAction(VehicleDetailAction.OnStateChange(vehicle = state.vehicle?.copy(name = it)) ) }, 
                placeholder = stringResource(Res.string.vehicle_name_placeholder),
                icon = Icons.Default.DirectionsCar
            )

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                DetailField(
                    label = stringResource(Res.string.year),
                    value = state.vehicle?.year?.toString(),
                    onValueChange = { onAction(VehicleDetailAction.OnStateChange(vehicle = state.vehicle?.copy(year = it.toIntOrNull() ?: 0))) }, 
                    placeholder = stringResource(Res.string.year_placeholder),
                    icon = Icons.Default.Numbers,
                    keyboardType = KeyboardType.Number,
                    modifier = Modifier.weight(1.2f)
                )
                DetailField(
                    label = stringResource(Res.string.license_plate),
                    value = state.vehicle?.licensePlate,
                    onValueChange = { onAction(VehicleDetailAction.OnStateChange(vehicle = state.vehicle?.copy(licensePlate = it))) },
                    placeholder = stringResource(Res.string.license_plate_placeholder),
                    icon = Icons.Default.Pin,
                    modifier = Modifier.weight(1.3f)
                )
            }

            DetailField(
                label = stringResource(Res.string.make),
                value = state.vehicle?.maker,
                onValueChange = { onAction(VehicleDetailAction.OnStateChange(vehicle = state.vehicle?.copy(maker = it))) },
                placeholder = stringResource(Res.string.make_placeholder),
                icon = Icons.Default.Settings
            )

            DetailField(
                label = stringResource(Res.string.model),
                value = state.vehicle?.model,
                onValueChange = { onAction(VehicleDetailAction.OnStateChange(vehicle = state.vehicle?.copy(model = it))) },
                placeholder = stringResource(Res.string.model_placeholder),
                icon = Icons.Default.DirectionsCar
            )

            Column {
                Text(
                    text = stringResource(Res.string.fuel_type),
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                    FuelType.entries.forEachIndexed { index, fuelType ->
                        SegmentedButton(
                            shape = SegmentedButtonDefaults.itemShape(index = index, count = FuelType.entries.size),
                            onClick = { onAction(VehicleDetailAction.OnStateChange(vehicle = state.vehicle?.copy(fuelType = fuelType))) },
                            selected = state.vehicle?.fuelType == fuelType,
                            colors = SegmentedButtonDefaults.colors(
                                activeContainerColor = MaterialTheme.colorScheme.primary,
                                activeContentColor = MaterialTheme.colorScheme.onPrimary,
                                inactiveContainerColor = MaterialTheme.colorScheme.onPrimary,
                                activeBorderColor = MaterialTheme.colorScheme.outlineVariant,
                                inactiveBorderColor = MaterialTheme.colorScheme.outlineVariant
                            )
                        ) {
                            Text(fuelType.name.lowercase().replaceFirstChar { it.uppercase() })
                        }
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom
            ){
                DetailField(
                    label = stringResource(Res.string.odometer),
                    value = state.vehicle?.odometer?.toString() ?: "",
                    onValueChange = { newValue ->
                        // Filter out non-numeric characters if necessary, or just use toIntOrNull
                        val intValue = newValue.filter { it.isDigit() }.toIntOrNull() ?: 0
                        onAction(VehicleDetailAction.OnStateChange(vehicle = state.vehicle?.copy(odometer = intValue)))
                    },
                    placeholder = stringResource(Res.string.odometer_placeholder),
                    icon = Icons.Default.GasMeter,
                    modifier = Modifier.weight(1.5f)
                )
                Spacer(modifier = Modifier.width(12.dp))
                SingleChoiceSegmentedButtonRow(
                    modifier = Modifier.width(140.dp)
                        .padding(bottom = 4.dp)
                ) {
                    OdometerUnit.entries.forEachIndexed { index, unit ->
                        SegmentedButton(
                            shape = SegmentedButtonDefaults.itemShape(index = index, count = 2),
                            onClick = {
                                onAction(VehicleDetailAction.OnStateChange(vehicle = state.vehicle?.copy(odometerUnit = unit)))
                            },
                            selected = state.vehicle?.odometerUnit == unit,
                            colors = SegmentedButtonDefaults.colors(
                                activeContainerColor = MaterialTheme.colorScheme.primary,
                                activeContentColor = MaterialTheme.colorScheme.onPrimary,
                                inactiveContainerColor = MaterialTheme.colorScheme.onPrimary,
                                activeBorderColor = MaterialTheme.colorScheme.outlineVariant,
                                inactiveBorderColor = MaterialTheme.colorScheme.outlineVariant
                            )
                        ) {
                            Text(if (unit == OdometerUnit.MILES) "mi" else "km")
                        }
                    }
                }
            }
        }
    }
}



@Preview
@Composable
fun VehicleDetailScreenPreview() {
    CustomAppTheme {
        VehicleDetailScreen(
            state = VehicleDetailState(),
            onAction = {},
        )
    }
}
