package com.echcoding.carcaremanager.di

import com.echcoding.carcaremanager.presentation.vehicle.vehicle_list.VehicleListViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
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
    //singleOf(::KtorRemoteBookDataSource).bind<RemoteBookDataSource>()
    //singleOf(::DefaultBookRepository).bind<BookRepository>()

    // Inject the database instance using koin
    /*single {
        get<DatabaseFactory>().create()
            .setDriver(BundledSQLiteDriver())
            .build()
    }
    */

    //single { get<FavoriteBookDatabase>().favoriteBookDao }

    viewModelOf(::VehicleListViewModel)
    //viewModelOf(::BookDetailViewModel)
    //viewModelOf(::SelectedBookViewModel)
}