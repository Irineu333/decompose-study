package com.example.first

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value

interface FirstComponent {
    val name: Value<String>

    fun onChangeName(text: String)
    fun navigateToSecond()
}

class DefaultFirstComponent(
    context: ComponentContext,
    private val navigateToSecond: (name: String) -> Unit,
) : FirstComponent, ComponentContext by context {

    private val _name = MutableValue(initialValue = "")
    override val name = _name

    override fun onChangeName(text: String) {
        _name.value = text
    }

    override fun navigateToSecond() {
        navigateToSecond(name.value)
    }
}