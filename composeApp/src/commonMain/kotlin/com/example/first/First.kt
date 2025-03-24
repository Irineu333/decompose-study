package com.example.first

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState

@Composable
fun First(
    component: FirstComponent,
) = Column(
    modifier = Modifier.fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(
        space = 8.dp,
        alignment = Alignment.CenterVertically
    )
) {
    val name by component.name.subscribeAsState()

    TextField(
        label = {
            Text(text = "What is your name?")
        },
        value = name,
        onValueChange = {
            component.onChangeName(it)
        }
    )

    Button(
        onClick = {
            component.navigateToSecond()
        },
        enabled = name.isNotBlank()
    ) {
        Text(text = "Say Hello")
    }
}