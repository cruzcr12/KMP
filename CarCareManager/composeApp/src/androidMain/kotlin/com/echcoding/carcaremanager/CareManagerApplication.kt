package com.echcoding.carcaremanager

import android.app.Application
import com.echcoding.carcaremanager.di.initKoin
import org.koin.android.ext.koin.androidContext

class CareManagerApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidContext(this@CareManagerApplication)
        }

    }
}