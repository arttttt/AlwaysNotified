package com.arttttt.alwaysnotified.domain.store.permissions

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arttttt.alwaysnotified.domain.repository.PermissionsRepository
import com.arttttt.alwaysnotified.utils.permissions.PermissionsRequester

class PermissionsStoreFactory(
    private val storeFactory: StoreFactory,
    private val repository: PermissionsRepository,
    private val permissionsRequester: PermissionsRequester,
) {

    fun create(): PermissionsStore {
        return object : PermissionsStore, Store<PermissionsStore.Intent, PermissionsStore.State, PermissionsStore.Label> by storeFactory.create(
            name = PermissionsStore::class.qualifiedName,
            initialState = PermissionsStore.State(
                isInProgress = false,
                permissions = emptyMap(),
            ),
            bootstrapper = SimpleBootstrapper(PermissionsStore.Action.GetRequestedPermissions),
            executorFactory = {
                PermissionsStoreExecutor(
                    repository = repository,
                    permissionsRequester = permissionsRequester,
                )
            },
            reducer = PermissionsReducer,
        ) {}
    }
}