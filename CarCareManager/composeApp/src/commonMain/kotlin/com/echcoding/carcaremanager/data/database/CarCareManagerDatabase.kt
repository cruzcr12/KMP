package com.echcoding.carcaremanager.data.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.echcoding.carcaremanager.data.database.dao.ExpenseDao
import com.echcoding.carcaremanager.data.database.dao.MaintenanceDao
import com.echcoding.carcaremanager.data.database.dao.VehicleDao
import com.echcoding.carcaremanager.data.database.entity.ExpenseEntity
import com.echcoding.carcaremanager.data.database.entity.MaintenanceEntity
import com.echcoding.carcaremanager.data.database.entity.VehicleEntity

@Database(
    entities = [VehicleEntity::class, MaintenanceEntity::class, ExpenseEntity::class],
    version = 1, // When you change the schema, you'll have to increase the version number
    exportSchema = false
)
@TypeConverters( //Specify the type converters that you want to use in the database
    StringListTypeConverter::class
)
@ConstructedBy(CarCareManagerDatabaseConstructor::class)
abstract class CarCareManagerDatabase : RoomDatabase(){
    abstract val vehicleDao: VehicleDao
    abstract val maintenanceDao: MaintenanceDao
    abstract val expenseDao: ExpenseDao


    companion object{
        const val DATABASE_NAME = "car_care_manager_db"
    }
}
