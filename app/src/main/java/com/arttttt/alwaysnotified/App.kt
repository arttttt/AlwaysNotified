package com.arttttt.alwaysnotified

import android.app.Application
import com.arttttt.alwaysnotified.di.commonModule
import com.arttttt.alwaysnotified.utils.timber.CrashReportingTree
import com.arttttt.appslist.impl.di.appsListFeatureModule
import com.arttttt.appssearch.impl.di.appsSearchFeatureModule
import com.arttttt.permissions.impl.di.permissionsFeatureModule
import com.arttttt.topbar.impl.di.topBarFeatureModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        Timber.plant(
            CrashReportingTree(
                printLogToConsole = BuildConfig.DEBUG
            )
        )

        startKoin {
            androidContext(this@App)
            androidLogger(Level.DEBUG)

            modules(
                commonModule,
                appsSearchFeatureModule,
                topBarFeatureModule,
                appsListFeatureModule,
                permissionsFeatureModule,
            )
        }
    }
}