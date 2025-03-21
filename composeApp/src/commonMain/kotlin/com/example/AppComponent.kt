package com.example

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value

interface AppComponent {
    val text: Value<String>
}

class DefaultAppComponent(
    context: ComponentContext,
) : AppComponent, ComponentContext by context {

    private val _text = MutableValue(initialValue = "Hello, world!")
    override val text = _text
}