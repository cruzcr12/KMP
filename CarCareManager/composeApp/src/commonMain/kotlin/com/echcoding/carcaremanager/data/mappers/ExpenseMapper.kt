package com.echcoding.carcaremanager.data.mappers

import com.echcoding.carcaremanager.data.database.entity.ExpenseEntity
import com.echcoding.carcaremanager.domain.model.Expense
import com.echcoding.carcaremanager.domain.model.TypeOfService
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
fun ExpenseEntity.toExpense(): Expense {
    return Expense(
        id = id,
        vehicleId = vehicleId,
        maintenanceName = maintenanceName,
        date = Instant.fromEpochMilliseconds(date)
            .toLocalDateTime(TimeZone.UTC).date,
        mileage = mileage,
        mileageUnit = mileageUnit,
        amount = amount,
        typeOfService = TypeOfService.valueOf(typeOfService),
        note = note
    )
}

@OptIn(ExperimentalTime::class)
fun Expense.toExpenseEntity(): ExpenseEntity {
    return ExpenseEntity(
        id = id ?: 0,
        vehicleId = vehicleId,
        maintenanceName = maintenanceName,
        // Convert LocalDate to Long (Millis) for storage
        date = date.atStartOfDayIn(TimeZone.currentSystemDefault()).toEpochMilliseconds(),
        mileage = mileage,
        mileageUnit = mileageUnit,
        amount = amount,
        typeOfService = typeOfService.name,
        note = note
    )
}



