package com.arttttt.appslist.impl.components.app

import com.arttttt.appslist.impl.domain.entity.AppInfo
import com.arttttt.appslist.impl.ui.app.AppContent
import com.arttttt.core.arch.content.ComponentContent
import com.arttttt.core.arch.context.AppComponentContext
import com.arttttt.core.arch.dialog.DismissEventConsumer
import com.arttttt.core.arch.dialog.DismissEventDelegate
import com.arttttt.core.arch.dialog.DismissEventProducer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

internal class AppComponentImpl(
    context: AppComponentContext,
    app: AppInfo,
    dismissEventDelegate: DismissEventDelegate = DismissEventDelegate(),
) : AppComponent,
    AppComponentContext by context,
    DismissEventConsumer by dismissEventDelegate,
    DismissEventProducer by dismissEventDelegate {

    private val transformer = AppTransformer()

    override val content: ComponentContent = AppContent(this)

    override val uiStates: StateFlow<AppComponent.UIState> = MutableStateFlow(
        transformer.invoke(app)
    )
}