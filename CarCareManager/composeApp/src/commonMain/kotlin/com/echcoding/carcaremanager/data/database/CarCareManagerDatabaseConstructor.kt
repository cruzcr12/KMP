package com.echcoding.carcaremanager.data.database

import androidx.room.RoomDatabaseConstructor

/**
 * This class is used to initialize the database. This is unique for KMP
 */

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object CarCareManagerDatabaseConstructor: RoomDatabaseConstructor<CarCareManagerDatabase>{
    override fun initialize(): CarCareManagerDatabase
}