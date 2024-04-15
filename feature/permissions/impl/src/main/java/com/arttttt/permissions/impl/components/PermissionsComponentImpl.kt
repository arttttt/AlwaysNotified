package com.arttttt.permissions.impl.components

import com.arkivanov.decompose.value.operator.map
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.arkivanov.mvikotlin.extensions.coroutines.states
import com.arttttt.core.arch.context.AppComponentContext
import com.arttttt.permissions.impl.components.di.permissionsModule
import com.arttttt.permissions.impl.domain.entity.Permission2
import com.arttttt.lazylist.ListItem
import com.arttttt.core.arch.asValue
import com.arttttt.core.arch.content.ComponentContent
import com.arttttt.core.arch.koinScope
import com.arttttt.core.arch.events.producer.EventsProducerDelegate
import com.arttttt.core.arch.events.producer.EventsProducerDelegateImpl
import com.arttttt.permissions.api.PermissionsComponent
import com.arttttt.permissions.impl.domain.store.PermissionsStore
import com.arttttt.permissions.impl.ui.PermissionsContent
import com.arttttt.permissions.impl.ui.models.PermissionLazyListItem
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import org.koin.core.component.getScopeId
import org.koin.core.qualifier.qualifier
import kotlin.reflect.KClass

internal class PermissionsComponentImpl(
    context: AppComponentContext,
) : PermissionsComponent,
    InternalPermissionsComponent,
    EventsProducerDelegate<PermissionsComponent.Event> by EventsProducerDelegateImpl(),
    AppComponentContext by context {

    private val coroutineScope = coroutineScope()

    private val koinScope = koinScope(
        permissionsModule,
        scopeID = getScopeId(),
        qualifier = qualifier<PermissionsComponent>()
    )

    private val permissionsStore = koinScope.get<PermissionsStore>()

    override val state = permissionsStore
        .stateFlow
        .asValue()
        .map { state ->
            state.toComponentState()
        }

    override val content: ComponentContent = PermissionsContent(this)

    init {
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

    private fun PermissionsStore.State.toComponentState(): InternalPermissionsComponent.State {
        return InternalPermissionsComponent.State(
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