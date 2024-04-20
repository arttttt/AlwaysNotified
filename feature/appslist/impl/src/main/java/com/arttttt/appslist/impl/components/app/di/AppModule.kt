package com.arttttt.appslist.impl.components.app.di

import com.arttttt.appslist.impl.components.app.AppComponent
import com.arttttt.appslist.impl.components.app.AppTransformer
import org.koin.dsl.module

internal val appModule = module {
    scope<AppComponent> {
        scoped {
            AppTransformer(
                resourcesProvider = get(),
            )
        }
    }
}