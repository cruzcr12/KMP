package com.echcoding.carcaremanager.di

import com.echcoding.carcaremanager.data.database.DatabaseFactory
import io.ktor.client.engine.HttpClientEngine
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * This is the specific 'implementation' for Android to get the HttpClientEngine
 */
actual val platformModule: Module
    get() = module {
        //single<HttpClientEngine> { OkHttp.create() }
        single { DatabaseFactory(androidApplication()) }
    }