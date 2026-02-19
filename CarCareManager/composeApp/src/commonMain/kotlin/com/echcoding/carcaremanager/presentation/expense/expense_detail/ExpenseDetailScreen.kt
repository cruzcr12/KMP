package com.echcoding.carcaremanager.presentation.expense.expense_detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import carcaremanager.composeapp.generated.resources.Res
import carcaremanager.composeapp.generated.resources.cancel
import carcaremanager.composeapp.generated.resources.delete
import carcaremanager.composeapp.generated.resources.delete_expense
import carcaremanager.composeapp.generated.resources.delete_expense_confirmation
import carcaremanager.composeapp.generated.resources.edit_expense
import carcaremanager.composeapp.generated.resources.amount
import carcaremanager.composeapp.generated.resources.amount_placeholder
import carcaremanager.composeapp.generated.resources.date
import carcaremanager.composeapp.generated.resources.expense_maintenance_name
import carcaremanager.composeapp.generated.resources.mileage
import carcaremanager.composeapp.generated.resources.mileage_placeholder
import carcaremanager.composeapp.generated.resources.note
import carcaremanager.composeapp.generated.resources.note_placeholder
import carcaremanager.composeapp.generated.resources.type_of_service
import carcaremanager.composeapp.generated.resources.maintenance
import carcaremanager.composeapp.generated.resources.maintenance_performed
import carcaremanager.composeapp.generated.resources.new_expense
import carcaremanager.composeapp.generated.resources.other
import carcaremanager.composeapp.generated.resources.repairment
import carcaremanager.composeapp.generated.resources.save_expense
import carcaremanager.composeapp.generated.resources.select_date
import carcaremanager.composeapp.generated.resources.select_maintenance
import com.echcoding.carcaremanager.domain.model.TypeOfService
import com.echcoding.carcaremanager.presentation.core.AthensGray
import com.echcoding.carcaremanager.presentation.core.GrayChateau
import com.echcoding.carcaremanager.presentation.core.RiverBed
import com.echcoding.carcaremanager.presentation.core.components.DetailBottomBar
import com.echcoding.carcaremanager.presentation.core.components.DetailDateField
import com.echcoding.carcaremanager.presentation.core.components.DetailDateFieldType
import com.echcoding.carcaremanager.presentation.core.components.DetailField
import com.echcoding.carcaremanager.presentation.core.components.DetailTopBar
import com.echcoding.carcaremanager.presentation.core.mocks.getMockExpenses
import com.echcoding.carcaremanager.presentation.expense.expense_detail.components.ClickableOutlinedTextField
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ExpenseDetailScreenRoot(
    viewModel: ExpenseDetailViewModel,
    onBackClick: () -> Unit
){
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    // Observe the side effects independently of the state. Handles the onBackClick
    LaunchedEffect(Unit){
        viewModel.effects.collect { effect ->
            when(effect){
                is ExpenseDetailSideEffect.NavigateBack -> onBackClick()
                is ExpenseDetailSideEffect.ShowSnackBar -> {
                    snackbarHostState.showSnackbar(
                        message = effect.message,
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }
    }

    ExpenseDetailScreen(
        state = state,
        onAction = { action ->
            when(action){
                is ExpenseDetailAction.OnBackClick -> onBackClick()
                else -> Unit
            }
            viewModel.onAction(action)
        },
        modifier = Modifier
    )
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ExpenseDetailScreen(
    state: ExpenseDetailState,
    onAction: (ExpenseDetailAction) -> Unit,
    modifier: Modifier = Modifier
){
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

    // Confirmation dialog to delete the expense
    if(state.showDeleteConfirmationDialog){
        AlertDialog(
            onDismissRequest = { onAction(ExpenseDetailAction.OnDismissDeleteDialog) },
            title = { Text(text = stringResource(Res.string.delete_expense)) },
            text = { Text(text = stringResource(Res.string.delete_expense_confirmation)) },
            confirmButton = {
                TextButton(onClick = { onAction(ExpenseDetailAction.OnConfirmDeleteExpense) }) {
                    Text(text = stringResource(Res.string.delete),
                        color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { onAction(ExpenseDetailAction.OnDismissDeleteDialog) }) {
                    Text(stringResource(Res.string.cancel))
                }
            }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            DetailTopBar(
                title = if(state.isEditing) Res.string.edit_expense else Res.string.new_expense,
                onBackClick = { onAction(ExpenseDetailAction.OnBackClick) },
                modifier = modifier,
                actions = {
                    if(state.isEditing){
                        IconButton(onClick = { onAction(ExpenseDetailAction.OnDeleteExpenseClick(state.expense?.id)) }){
                            Icon(imageVector = Icons.Default.Delete, contentDescription = stringResource(Res.string.delete_expense), tint = Color.Red)
                        }
                    }
                }
            )
        },
        containerColor = AthensGray,
        bottomBar = {
            DetailBottomBar(
                onSaveButtonClick = { onAction(ExpenseDetailAction.OnSaveExpenseClick) },
                isSaving = state.isSaving,
                saveButtonText = stringResource(Res.string.save_expense)
            )
        }
    ){ padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        )
        {
            // Amount
            DetailField(
                label = stringResource(Res.string.amount),
                value = state.expense?.amount?.toString() ?: "",
                onValueChange = {
                    onAction(
                        ExpenseDetailAction.OnStateChange(
                            expense = state.expense?.copy(
                                amount = it.toDoubleOrNull() ?: 0.0
                            )
                        )
                    )
                },
                placeholder = stringResource(Res.string.amount_placeholder),
                keyboardType = KeyboardType.Decimal,
                icon = Icons.Default.MonetizationOn
            )

            // Date
            DetailDateField(
                label = stringResource(Res.string.date),
                value = state.expense?.date?.toString(),
                onSelectedDate = { selectedDate ->
                    // The selectedDate is returned as a LocalDate by the DatePicker
                    if(selectedDate != null){
                        onAction(ExpenseDetailAction.OnStateChange(expense = state.expense?.copy(date = selectedDate)))
                    }
                },
                onValueChange = { },
                type = DetailDateFieldType.MODAL,
                placeholder = stringResource(Res.string.select_date)
            )


            // Try to select the specific maintenance from the list of maintenances
            val selectedMaintenance = state.maintenances.find { it.id == state.expense?.maintenanceId }
            // Expense Maintenance Name will be displayed when editing, but cannot be changed
            ClickableOutlinedTextField(
                label = stringResource(Res.string.maintenance_performed),
                value = selectedMaintenance?.name ?: stringResource(Res.string.select_maintenance),
                enabled = !state.isEditing,
                readOnly = true,
                onClick = {
                    if(!state.isEditing) { onAction(ExpenseDetailAction.OnShowMaintenancePicker) }
                },
                icon = Icons.Default.ArrowDropDown
            )
            
            Box(
                contentAlignment = Alignment.BottomEnd,
            ) {
                // Mileage
                DetailField(
                    label = stringResource(Res.string.mileage),
                    value = state.expense?.mileage?.toString() ?: "",
                    onValueChange = {
                        onAction(
                            ExpenseDetailAction.OnStateChange(
                                expense = state.expense?.copy(
                                    mileage = it.toIntOrNull() ?: 0
                                )
                            )
                        )
                    },
                    placeholder = stringResource(Res.string.mileage_placeholder),
                    keyboardType = KeyboardType.Number,
                    icon = Icons.Default.Speed,
                    //modifier = Modifier.weight(0.8f)
                )

                Text(
                    text = state.expense?.mileageUnit ?: "",
                    style = MaterialTheme.typography.titleMedium,
                    color = RiverBed,
                    modifier = Modifier
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                )
            }

            // Type of Service
            Column {
                Text(
                    text = stringResource(Res.string.type_of_service),
                    style = MaterialTheme.typography.labelLarge,
                    color = Color(0xFF6B7280),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                SingleChoiceSegmentedButtonRow(modifier = Modifier.padding(0.dp)) {
                    TypeOfService.entries.forEachIndexed { index, type ->
                        SegmentedButton(
                            shape = SegmentedButtonDefaults.itemShape(
                                index = index,
                                count = TypeOfService.entries.size
                            ),
                            onClick = {
                                onAction(
                                    ExpenseDetailAction.OnStateChange(
                                        expense = state.expense?.copy(
                                            typeOfService = type
                                        )
                                    )
                                )
                            },
                            selected = state.expense?.typeOfService == type,
                            colors = SegmentedButtonDefaults.colors(
                                activeContainerColor = Color(0xFF2563EB),
                                activeContentColor = Color.White,
                                inactiveContainerColor = Color.White,
                                activeBorderColor = Color(0xFFE5E7EB),
                                inactiveBorderColor = Color(0xFFE5E7EB)
                            )
                        ) {
                            Text(
                                when (type) {
                                    TypeOfService.MAINTENANCE -> stringResource(Res.string.maintenance)
                                    TypeOfService.REPAIRMENT -> stringResource(Res.string.repairment)
                                    else -> stringResource(Res.string.other)
                                }
                            )
                        }
                    }
                }
            }

            // Notes
            DetailField(
                label = stringResource(Res.string.note),
                value = state.expense?.note ?: "",
                onValueChange = { onAction(ExpenseDetailAction.OnStateChange(expense = state.expense?.copy(note = it))) },
                placeholder = stringResource(Res.string.note_placeholder),
                icon = Icons.Default.Build,
                singleLine = false,
                minLines = 3,
                maxLines = 6,
            )

            // Bottom Sheet to display the list of maintenances
            if(state.showMaintenancePicker){
                ModalBottomSheet(
                    onDismissRequest = { onAction(ExpenseDetailAction.OnDismissMaintenancePicker) },
                    containerColor = Color.White
                ){
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 32.dp)
                    ){
                        item {
                            Text(
                                text = stringResource(Res.string.select_maintenance),
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                        items(state.maintenances){ maintenance ->
                            ListItem(
                                headlineContent = { Text(text = maintenance.name) },
                                supportingContent = { Text(text = maintenance.description ?: "") },
                                modifier = Modifier.clickable {
                                    onAction(ExpenseDetailAction.OnMaintenanceSelected(maintenance))
                                }
                            )
                        }
                    }
                }
            }

        }


    }

}

@Preview(showBackground = true)
@Composable
fun ExpenseDetailScreenPreview(){
    ExpenseDetailScreen(
        state = ExpenseDetailState(
            expense = getMockExpenses()[1],
            isEditing = true,
            isSaving = false,
        ),
        onAction = {}
    )
}