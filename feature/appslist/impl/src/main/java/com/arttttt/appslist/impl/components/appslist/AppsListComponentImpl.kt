package com.arttttt.appslist.impl.components.appslist

import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arttttt.appslist.SelectedActivity
import com.arttttt.appslist.api.AppsListComponent
import com.arttttt.appslist.impl.components.app.AppComponent
import com.arttttt.appslist.impl.components.appslist.di.appsListModule
import com.arttttt.appslist.impl.domain.AppsLauncher
import com.arttttt.appslist.impl.domain.entity.AppInfo
import com.arttttt.appslist.impl.domain.store.AppsStore
import com.arttttt.appslist.impl.ui.appslist.AppsListContent
import com.arttttt.core.arch.DecomposeComponent
import com.arttttt.core.arch.content.ComponentContent
import com.arttttt.core.arch.context.AppComponentContext
import com.arttttt.core.arch.context.wrapComponentContext
import com.arttttt.core.arch.events.producer.EventsProducerDelegate
import com.arttttt.core.arch.events.producer.EventsProducerDelegateImpl
import com.arttttt.core.arch.koinScope
import com.arttttt.core.arch.slotComponentEvents
import com.arttttt.core.arch.slotDismissEvents
import com.arttttt.topbar.api.TopBarComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.koin.core.component.getScopeId
import org.koin.core.qualifier.qualifier

internal class AppsListComponentImpl(
    context: AppComponentContext,
) : AppsListComponent,
    InternalAppsListComponent,
    AppComponentContext by context,
    EventsProducerDelegate<AppsListComponent.Event> by EventsProducerDelegateImpl() {

    @Serializable
    sealed interface DialogConfig {

        @Serializable
        data class App(
            val app: AppInfo,
            val selectedActivity: SelectedActivity?,
        ) : DialogConfig
    }

    private val koinScope = koinScope(
        appsListModule,
        scopeID = getScopeId(),
        qualifier = qualifier<AppsListComponent>()
    )

    private val appsStore: AppsStore by koinScope.inject()
    private val appsLauncher: AppsLauncher by koinScope.inject()
    private val transformer: AppsListTransformer by koinScope.inject()

    private val appComponentFactory: AppComponent.Factory by koinScope.inject()

    private val coroutineScope = coroutineScope()

    override val content: ComponentContent = AppsListContent(this)

    private val slotNavigation = SlotNavigation<DialogConfig>()

    override val topBarComponent: TopBarComponent = koinScope
        .get<TopBarComponent.Factory>()
        .create(
            context = wrapComponentContext(
                context = childContext(
                    key = "topbar",
                ),
                parentScopeID = koinScope.id,
            ),
        )

    override val uiState = combine(
        appsStore.states,
        topBarComponent.appsSearchComponent.states,
        ::Pair,
    )
        .map(transformer)
        .flowOn(Dispatchers.IO)
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = transformer.invoke(
                Pair(
                    appsStore.state,
                    topBarComponent.appsSearchComponent.states.value,
                )
            )
        )

    override val slot: Value<ChildSlot<*, DecomposeComponent>> = childSlot(
        source = slotNavigation,
        serializer = DialogConfig.serializer(),
        handleBackButton = true,
        childFactory = ::createDialog,
    )

    init {
        slot
            .slotDismissEvents()
            .onEach { slotNavigation.dismiss() }
            .launchIn(coroutineScope)

        slot
            .slotComponentEvents<AppComponent.Event>()
            .filterIsInstance<AppComponent.Event.EditingFinished>()
            .onEach { event ->
                handeAppEditingFinished(
                    pkg = event.pkg,
                    selectedActivity = event.selectedActivity,
                )
            }
            .launchIn(coroutineScope)
    }

    override fun startApps() {
        coroutineScope.launch {
            appsLauncher.launchApps()
        }
    }

    override fun onAppClicked(pkg: String) {
        slotNavigation.activate(
            DialogConfig.App(
                app = appsStore.state.applications!!.getValue(pkg),
                selectedActivity = appsStore.state.selectedActivities?.get(pkg),
            )
        )
    }

    override fun openSettings() {
        dispatch(AppsListComponent.Event.OpenSettings)
    }

    private fun handeAppEditingFinished(
        pkg: String,
        selectedActivity: SelectedActivity?,
    ) {
        appsStore.accept(
            AppsStore.Intent.SetSelectedActivity(
                pkg = pkg,
                selectedActivity = selectedActivity,
            )
        )
    }

    private fun createDialog(
        config: DialogConfig,
        context: AppComponentContext,
    ): DecomposeComponent {
        val wrappedContext = wrapComponentContext(
            context = context,
            parentScopeID = koinScope.id,
        )

        return when (config) {
            is DialogConfig.App -> appComponentFactory.create(
                context = wrappedContext,
                app = config.app,
                selectedActivity = config.selectedActivity,
            )
        }
    }
}