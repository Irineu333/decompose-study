package com.example.second

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value

interface SecondComponent {
    val text: Value<String>
}

class DefaultSecondComponent(
    context: ComponentContext,
    name: String
) : SecondComponent, ComponentContext by context {
    override val text = MutableValue(initialValue = "Hello, $name!")
}