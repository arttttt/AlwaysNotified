package com.arttttt.appholder.utils.extensions

import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arttttt.appholder.arch.DecomposeComponent
import com.arttttt.appholder.arch.FlowValue
import com.arttttt.appholder.arch.context.AppComponentContext
import kotlinx.coroutines.flow.StateFlow
import org.koin.core.component.getScopeId
import org.koin.core.module.Module
import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.qualifier
import org.koin.core.scope.Scope
import org.koin.core.scope.ScopeCallback
import org.koin.core.scope.ScopeID
import org.koin.mp.KoinPlatform

inline fun <reified T> T.koinScope(
    vararg modules: Module,
    scopeID: ScopeID? = null,
    qualifier: Qualifier? = null,
) : Scope where T : DecomposeComponent,
                T : AppComponentContext
{
    val scope = KoinPlatform.getKoin().createScope(
        scopeId = scopeID ?: getScopeId<T>(),
        qualifier = qualifier ?: qualifier<T>(),
        source = null,
    )

    val modulesList = modules.toList()

    parentScopeID
        ?.let(scope::getScope)
        ?.let { parentScope ->
            scope.linkTo(parentScope)
        }

    KoinPlatform.getKoin().loadModules(
        modulesList,
    )

    scope.registerCallback(
        object : ScopeCallback {
            override fun onScopeClose(scope: Scope) {
                KoinPlatform.getKoin().unloadModules(
                    modulesList
                )
            }
        }
    )

    lifecycle.doOnDestroy {
        scope.close()
    }

    return scope
}

inline fun <reified T : Any> StateFlow<T>.asValue(): Value<T> {
    return FlowValue(this)
}