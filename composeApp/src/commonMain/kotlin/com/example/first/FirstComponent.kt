package com.example.first

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value

interface FirstComponent {
    val items: Value<List<String>>

    fun onItemClicked(item: String)
}

class DefaultFirstComponent(
    context: ComponentContext,
    private val onItemSelected: (item: String) -> Unit,
) : FirstComponent, ComponentContext by context {

    override val items = MutableValue(
        initialValue = List(size = 100) { "Item $it" }
    )

    override fun onItemClicked(item: String) {
        onItemSelected(item)
    }
}