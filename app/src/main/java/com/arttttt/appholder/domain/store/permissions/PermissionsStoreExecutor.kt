package com.arttttt.appholder.domain.store.permissions

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.arttttt.appholder.domain.entity.permission.Permission2
import com.arttttt.appholder.domain.repository.PermissionsRepository
import com.arttttt.appholder.exceptCancellationException
import com.arttttt.appholder.utils.PermissionsRequester
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

class PermissionsStoreExecutor(
    private val repository: PermissionsRepository,
    private val permissionsRequester: PermissionsRequester,
) : CoroutineExecutor<PermissionsStore.Intent, PermissionsStore.Action, PermissionsStore.State, PermissionsStore.Message, PermissionsStore.Label>() {

    override fun executeAction(
        action: PermissionsStore.Action,
        getState: () -> PermissionsStore.State,
    ) {
        when (action) {
            is PermissionsStore.Action.GetRequestedPermissions -> getAndCheckPermissions()
        }
    }

    override fun executeIntent(
        intent: PermissionsStore.Intent,
        getState: () -> PermissionsStore.State,
    ) {
        when (intent) {
            is PermissionsStore.Intent.RequestPermission -> requestPermission(getState, intent.permission)
            is PermissionsStore.Intent.CheckPermissions -> getAndCheckPermissions()
        }
    }

    private fun requestPermission(
        getState: () -> PermissionsStore.State,
        permission: KClass<out Permission2>,
    ) {
        scope.launch {
            kotlin
                .runCatching {
                    val permission = getState
                        .invoke()
                        .permissions
                        .getValue(Permission2.Status.Denied)
                        .getValue(permission)

                    permissionsRequester
                        .requestPermission(permission)
                        .takeIf { status -> status != Permission2.Status.Denied }!!
                }
                .onSuccess {
                    getAndCheckPermissions()

                    val permissions = getState.invoke().permissions.get(Permission2.Status.Denied)
                    if (permissions.isNullOrEmpty()) {
                        publish(PermissionsStore.Label.AllPermissionsGranted)
                    }
                }
                .exceptCancellationException()
        }
    }

    private fun getAndCheckPermissions() {
        runCatching {
            repository
                .getRequiredPermissions()
                .groupBy { permission ->
                    repository.checkPermission(permission)
                }
                .mapValues { (_, value) ->
                    value.associateBy { it::class }
                }
                .let(PermissionsStore.Message::PermissionsReceived)
                .let(this::dispatch)
        }
            .exceptCancellationException()
    }
}