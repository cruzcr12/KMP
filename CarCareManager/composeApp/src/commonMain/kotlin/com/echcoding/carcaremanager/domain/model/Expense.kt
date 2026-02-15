package com.echcoding.carcaremanager.domain.model

import kotlinx.datetime.LocalDate

data class Expense(
    val id: Long?,
    val vehicleId: Int,
    val maintenanceName: String,
    val date: LocalDate,
    val mileage: Int,
    val mileageUnit: String,
    val amount: Double?,
    val typeOfService: TypeOfService,
    val note: String?
)

enum class TypeOfService { MAINTENANCE, REPAIR, OTHER }