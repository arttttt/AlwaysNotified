package com.arttttt.appslist.impl.domain.store

import com.arttttt.appslist.impl.domain.repository.AppsRepository
import com.arttttt.simplemvi.actor.DefaultActor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal class AppsStoreActor(
    private val appsRepository: AppsRepository,
) : DefaultActor<AppsStore.Intent, AppsStore.State, AppsStore.SideEffect>() {

    override fun onInit() {
        scope.launch {
            reduce {
                copy(
                    isInProgress = true,
                )
            }

            joinAll(
                launch { getInstalledApplications() },
            )
        }
            .invokeOnCompletion {
                reduce {
                    copy(
                        isInProgress = false,
                    )
                }
            }
    }

    override fun handleIntent(intent: AppsStore.Intent) {
        when (intent) {
            is AppsStore.Intent.SelectApp -> selectApp(intent.pkg)
        }
    }

    private fun selectApp(pkg: String) {
        reduce {
            copy(
                selectedApps = if (pkg in selectedApps) {
                    selectedApps - pkg
                } else {
                    selectedApps + pkg
                },
            )
        }
    }

    private suspend fun getInstalledApplications() {
        val applications = withContext(Dispatchers.IO) {
            appsRepository
                .getInstalledApplications()
                .sortedBy { info -> info.title }
                .associateBy { info -> info.pkg }
        }

        reduce {
            copy(
                applications = applications,
            )
        }
    }
}