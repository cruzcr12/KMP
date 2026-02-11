package com.echcoding.carcaremanager.presentation.maintenance.maintenance_detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddTask
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ModeComment
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import carcaremanager.composeapp.generated.resources.Res
import carcaremanager.composeapp.generated.resources.cancel
import carcaremanager.composeapp.generated.resources.date_interval
import carcaremanager.composeapp.generated.resources.date_interval_placeholder
import carcaremanager.composeapp.generated.resources.delete
import carcaremanager.composeapp.generated.resources.delete_maintenance
import carcaremanager.composeapp.generated.resources.delete_maintenance_confirmation
import carcaremanager.composeapp.generated.resources.delete_task
import carcaremanager.composeapp.generated.resources.edit_task
import carcaremanager.composeapp.generated.resources.initial_date
import carcaremanager.composeapp.generated.resources.initial_odometer
import carcaremanager.composeapp.generated.resources.maintenance_control_type
import carcaremanager.composeapp.generated.resources.maintenance_intervals
import carcaremanager.composeapp.generated.resources.mileage_interval
import carcaremanager.composeapp.generated.resources.new_task
import carcaremanager.composeapp.generated.resources.odometer_interval_placeholder
import carcaremanager.composeapp.generated.resources.save_task
import carcaremanager.composeapp.generated.resources.select_date
import carcaremanager.composeapp.generated.resources.task_description
import carcaremanager.composeapp.generated.resources.task_description_placeholder
import carcaremanager.composeapp.generated.resources.task_name
import carcaremanager.composeapp.generated.resources.task_name_placeholder
import com.echcoding.carcaremanager.domain.model.ControlType
import com.echcoding.carcaremanager.presentation.core.AthensGray
import com.echcoding.carcaremanager.presentation.core.components.DetailBottomBar
import com.echcoding.carcaremanager.presentation.core.components.DetailDateField
import com.echcoding.carcaremanager.presentation.core.components.DetailDateFieldType
import com.echcoding.carcaremanager.presentation.core.components.DetailTopBar
import com.echcoding.carcaremanager.presentation.core.mocks.getMockMaintenances
import com.echcoding.carcaremanager.presentation.core.components.DetailField
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun MaintenanceDetailScreenRoot(
    viewModel: MaintenanceDetailViewModel,
    onBackClick: () -> Unit
){
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState  = remember { SnackbarHostState() }
    // Observe the side effects independently of the state. Handles the onBackClick
    LaunchedEffect(Unit){
        viewModel.effects.collect { effect ->
            when(effect){
                is MaintenanceDetailSideEffect.NavigateBack -> onBackClick()
                is MaintenanceDetailSideEffect.ShowSnackBar -> {
                    snackbarHostState.showSnackbar(
                        message = effect.message,
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }
    }

    MaintenanceDetailScreen(
        state = state,
        onAction = { action ->
            when(action){
                is MaintenanceDetailAction.OnBackClick -> onBackClick()
                else -> Unit
            }
            viewModel.onAction(action)
        },
        modifier = Modifier.fillMaxSize()
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MaintenanceDetailScreen(
    state: MaintenanceDetailState,
    onAction: (MaintenanceDetailAction) -> Unit,
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

    // Observe the error message from the state and displays the message
    LaunchedEffect(state.errorMessage){
        state.errorMessage?.let { error ->
            snackbarHostState.showSnackbar(
                message = error,
                withDismissAction = true
            )
        }
    }

    // Confirmation dialog to delete the vehicle
    if(state.showDeleteConfirmationDialog){
        AlertDialog(
            onDismissRequest = { onAction(MaintenanceDetailAction.OnDismissDeleteDialog) },
            title = { Text(text = stringResource(Res.string.delete_maintenance)) },
            text = { Text(text = stringResource(Res.string.delete_maintenance_confirmation)) },
            confirmButton = {
                TextButton(onClick = { onAction(MaintenanceDetailAction.OnConfirmDeleteMaintenance) }) {
                    Text(text = stringResource(Res.string.delete),
                        color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { onAction(MaintenanceDetailAction.OnDismissDeleteDialog) }) {
                    Text(stringResource(Res.string.cancel))
                }
            }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            DetailTopBar(
                title = if (state.isEditing) Res.string.edit_task else Res.string.new_task,
                onBackClick = { onAction(MaintenanceDetailAction.OnBackClick) },
                modifier = modifier,
                actions = {
                    if(state.isEditing){
                        IconButton(onClick = { onAction(MaintenanceDetailAction.OnDeleteMaintenanceClick(state.maintenance?.id)) }){
                            Icon(imageVector = Icons.Default.Delete, contentDescription = stringResource(Res.string.delete_task), tint = Color.Red)
                        }
                    }
                }
            )
        },
        containerColor = AthensGray,
        bottomBar = {
            DetailBottomBar(
                onSaveButtonClick = { onAction(MaintenanceDetailAction.OnSaveMaintenanceClick) },
                isSaving = state.isSaving,
                saveButtonText = stringResource(Res.string.save_task)
            )
        }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        )
        {
            // Task name
            DetailField(
                label = stringResource(Res.string.task_name),
                value = state.maintenance?.name ?: "",
                onValueChange = { onAction(MaintenanceDetailAction.OnStateChange(maintenance = state.maintenance?.copy(name = it)))},
                placeholder = stringResource(Res.string.task_name_placeholder),
                icon = Icons.Default.AddTask
            )

            // Description
            DetailField(
                label = stringResource(Res.string.task_description),
                value = state.maintenance?.description ?: "",
                onValueChange = { onAction(MaintenanceDetailAction.OnStateChange(maintenance = state.maintenance?.copy(description = it)))},
                placeholder = stringResource(Res.string.task_description_placeholder),
                singleLine = false,
                maxLines = 3,
                icon = Icons.Default.ModeComment
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color.LightGray)
            Text(
                text = stringResource(Res.string.maintenance_intervals),
                style = MaterialTheme.typography.titleSmall,
                color = Color.Gray
            )

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                // Initial odometer
                DetailField(
                    label = stringResource(Res.string.initial_odometer),
                    value = state.maintenance?.initialOdometer?.toString() ?: "",
                    onValueChange = { onAction(MaintenanceDetailAction.OnStateChange(maintenance = state.maintenance?.copy(initialOdometer = it.toIntOrNull() ?: 0))) },
                    placeholder = stringResource(Res.string.odometer_interval_placeholder),
                    keyboardType = KeyboardType.Number,
                    icon =  Icons.Default.Speed,
                    modifier = Modifier.weight(1.5f)
                )

                // Odometer interval
                DetailField(
                    label = stringResource(Res.string.mileage_interval),
                    value = state.maintenance?.odometerInterval?.toString() ?: "",
                    onValueChange = {
                        val value = it.filter { char -> char.isDigit() }.toIntOrNull() ?: 0
                        onAction(MaintenanceDetailAction.OnStateChange(maintenance = state.maintenance?.copy(odometerInterval = value)))
                    },
                    placeholder = stringResource(Res.string.odometer_interval_placeholder),
                    keyboardType = KeyboardType.Number,
                    icon =  Icons.Default.Speed,
                    modifier = Modifier.weight(1f)
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                // Initial date
                DetailDateField(
                    label = stringResource(Res.string.initial_date),
                    value = state.maintenance?.initialDate?.toString(),
                    onSelectedDate = { selectedDate ->
                        // The selectedDate is returned as a LocalDate by the DatePicker
                        if(selectedDate != null){
                            onAction(MaintenanceDetailAction.OnStateChange(maintenance = state.maintenance?.copy(initialDate = selectedDate)))
                        }
                    },
                    onValueChange = { },  //{ onAction(MaintenanceDetailAction.OnStateChange(maintenance = state.maintenance?.copy(initialDate = it.toLocalDate()))) },
                    type = DetailDateFieldType.MODAL,
                    placeholder = stringResource(Res.string.select_date),
                    modifier = Modifier.weight(1.5f)
                )

                // Date interval
                DetailField(
                    label = stringResource(Res.string.date_interval),
                    value = state.maintenance?.dateInterval?.toString() ?: "",
                    onValueChange = { onAction(MaintenanceDetailAction.OnStateChange(maintenance = state.maintenance?.copy(dateInterval = it.toIntOrNull() ?: 0))) },
                    placeholder = stringResource(Res.string.date_interval_placeholder),
                    keyboardType = KeyboardType.Number,
                    icon =  Icons.Default.CalendarMonth,
                    modifier = Modifier.weight(1f)
                )
            }

            // Control Type
            Column {
                Text(
                    text = stringResource(Res.string.maintenance_control_type),
                    style = MaterialTheme.typography.labelLarge,
                    color = Color(0xFF6B7280),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                    ControlType.entries.forEachIndexed { index, controlType ->
                        SegmentedButton(
                            shape = SegmentedButtonDefaults.itemShape(index = index, count = ControlType.entries.size),
                            onClick = { onAction(MaintenanceDetailAction.OnStateChange(maintenance = state.maintenance?.copy(controlType = controlType))) },
                            selected = state.maintenance?.controlType == controlType,
                            colors = SegmentedButtonDefaults.colors(
                                activeContainerColor = Color(0xFF2563EB),
                                activeContentColor = Color.White,
                                inactiveContainerColor = Color.White,
                                activeBorderColor = Color(0xFFE5E7EB),
                                inactiveBorderColor = Color(0xFFE5E7EB)
                            )
                        ) {
                            Text(controlType.name.lowercase().replaceFirstChar { it.uppercase() })
                        }
                    }
                }
            }


        }

    }
}

@Preview(showBackground = true)
@Composable
fun MaintenanceDetailScreenPreview(){
    MaintenanceDetailScreen(
        state = MaintenanceDetailState(
            maintenance = getMockMaintenances()[0],
            isEditing = false,
            isSaving = false,
        ),
        onAction = {}
    )
}
