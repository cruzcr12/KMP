package com.echcoding.carcaremanager.data.mappers

import com.echcoding.carcaremanager.data.database.entity.ExpenseEntity
import com.echcoding.carcaremanager.data.database.model.ExpenseWithMaintenance
import com.echcoding.carcaremanager.domain.model.Expense
import com.echcoding.carcaremanager.domain.model.TypeOfService
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
fun ExpenseWithMaintenance.toExpense(): Expense {
    return Expense(
        id = expense.id,
        vehicleId = expense.vehicleId,
        maintenanceId = expense.maintenanceId,
        maintenanceName = maintenanceName,
        date = Instant.fromEpochMilliseconds(expense.date)
            .toLocalDateTime(TimeZone.UTC).date,
        mileage = expense.mileage,
        mileageUnit = expense.mileageUnit,
        amount = expense.amount,
        typeOfService = TypeOfService.valueOf(expense.typeOfService),
        note = expense.note
    )
}

@OptIn(ExperimentalTime::class)
fun Expense.toExpenseEntity(): ExpenseEntity {
    return ExpenseEntity(
        id = id ?: 0,
        vehicleId = vehicleId,
        maintenanceId = maintenanceId,
        // Convert LocalDate to Long (Millis) for storage
        date = date.atStartOfDayIn(TimeZone.currentSystemDefault()).toEpochMilliseconds(),
        mileage = mileage,
        mileageUnit = mileageUnit,
        amount = amount,
        typeOfService = typeOfService.name,
        note = note
    )
}



