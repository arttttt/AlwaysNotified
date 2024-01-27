package com.arttttt.alwaysnotified

import android.app.Application
import com.arkivanov.mvikotlin.core.utils.isAssertOnMainThreadEnabled
import com.arttttt.alwaysnotified.di.commonModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        isAssertOnMainThreadEnabled = false

        startKoin {
            androidContext(this@App)
            androidLogger(Level.DEBUG)

            modules(commonModule)
        }
    }
}