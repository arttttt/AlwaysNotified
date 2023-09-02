package com.arttttt.appholder.ui.permissions

import com.arkivanov.decompose.value.operator.map
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.arkivanov.mvikotlin.extensions.coroutines.states
import com.arttttt.appholder.arch.context.AppComponentContext
import com.arttttt.appholder.arch.events.EventsProducerDelegate
import com.arttttt.appholder.arch.events.EventsProducerDelegateImpl
import com.arttttt.appholder.asValue
import com.arttttt.appholder.domain.entity.permission.Permission2
import com.arttttt.appholder.domain.store.permissions.PermissionsStore
import com.arttttt.appholder.koinScope
import com.arttttt.appholder.ui.base.ListItem
import com.arttttt.appholder.ui.permissions.di.permissionsModule
import com.arttttt.appholder.ui.permissions.models.PermissionLazyListItem
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.plus
import kotlinx.coroutines.runBlocking
import kotlin.reflect.KClass

class PermissionsComponentImpl(
    componentContext: AppComponentContext,
) : PermissionsComponent,
    EventsProducerDelegate<PermissionsComponent.Event> by EventsProducerDelegateImpl(),
    AppComponentContext by componentContext {


    private val scope = koinScope<PermissionsComponent>(
        permissionsModule,
    )

    private val permissionsStore = scope.get<PermissionsStore>()

    private val coroutineScope = MainScope() + SupervisorJob()

    override val state = permissionsStore
        .stateFlow
        .asValue()
        .map { state ->
            state.toComponentState()
        }

    init {
        lifecycle.doOnDestroy { coroutineScope.coroutineContext.cancelChildren() }

        permissionsStore
            .labels
            .filterIsInstance<PermissionsStore.Label.AllPermissionsGranted>()
            .take(1)
            .onEach { dispatch(PermissionsComponent.Event.AllPermissionsGranted) }
            .launchIn(coroutineScope)
    }

    override fun needRequestPermissions(): Boolean {
        return runBlocking {
            permissionsStore
                .states
                .filter { state -> !state.isInProgress }
                .take(1)
                .map { state -> state.deniedPermissions.isNotEmpty() }
                .first()
        }
    }

    override fun grantPermissionClicked(permission: KClass<out Permission2>) {
        permissionsStore.accept(PermissionsStore.Intent.RequestPermission(permission))
    }

    private fun PermissionsStore.State.toComponentState(): PermissionsComponent.State {
        return PermissionsComponent.State(
            items = createItems(this)
        )
    }

    private fun createItems(state: PermissionsStore.State): List<ListItem> {
        val items = mutableListOf<ListItem>()

        state.grantedPermissions.mapTo(items) { permission ->
            PermissionLazyListItem.Granted(
                key = permission.toString(),
                title = permission.title
            )
        }

        state.deniedPermissions.mapTo(items) { permission ->
            PermissionLazyListItem.Denied(
                title = permission.title,
                permission = permission::class,
            )
        }

        return items
    }
}