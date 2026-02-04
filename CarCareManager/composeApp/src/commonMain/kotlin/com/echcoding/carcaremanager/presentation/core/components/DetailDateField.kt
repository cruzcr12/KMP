package com.echcoding.carcaremanager.presentation.core.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.datetime.LocalDate

@Composable
fun DetailDateField(
    label: String,
    value: String?,
    type: DetailDateFieldType,
    onSelectedDate: (LocalDate?) -> Unit,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
    modifier: Modifier = Modifier
) {

    Column(modifier = modifier)
    {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = Color(0xFF6B7280),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        if(type == DetailDateFieldType.DOCKED) {
            DatePickerDocked(
                value = value,
                onValueChange = onValueChange,
            )
        }else{
            DatePickerFieldToModal(
                value = value,
                onSelectedDate = onSelectedDate,
                placeholder = placeholder
            )
        }

    }
}

enum class DetailDateFieldType {
    DOCKED, MODAL
}

