package com.example.second

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value

interface SecondComponent {
    val item: Value<String>
}

class DefaultSecondComponent(
    context: ComponentContext,
    item: String
) : SecondComponent, ComponentContext by context {
    override val item = MutableValue(item)
}