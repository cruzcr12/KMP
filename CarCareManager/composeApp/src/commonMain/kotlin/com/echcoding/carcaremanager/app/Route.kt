package com.echcoding.carcaremanager.app

import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable
    data object CarAppGraph : Route

    @Serializable
    data object VehicleList : Route

    @Serializable
    data object AddVehicle : Route

    @Serializable
    data class VehicleDetails(val vehicleId: Int?) : Route


    @Serializable
    data object ServiceList : Route

    @Serializable
    data object AddService : Route
}
