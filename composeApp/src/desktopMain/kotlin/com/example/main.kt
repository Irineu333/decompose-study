package com.example

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.extensions.compose.lifecycle.LifecycleController
import com.arkivanov.essenty.lifecycle.LifecycleRegistry

fun main() {

    val lifecycle = LifecycleRegistry()

    val appComponent = runOnUiThread {
        DefaultAppComponent(
            context = DefaultComponentContext(
                lifecycle = lifecycle
            ),
        )
    }

    application {

        val windowState = rememberWindowState()

        Window(
            onCloseRequest = ::exitApplication,
            title = "decompose-study",
        ) {
            LifecycleController(lifecycle, windowState)

            App(appComponent)
        }
    }
}

@Preview
@Composable
fun AppDesktopPreview() {
    App(DefaultAppComponent(PreviewComponentContext))
}