package com.echcoding.carcaremanager.presentation.expense.expense_list.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.echcoding.carcaremanager.domain.model.Expense
import com.echcoding.carcaremanager.presentation.core.mocks.getMockExpenses
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ExpenseList(
    expenses: List<Expense>,
    onEditExpenseClick: (Expense) -> Unit,
    padding: PaddingValues = PaddingValues(0.dp),
    modifier: Modifier = Modifier,
    scrollState: LazyListState = rememberLazyListState()
){
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(padding),
        state = scrollState,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        items(
            items = expenses,
            key = { it.id ?: -1 }
        ){expense ->
            ExpenseListItem(
                expense = expense,
                onEditClick = { onEditExpenseClick(expense) },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ExpenseListPreview(){
    ExpenseList(
        expenses = getMockExpenses(),
        onEditExpenseClick = {},
    )
}