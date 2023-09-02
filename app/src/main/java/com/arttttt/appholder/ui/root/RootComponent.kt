package com.arttttt.appholder.ui.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.arttttt.appholder.arch.DecomposeComponent

interface RootComponent : DecomposeComponent {

    val stack: Value<ChildStack<*, DecomposeComponent>>
}