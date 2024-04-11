package com.arttttt.appslist.impl.components.di

import com.arttttt.appslist.api.AppsListComponent
import com.arttttt.appslist.impl.components.AppsListTransformer
import org.koin.dsl.module

internal val appsListModule = module {
    scope<AppsListComponent> {
        scoped {
            AppsListTransformer(
                resourcesProvider = get(),
            )
        }
    }
}