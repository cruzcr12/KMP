package com.echcoding.carcaremanager.di

import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module
    get() = module {
        single { DatabaseFactory() }
    }