package com.example.second

import androidx.compose.ui.graphics.Color
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.*
import kotlin.random.Random

interface SecondComponent {
    val uiState: Value<UiState>

    fun startColorAnimation()

    data class UiState(
        val text: String,
        val color: Color
    )
}

class DefaultSecondComponent(
    context: ComponentContext,
    name: String
) : SecondComponent, ComponentContext by context {

    private val coroutine = CoroutineScope(Dispatchers.Default)
    private var colorAnimationJob: Job? = null

    private val _uiState = MutableValue(
        initialValue = SecondComponent.UiState(
            text = name,
            color = Color.Black
        )
    )

    override val uiState = _uiState

    init {
        lifecycle.doOnDestroy { coroutine.cancel() }
    }

    override fun startColorAnimation() {
        colorAnimationJob = coroutine.launch {
            while (isActive) {
                delay(timeMillis = 800)
                _uiState.update {
                    it.copy(
                        color = colorRandom()
                    )
                }
            }
        }
    }

    private fun colorRandom(): Color {
        return Color(
            red = Random.nextInt(until = 255),
            green = Random.nextInt(until = 255),
            blue = Random.nextInt(until = 255),
            alpha = 255
        )
    }
}