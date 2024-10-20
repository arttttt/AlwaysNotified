package com.arttttt.permissions.impl.domain.store

import com.arttttt.permissions.impl.domain.entity.Permission2
import com.arttttt.permissions.impl.domain.repository.PermissionsRepository
import com.arttttt.permissions.impl.utils.PermissionsRequester
import com.arttttt.simplemvi.actor.dsl.DslActorScope
import com.arttttt.simplemvi.actor.dsl.actorDsl
import com.arttttt.simplemvi.logging.loggingActor
import com.arttttt.simplemvi.store.Store
import com.arttttt.simplemvi.store.createStore
import com.arttttt.utils.exceptCancellationException
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

internal class PermissionsStore(
    repository: PermissionsRepository,
    permissionsRequester: PermissionsRequester,
) : Store<PermissionsStore.Intent, PermissionsStore.State, PermissionsStore.SideEffect> by createStore(
    initialState = State(
        isInProgress = false,
        permissions = emptyMap(),
    ),
    initialIntents = listOf(Intent.GetRequestedPermissions),
    actor = loggingActor(
        name = PermissionsStore::class.simpleName,
        delegate = actorDsl {
            onIntent<Intent.GetRequestedPermissions> {
                getAndCheckPermissions(
                    repository = repository,
                )
            }

            onIntent<Intent.RequestPermission> { intent ->
                requestPermission(
                    permission = intent.permission,
                    permissionsRequester = permissionsRequester,
                    repository = repository,
                )
            }

            onIntent<Intent.CheckPermissions> {
                getAndCheckPermissions(
                    repository = repository,
                )
            }
        },
    )
) {

    sealed interface Intent {

        data object GetRequestedPermissions : Intent

        data class RequestPermission(
            val permission: KClass<out Permission2>,
        ) : Intent

        data object CheckPermissions : Intent
    }

    data class State(
        val isInProgress: Boolean,
        val permissions: Map<Permission2.Status, Map<KClass<out Permission2>, Permission2>>,
    ) {

        val grantedPermissions: List<Permission2>
            get() {
                return permissions
                    .getOrElse(Permission2.Status.Granted) { emptyMap() }
                    .values
                    .toList()
            }

        val deniedPermissions: List<Permission2>
            get() {
                return permissions
                    .getOrElse(Permission2.Status.Denied) { emptyMap() }
                    .values
                    .toList()
            }
    }

    sealed interface SideEffect {

        data object AllPermissionsGranted : SideEffect
    }
}

private fun DslActorScope<PermissionsStore.Intent, PermissionsStore.State, PermissionsStore.SideEffect>.requestPermission(
    permission: KClass<out Permission2>,
    permissionsRequester: PermissionsRequester,
    repository: PermissionsRepository,
) {
    launch {
        kotlin
            .runCatching {
                val permission = state
                    .permissions
                    .getValue(Permission2.Status.Denied)
                    .getValue(permission)

                permissionsRequester
                    .requestPermission(permission)
                    .takeIf { status -> status != Permission2.Status.Denied }!!
            }
            .onSuccess {
                getAndCheckPermissions(
                    repository = repository,
                )

                val permissions = state.permissions[Permission2.Status.Denied]
                if (permissions.isNullOrEmpty()) {
                    sideEffect(PermissionsStore.SideEffect.AllPermissionsGranted)
                }
            }
            .exceptCancellationException()
    }
}

private fun DslActorScope<PermissionsStore.Intent, PermissionsStore.State, PermissionsStore.SideEffect>.getAndCheckPermissions(
    repository: PermissionsRepository,
) {
    runCatching {
        val permissions = repository
            .getRequiredPermissions()
            .groupBy { permission ->
                repository.checkPermission(permission)
            }
            .mapValues { (_, value) ->
                value.associateBy { it::class }
            }

        reduce {
            copy(
                permissions = permissions,
            )
        }
    }
        .exceptCancellationException()
}