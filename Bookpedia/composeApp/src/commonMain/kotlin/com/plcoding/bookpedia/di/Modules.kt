package com.plcoding.bookpedia.di

import com.plcoding.bookpedia.book.data.network.KtorRemoteBookDataSource
import com.plcoding.bookpedia.book.data.network.RemoteBookDataSource
import com.plcoding.bookpedia.book.data.repository.DefaultBookRepository
import com.plcoding.bookpedia.book.domain.BookRepository
import com.plcoding.bookpedia.book.presentation.book_list.BookListViewModel
import com.plcoding.bookpedia.core.data.HttpClientFactory
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
    single { HttpClientFactory.create(get()) }
    // A shorthand to create the Singleton. The bind method allows to bind the implementation to the interface
    singleOf(::KtorRemoteBookDataSource).bind<RemoteBookDataSource>()
    singleOf(::DefaultBookRepository).bind<BookRepository>()

    viewModelOf(::BookListViewModel)
}