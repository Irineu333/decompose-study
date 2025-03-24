package com.example.app

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.decompose.value.Value
import com.example.first.DefaultFirstComponent
import com.example.first.FirstComponent
import com.example.second.DefaultSecondComponent
import com.example.second.SecondComponent
import kotlinx.serialization.Serializable

interface AppComponent {
    val stack: Value<ChildStack<*, Child>>

    sealed class Child {
        class First(val component: FirstComponent) : Child()
        class Second(val component: SecondComponent) : Child()
    }
}

class DefaultAppComponent(
    context: ComponentContext,
) : AppComponent, ComponentContext by context {

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, AppComponent.Child>> = childStack(
        source = navigation,
        serializer = Config.serializer(),
        initialConfiguration = Config.First,
        handleBackButton = true,
        childFactory = ::childFactory,
    )

    private fun childFactory(
        config: Config,
        context: ComponentContext
    ): AppComponent.Child = when (config) {
        is Config.First -> {
            AppComponent.Child.First(
                DefaultFirstComponent(
                    context = context,
                    navigateToSecond = { name ->
                        navigation.pushNew(Config.Second(name))
                    }
                )
            )
        }

        is Config.Second -> {
            AppComponent.Child.Second(
                DefaultSecondComponent(
                    context = context,
                    name = config.name
                )
            )
        }
    }

    @Serializable
    private sealed interface Config {
        @Serializable
        data object First : Config

        @Serializable
        data class Second(val name: String) : Config
    }
}