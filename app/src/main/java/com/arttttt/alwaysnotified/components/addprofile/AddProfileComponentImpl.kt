package com.arttttt.alwaysnotified.components.addprofile

import com.arttttt.alwaysnotified.arch.shared.context.AppComponentContext
import com.arttttt.alwaysnotified.arch.shared.dialog.DismissEvent
import com.arttttt.alwaysnotified.arch.shared.dialog.DismissEventConsumer
import com.arttttt.alwaysnotified.arch.shared.dialog.DismissEventDelegate
import com.arttttt.alwaysnotified.arch.shared.dialog.DismissEventProducer
import com.arttttt.alwaysnotified.arch.shared.events.producer.EventsProducerDelegate
import com.arttttt.alwaysnotified.arch.shared.events.producer.EventsProducerDelegateImpl
import com.arttttt.alwaysnotified.domain.repository.ProfilesRepository
import com.arttttt.alwaysnotified.utils.extensions.koinScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * todo: introduce store
 * todo: handle different errors
 */
class AddProfileComponentImpl(
    context: AppComponentContext,
    dismissEventDelegate: DismissEventDelegate = DismissEventDelegate(),
) : AddProfileComponent,
    AppComponentContext by context,
    EventsProducerDelegate<AddProfileComponent.Event> by EventsProducerDelegateImpl(),
    DismissEventConsumer by dismissEventDelegate,
    DismissEventProducer by dismissEventDelegate {

    private val scope = koinScope()

    private val profilesRepository: ProfilesRepository by scope.inject()

    override fun createProfileClicked(
        title: String,
        color: Int,
        addSelectedApps: Boolean,
    ) {
        dispatch(AddProfileComponent.Event.CreateProfile(title, color, addSelectedApps))
        onDismiss(DismissEvent)
    }

    override suspend fun canCreateProfile(title: String): Boolean {
        if (title.isEmpty()) return true

        return withContext(Dispatchers.IO) {
            !profilesRepository.doesProfileExist(title)
        }
    }
}