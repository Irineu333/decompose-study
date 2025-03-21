package com.example.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.example.first.First
import com.example.second.Second

@Composable
fun App(
    component: AppComponent
) = MaterialTheme {
    Children(
        stack = component.stack,
        modifier = Modifier.fillMaxSize(),
        animation = stackAnimation(fade()),
    ) {
        when (val child = it.instance) {
            is AppComponent.Child.First -> {
                First(child.component)
            }

            is AppComponent.Child.Second -> {
                Second(child.component)
            }
        }
    }
}