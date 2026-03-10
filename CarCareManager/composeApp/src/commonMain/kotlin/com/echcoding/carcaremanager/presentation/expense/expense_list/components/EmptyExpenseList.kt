package com.echcoding.carcaremanager.presentation.expense.expense_list.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import carcaremanager.composeapp.generated.resources.Res
import carcaremanager.composeapp.generated.resources.add_first_expense
import carcaremanager.composeapp.generated.resources.no_expenses_found
import com.echcoding.carcaremanager.themes.customapp.CustomAppTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun EmptyExpenseList(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            Icons.Default.History,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.tertiaryContainer
        )
        Spacer(Modifier.height(16.dp))
        Text(
            stringResource(Res.string.no_expenses_found),
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            stringResource(Res.string.add_first_expense),
            color = MaterialTheme.colorScheme.tertiaryContainer,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EmptyExpenseListPreview() {
    CustomAppTheme {
        EmptyExpenseList()
    }
}