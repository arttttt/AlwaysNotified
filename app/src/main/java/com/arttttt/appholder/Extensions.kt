@file:Suppress("NOTHING_TO_INLINE")

package com.arttttt.appholder

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Parcelable
import androidx.core.content.IntentCompat
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arttttt.appholder.arch.DecomposeComponent
import com.arttttt.appholder.arch.FlowValue
import com.arttttt.appholder.arch.context.AppComponentContext
import com.arttttt.appholder.domain.entity.permission.Permission2
import com.arttttt.appholder.domain.entity.permission.StandardPermission
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.StateFlow
import org.koin.core.component.getScopeId
import org.koin.core.module.Module
import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.qualifier
import org.koin.core.scope.Scope
import org.koin.core.scope.ScopeCallback
import org.koin.core.scope.ScopeID
import org.koin.mp.KoinPlatform.getKoin
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

inline fun <reified T : Serializable?> Intent.getSerializable(name: String): T {
    return if (Build.VERSION.SDK_INT >= 34) {
        getSerializableExtra(name, T::class.java) as T
    } else {
        getSerializableExtra(name) as T
    }
}

inline fun <reified T> T.koinScope(
    vararg modules: Module,
    scopeID: ScopeID? = null,
    qualifier: Qualifier? = null,
) : Scope where T : DecomposeComponent,
                T : AppComponentContext
{
    val scope = getKoin().createScope(
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

    getKoin().loadModules(
        modulesList,
    )

    scope.registerCallback(
        object : ScopeCallback {
            override fun onScopeClose(scope: Scope) {
                getKoin().unloadModules(
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

inline fun<reified T : Activity> Context.activityIntent(block: Intent.() -> Unit): Intent {
    return Intent(this, T::class.java).apply(block)
}

inline fun <T> Result<T>.exceptCancellationException(): Result<T> {
    return onFailure { e ->
        if (e is CancellationException) throw e
    }
}

inline fun <T> Result<T>.finally(block: () -> Unit): Result<T> {
    return try {
        this
    } finally {
        block.invoke()
    }
}

inline fun <T : Any> StateFlow<T>.asValue(): Value<T> {
    return FlowValue(this)
}

fun Permission2.Status.Companion.of(value: Int): Permission2.Status {
    return if (value == PackageManager.PERMISSION_GRANTED) {
        Permission2.Status.Granted
    } else {
        Permission2.Status.Denied
    }
}

fun Permission2.Status.Companion.of(value: Boolean): Permission2.Status {
    return if (value) {
        Permission2.Status.Granted
    } else {
        Permission2.Status.Denied
    }
}

context(StandardPermission)
fun checkStatusImpl(context: Context): Permission2.Status {
    return Permission2.Status.of(context.checkSelfPermission(permission))
}