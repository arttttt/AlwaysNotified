package com.arttttt.alwaysnotified.components.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.arttttt.alwaysnotified.arch.shared.DecomposeComponent

interface RootComponent : DecomposeComponent {

    val stack: Value<ChildStack<*, DecomposeComponent>>
}