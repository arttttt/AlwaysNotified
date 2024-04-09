package com.arttttt.addprofile.impl.components

import com.arttttt.addprofile.api.AddProfileComponent
import com.arttttt.addprofile.api.ProfileExistsChecker
import com.arttttt.addprofile.impl.ui.AddProfileContentImpl
import com.arttttt.core.arch.content.ComponentContent
import com.arttttt.core.arch.context.AppComponentContext
import com.arttttt.core.arch.dialog.DismissEvent
import com.arttttt.core.arch.dialog.DismissEventConsumer
import com.arttttt.core.arch.dialog.DismissEventDelegate
import com.arttttt.core.arch.dialog.DismissEventProducer
import com.arttttt.core.arch.events.producer.EventsProducerDelegate
import com.arttttt.core.arch.events.producer.EventsProducerDelegateImpl
import com.arttttt.core.arch.koinScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.getScopeId
import org.koin.core.qualifier.qualifier

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

    private val scope = koinScope(
        scopeID = getScopeId(),
        qualifier = qualifier<AddProfileComponent>(),
    )
    override val content: ComponentContent = AddProfileContentImpl(this)

    private val profileExistsChecker: ProfileExistsChecker by scope.inject()

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
            !profileExistsChecker.isProfileExist(title)
        }
    }
}