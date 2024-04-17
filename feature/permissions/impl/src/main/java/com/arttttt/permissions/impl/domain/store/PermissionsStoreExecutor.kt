package com.arttttt.permissions.impl.domain.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.arttttt.utils.exceptCancellationException
import com.arttttt.permissions.impl.domain.entity.Permission2
import com.arttttt.permissions.impl.domain.repository.PermissionsRepository
import com.arttttt.permissions.impl.utils.PermissionsRequester
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

internal class PermissionsStoreExecutor(
    private val repository: PermissionsRepository,
    private val permissionsRequester: PermissionsRequester,
) : CoroutineExecutor<PermissionsStore.Intent, PermissionsStore.Action, PermissionsStore.State, PermissionsStore.Message, PermissionsStore.Label>() {

    override fun executeAction(
        action: PermissionsStore.Action,
    ) {
        when (action) {
            is PermissionsStore.Action.GetRequestedPermissions -> getAndCheckPermissions()
        }
    }

    override fun executeIntent(
        intent: PermissionsStore.Intent,
    ) {
        when (intent) {
            is PermissionsStore.Intent.RequestPermission -> requestPermission(intent.permission)
            is PermissionsStore.Intent.CheckPermissions -> getAndCheckPermissions()
        }
    }

    private fun requestPermission(
        permission: KClass<out Permission2>,
    ) {
        scope.launch {
            kotlin
                .runCatching {
                    val permission = state()
                        .permissions
                        .getValue(Permission2.Status.Denied)
                        .getValue(permission)

                    permissionsRequester
                        .requestPermission(permission)
                        .takeIf { status -> status != Permission2.Status.Denied }!!
                }
                .onSuccess {
                    getAndCheckPermissions()

                    val permissions = state().permissions.get(Permission2.Status.Denied)
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