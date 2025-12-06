package com.plcoding.bookpedia.book.data.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [BookEntity::class],
    version = 1 // When you modify the database you need to update the version so that Room can migrate the data to the new schema
)
@TypeConverters( //Specify the type converters that you want to use in the database
    StringListTypeConverter::class
)
@ConstructedBy(BookDatabaseConstructor::class)
abstract class FavoriteBookDatabase: RoomDatabase() {
    abstract val favoriteBookDao: FavoriteBookDao

    companion object{
        const val DB_NAME = "books_db"

    }
}