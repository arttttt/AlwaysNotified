package com.arttttt.appholder

import android.content.Context
import android.content.Intent
import android.os.Parcelable
import androidx.core.content.IntentCompat
import androidx.core.os.BuildCompat
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arttttt.appholder.arch.DecomposeComponent
import org.koin.core.component.KoinComponent
import org.koin.core.component.getScopeId
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.qualifier
import org.koin.core.scope.Scope
import org.koin.core.scope.ScopeID
import java.io.Serializable

inline fun <reified R> Any.castTo(): R? {
    return this as? R
}

inline fun <reified R> Any.unsafeCastTo(): R {
    return this as R
}

inline fun <reified T> Context.intent(
    block: Intent.() -> Unit = {}
): Intent {
    return Intent(this, T::class.java).apply(block)
}

inline fun <reified T : Parcelable?> Intent.getParcelable(name: String): T {
    return IntentCompat.getParcelableExtra(
        this,
        name,
        T::class.java,
    ) as T
}

@androidx.annotation.OptIn(BuildCompat.PrereleaseSdkCheck::class)
inline fun <reified T : Serializable?> Intent.getSerializable(name: String): T {
    return if (BuildCompat.isAtLeastU()) {
        getSerializableExtra(name, T::class.java) as T
    } else {
        getSerializableExtra(name) as T
    }
}

/**
 * WIP
 *
 * todo: add an ability to link to a parent
 */
inline fun <reified T> T.scope(
    scopeID: ScopeID? = null,
    qualifier: Qualifier? = null,
    vararg modules: Module,
) : Scope where T : DecomposeComponent,
                T : ComponentContext,
                T: KoinComponent
{
    val scope = getKoin().createScope(
        scopeId = scopeID ?: getScopeId<T>(),
        qualifier = qualifier ?: qualifier<T>(),
        source = null,
    )

    val modulesList = modules.toList()

    getKoin().loadModules(
        modulesList,
    )

    lifecycle.doOnDestroy {
        getKoin().unloadModules(
            modulesList
        )
    }

    return scope
}