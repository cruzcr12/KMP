package com.echcoding.carcaremanager.presentation.expense.expense_list.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.echcoding.carcaremanager.domain.model.Expense
import com.echcoding.carcaremanager.domain.model.TypeOfService
import com.echcoding.carcaremanager.presentation.core.extensions.formattedMileage
import com.echcoding.carcaremanager.presentation.core.mocks.getMockExpenses
import com.echcoding.carcaremanager.presentation.core.utils.CurrencyFormatter
import com.echcoding.carcaremanager.themes.customapp.CustomAppTheme
import com.echcoding.carcaremanager.themes.customapp.informativeLight
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ExpenseListItem(
    expense: Expense,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier.fillMaxWidth().height(IntrinsicSize.Max)) {
        // Time line indicator
        Column(horizontalAlignment = Alignment.CenterHorizontally
            ) {
            /*
            Box(
                modifier = Modifier.padding(4.dp),
                contentAlignment = Alignment.Center
            ) {
                */
                Box(
                    modifier = Modifier.size(28.dp)
                        .clip(CircleShape)
                        .background(if (expense.typeOfService == TypeOfService.MAINTENANCE) MaterialTheme.colorScheme.secondary else informativeLight ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (expense.typeOfService == TypeOfService.MAINTENANCE) Icons.Default.Build else Icons.Default.FlashOn,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                }
                // Vertical Line
                Box(modifier = Modifier.width(2.dp)
                    .height(132.dp)
                    .background(MaterialTheme.colorScheme.outlineVariant))
            //}
        }
        Spacer(modifier = Modifier.width(16.dp))
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
                .padding(bottom = 8.dp),
            onClick = onEditClick
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth())
                {
                    Text(text = expense.date.format(LocalDate.Formats.ISO),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.tertiary,
                        fontWeight = FontWeight.Bold)
                    Text(text = CurrencyFormatter().formatCurrency(expense.amount ?: 0.0), fontWeight = FontWeight.Bold)
                }
                Text(text = expense.maintenanceName ?: "", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Row(verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 8.dp)) {
                    Surface(color = MaterialTheme.colorScheme.tertiaryContainer, shape = RoundedCornerShape(4.dp)) {
                        Text(expense.formattedMileage,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            color = MaterialTheme.colorScheme.onTertiaryContainer,
                            style = MaterialTheme.typography.labelSmall)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(color = MaterialTheme.colorScheme.onTertiary,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary),
                        shape = RoundedCornerShape(4.dp)) {
                        Text(expense.typeOfService.toString(),
                            color = MaterialTheme.colorScheme.tertiary,
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                    }
                }
                Text(text = expense.note ?: "",
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2,
                    modifier = Modifier.defaultMinSize(minHeight = 36.dp)
                )
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun ExpenseListItemPreview(){
    CustomAppTheme {
        ExpenseListItem(
            expense = getMockExpenses()[1],
            onEditClick = {}
        )
    }
}