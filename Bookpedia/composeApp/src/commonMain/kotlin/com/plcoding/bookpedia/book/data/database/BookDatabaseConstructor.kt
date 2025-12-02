package com.plcoding.bookpedia.book.data.database

import androidx.room.RoomDatabaseConstructor

/**
 * This class is used to initialize the database. This is unique for KMP
 */

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object BookDatabaseConstructor: RoomDatabaseConstructor<FavoriteBookDatabase> {
    override fun initialize(): FavoriteBookDatabase
}