package com.example.second

import androidx.compose.ui.graphics.Color
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.subscribe
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.instancekeeper.getOrCreate
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

    private val colorAnimationWorker =
        instanceKeeper.getOrCreate {
            ColorAnimationWorker()
        }

    private val _uiState = MutableValue(
        initialValue = SecondComponent.UiState(
            text = name,
            color = colorAnimationWorker.color.value
        )
    )

    override val uiState = _uiState

    init {
        colorAnimationWorker.color.subscribe(
            lifecycle = lifecycle
        ) { color ->
            uiState.update {
                it.copy(
                    color = color
                )
            }
        }
    }

    override fun startColorAnimation() { colorAnimationWorker() }

    class ColorAnimationWorker : InstanceKeeper.Instance {

        private val coroutine = CoroutineScope(Dispatchers.Default)
        private var job: Job? = null

        val color = MutableValue(Color.Black)

        operator fun invoke() {
            job = coroutine.launch {
                while (isActive) {
                    delay(timeMillis = 800)

                    color.value = colorRandom()
                }
            }
        }

        override fun onDestroy() { coroutine.cancel() }

        private fun colorRandom(): Color {
            return Color(
                red = Random.nextInt(until = 255),
                green = Random.nextInt(until = 255),
                blue = Random.nextInt(until = 255),
                alpha = 255
            )
        }
    }
}