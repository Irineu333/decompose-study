package com.example.first

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import kotlinx.serialization.builtins.serializer

interface FirstComponent {
    val name: Value<String>

    fun onChangeName(text: String)
    fun navigateToSecond()
}

class DefaultFirstComponent(
    context: ComponentContext,
    private val navigateToSecond: (name: String) -> Unit,
) : FirstComponent, ComponentContext by context {

    private val _name = MutableValue(
        initialValue = stateKeeper.consume(
            key = NAME_KEY,
            strategy = String.serializer()
        ).orEmpty()
    )

    override val name = _name

    init {
        stateKeeper.register(
            key = NAME_KEY,
            strategy = String.serializer(),
            supplier = { name.value }
        )
    }

    override fun onChangeName(text: String) {
        _name.value = text
    }

    override fun navigateToSecond() {
        navigateToSecond(name.value)
    }

    companion object {
        private const val NAME_KEY = "NAME_KEY"
    }
}