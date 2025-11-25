package com.plcoding.bookpedia.di

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * This is the specific 'implementation' for Android to get the HttpClientEngine
 */
actual val platformModule: Module
    get() = module {
        single<HttpClientEngine> { OkHttp.create() }
    }