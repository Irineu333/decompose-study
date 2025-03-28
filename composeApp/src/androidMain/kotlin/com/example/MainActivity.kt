package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.arkivanov.decompose.defaultComponentContext
import com.example.app.App
import com.example.app.DefaultAppComponent

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appComponent = DefaultAppComponent(
            context = defaultComponentContext(),
        )

        setContent {
            App(appComponent)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppAndroidPreview() {
    App(DefaultAppComponent(PreviewComponentContext))
}