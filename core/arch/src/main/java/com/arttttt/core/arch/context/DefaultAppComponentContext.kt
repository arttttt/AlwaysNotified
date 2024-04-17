package com.arttttt.core.arch.context

import com.arkivanov.decompose.ComponentContextFactory
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.GenericComponentContext
import com.arkivanov.essenty.backhandler.BackHandlerOwner
import com.arkivanov.essenty.instancekeeper.InstanceKeeperOwner
import com.arkivanov.essenty.lifecycle.LifecycleOwner
import com.arkivanov.essenty.statekeeper.StateKeeperOwner
import org.koin.core.scope.ScopeID

class DefaultAppComponentContext(
    context: GenericComponentContext<*>,
    override val parentScopeID: ScopeID?,
) : AppComponentContext,
    LifecycleOwner by context,
    StateKeeperOwner by context,
    InstanceKeeperOwner by context,
    BackHandlerOwner by context {

    override val componentContextFactory =
        ComponentContextFactory<AppComponentContext> { lifecycle, stateKeeper, instanceKeeper, backHandler ->
            DefaultAppComponentContext(
                context = DefaultComponentContext(
                    lifecycle = lifecycle,
                    stateKeeper = stateKeeper,
                    instanceKeeper = instanceKeeper,
                    backHandler = backHandler,
                ),
                parentScopeID = parentScopeID,
            )
        }
}