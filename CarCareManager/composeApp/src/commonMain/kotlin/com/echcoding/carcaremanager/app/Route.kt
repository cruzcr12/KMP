package com.echcoding.carcaremanager.app

import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable
    data object CarAppGraph : Route

    @Serializable
    data object VehicleGraph : Route
    
    @Serializable
    data object VehicleList : Route

    @Serializable
    data object AddVehicle : Route

}
