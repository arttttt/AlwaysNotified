package com.arttttt.appslist.impl.domain.store

import com.arttttt.appslist.impl.domain.repository.AppsRepository
import com.arttttt.simplemvi.actor.DefaultActor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal class AppsStoreActor(
    private val appsRepository: AppsRepository,
) : DefaultActor<AppsStore.Intent, AppsStore.State, AppsStore.SideEffect>() {

    override fun onInit() {
        scope.launch {
            reduce { copy(isInProgress = true) }

            getInstalledApplications()
        }
            .invokeOnCompletion {
                reduce { copy(isInProgress = false) }
            }
    }

    override fun handleIntent(intent: AppsStore.Intent) {
        when (intent) {
            is AppsStore.Intent.ToggleAppSelection -> toggleAppSelection(intent.pkg)
        }
    }

    private fun toggleAppSelection(pkg: String) {
        scope.launch {
            val selectedApps = if (pkg in state.selectedApps) {
                appsRepository.removeApp(pkg)

                state.selectedApps - pkg
            } else {
                appsRepository.saveApp(pkg)

                state.selectedApps + pkg
            }

            reduce { copy(selectedApps = selectedApps) }
        }
    }

    private suspend fun getInstalledApplications() {
        val applications = withContext(Dispatchers.IO) {
            appsRepository
                .getInstalledApplications()
                .sortedBy { info -> info.title }
                .associateBy { info -> info.pkg }
        }

        val selectedApps = withContext(Dispatchers.IO) {
            appsRepository.getAllApps().toSet()
        }

        reduce {
            copy(
                applications = applications,
                selectedApps = selectedApps,
            )
        }
    }
}