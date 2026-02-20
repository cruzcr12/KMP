package com.echcoding.carcaremanager.presentation.expense.expense_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddTask
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Money
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
import carcaremanager.composeapp.generated.resources.add_expense
import com.echcoding.carcaremanager.domain.model.Expense
import com.echcoding.carcaremanager.presentation.core.RoyalBlue
import com.echcoding.carcaremanager.presentation.core.mocks.getMockExpenses
import com.echcoding.carcaremanager.presentation.expense.expense_list.components.EmptyExpenseList
import com.echcoding.carcaremanager.presentation.expense.expense_list.components.ExpenseList
import com.echcoding.carcaremanager.presentation.vehicle.vehicle_selected.SelectedVehicleViewModel
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ExpenseListScreenRoot(
    viewModel: ExpenseListViewModel = koinViewModel(),
    selectedVehicleViewModel: SelectedVehicleViewModel,
    onAddExpense: () -> Unit,
    onEditExpense: (Expense) -> Unit,
    modifier: Modifier = Modifier
){
    // This automatically reacts to the Flow in the VM
    val state by viewModel.state.collectAsStateWithLifecycle()
    // Collect the active vehicle state
    val activeVehicle by selectedVehicleViewModel.selectedVehicle.collectAsStateWithLifecycle()
    // Notify the viewModel when the active vehicle changes so it can update the expense list
    LaunchedEffect(activeVehicle){
        viewModel.onAction(ExpenseListAction.OnActiveVehicleChanged(activeVehicle))
    }

    val snackBarHostState = remember { SnackbarHostState() }
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
    ) {
        ExpenseListScreen(
            state = state,
            onAction = { action ->
                when(action){
                    is ExpenseListAction.OnAddExpenseClick -> onAddExpense()
                    is ExpenseListAction.OnEditExpenseClick -> onEditExpense(action.expense)
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
fun ExpenseListScreen(
    state: ExpenseListState,
    onAction: (ExpenseListAction) -> Unit,
    modifier: Modifier = Modifier
) {
    // Creates a LazyListState to be used in the list of expenses
    val expenseListState = rememberLazyListState()

    // Adds an effect to scroll the list to the top when the maintenance list changes
    LaunchedEffect(state.expenses.size){
        if(state.expenses.isNotEmpty()) {
            expenseListState.animateScrollToItem(0)
        }
    }

    Scaffold(
        floatingActionButton = {
            if(state.selectedVehicle != null) {
                FloatingActionButton(
                    onClick = { onAction(ExpenseListAction.OnAddExpenseClick) },
                    containerColor = RoyalBlue,
                    contentColor = Color.White,
                    shape = CircleShape
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = stringResource(Res.string.add_expense)
                    )
                }
            }
        }
    ){ padding ->
        if(state.isLoading){
            CircularProgressIndicator()
        }else{
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
                state.expenses.isEmpty() -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ){
                        EmptyExpenseList(modifier = Modifier.weight(2f))
                    }

                }
                else -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top
                    ){
                        ExpenseList(
                            expenses = state.expenses,
                            onEditExpenseClick = { onAction(ExpenseListAction.OnEditExpenseClick(it)) },
                            padding = padding,
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxSize(),
                            scrollState = expenseListState
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ExpenseScreenPreview() {
    ExpenseListScreen(
        state = ExpenseListState(
            expenses = getMockExpenses()
        ),
        onAction = {}
    )
}


