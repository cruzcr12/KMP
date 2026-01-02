package com.echcoding.carcaremanager.di

import io.ktor.client.engine.HttpClientEngine
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * This is the specific 'implementation' for Android to get the HttpClientEngine
 */
actual val platformModule: Module
    get() = module {
        //single<HttpClientEngine> { OkHttp.create() }
        //single { DatabaseFactory(androidApplication()) }
    }