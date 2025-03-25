package com.example.second

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.subscribe
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.example.second.SecondComponent.UiState
import kotlinx.coroutines.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
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
            ColorAnimationWorker(
                initialColor = stateKeeper.consume(
                    key = COLOR_KEY,
                    strategy = ColorSerializer
                ) ?: Color.Black
            )
        }

    private val _uiState = MutableValue(
        initialValue = UiState(
            text = name,
            color = colorAnimationWorker.color.value
        )
    )

    override val uiState: Value<UiState> = _uiState

    init {
        colorAnimationWorker.color.subscribe(
            lifecycle = lifecycle
        ) { color ->
            _uiState.update {
                it.copy(
                    color = color
                )
            }
        }

        stateKeeper.register(
            key = COLOR_KEY,
            strategy = ColorSerializer,
            supplier = {
                colorAnimationWorker.color.value
            }
        )
    }

    override fun startColorAnimation() {
        colorAnimationWorker()
    }

    class ColorAnimationWorker(
        initialColor: Color = Color.Black
    ) : InstanceKeeper.Instance {

        private val coroutine = CoroutineScope(Dispatchers.Default)
        private var job: Job? = null

        val color = MutableValue(initialColor)

        operator fun invoke() {
            job = coroutine.launch {
                while (isActive) {
                    delay(timeMillis = 800)

                    color.value = colorRandom()
                }
            }
        }

        override fun onDestroy() {
            coroutine.cancel()
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

    companion object {
        private const val COLOR_KEY = "COLOR_KEY"
    }
}

object ColorSerializer : KSerializer<Color> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(
        serialName = "my.app.Color",
        kind = PrimitiveKind.STRING
    )

    override fun serialize(encoder: Encoder, value: Color) {
        val string = value.toArgb()
            .toString(radix = 16)
            .padStart(length = 6, padChar = '0')

        encoder.encodeString(string)
    }

    override fun deserialize(decoder: Decoder): Color {
        val string = decoder.decodeString()
        return Color(string.toInt(radix = 16))
    }
}