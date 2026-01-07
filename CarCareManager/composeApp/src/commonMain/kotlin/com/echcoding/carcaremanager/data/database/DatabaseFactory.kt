package com.echcoding.carcaremanager.data.database

import androidx.room.RoomDatabase

expect class DatabaseFactory {
    fun create(): RoomDatabase.Builder<CarCareManagerDatabase>
}