package com.echcoding.carcaremanager.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.echcoding.carcaremanager.data.database.CarCareManagerDatabase
import com.echcoding.carcaremanager.data.database.DatabaseFactory
import com.echcoding.carcaremanager.data.repository.ExpenseRepositoryImpl
import com.echcoding.carcaremanager.data.repository.MaintenanceRepositoryImpl
import com.echcoding.carcaremanager.data.repository.VehicleRepositoryImpl
import com.echcoding.carcaremanager.domain.repository.ExpenseRepository
import com.echcoding.carcaremanager.domain.repository.MaintenanceRepository
import com.echcoding.carcaremanager.domain.repository.VehicleRepository
import com.echcoding.carcaremanager.presentation.expense.expense_list.ExpenseListViewModel
import com.echcoding.carcaremanager.presentation.maintenance.maintenance_detail.MaintenanceDetailViewModel
import com.echcoding.carcaremanager.presentation.maintenance.maintenance_list.MaintenanceListViewModel
import com.echcoding.carcaremanager.presentation.navigation.NavigationViewModel
import com.echcoding.carcaremanager.presentation.vehicle.vehicle_detail.VehicleDetailViewModel
import com.echcoding.carcaremanager.presentation.vehicle.vehicle_list.VehicleListViewModel
import com.echcoding.carcaremanager.presentation.vehicle.vehicle_selected.SelectedVehicleViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module


/**
 * There are some dependencies which instantiation depends on the platform, like iOS or Android
 */

expect val platformModule: Module


// The shareModule is a container for all the dependencies that are shared between iOS and Android
val sharedModule = module {
    // Create a Singleton instance of HttpClientFactory. The get method resolves the dependency.
    ///single { HttpClientFactory.create(get()) }
    // A shorthand to create the Singleton. The bind method allows to bind the implementation to the interface
    singleOf(::VehicleRepositoryImpl).bind<VehicleRepository>()
    singleOf(::MaintenanceRepositoryImpl).bind<MaintenanceRepository>()
    singleOf(::ExpenseRepositoryImpl).bind<ExpenseRepository>()

    // Inject the database instance using koin
    single {
        get<DatabaseFactory>().create()
            .setDriver(BundledSQLiteDriver())
            .build()
    }
    // DAOs
    single { get<CarCareManagerDatabase>().vehicleDao }
    single { get<CarCareManagerDatabase>().maintenanceDao }
    single { get<CarCareManagerDatabase>().expenseDao }

    // ViewModels
    viewModelOf(::VehicleListViewModel)
    viewModelOf(::VehicleDetailViewModel)
    viewModelOf(::SelectedVehicleViewModel)
    viewModelOf(::MaintenanceListViewModel)
    viewModelOf(::MaintenanceDetailViewModel)
    viewModelOf(::ExpenseListViewModel)
    viewModelOf(::NavigationViewModel)
}