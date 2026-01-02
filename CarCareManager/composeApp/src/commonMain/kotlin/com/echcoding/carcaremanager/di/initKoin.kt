package com.echcoding.carcaremanager.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

// Instructs Koin to use the corresponding modules
fun initKoin(config: KoinAppDeclaration? = null){
    startKoin {
        config?.invoke(this)
        modules(sharedModule, platformModule)
    }
}