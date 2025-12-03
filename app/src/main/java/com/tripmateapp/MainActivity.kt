package com.tripmateapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val viewModel: MainViewModel = viewModel()

                HelloWorldScreen(viewModel)
            }
        }
    }
}

@Composable
fun HelloWorldScreen(viewModel: MainViewModel) {
    val message by viewModel.message.collectAsState()
    var inputText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .padding(20.dp)
    ) {

        TextField(
            value = inputText,
            onValueChange = { inputText = it },
            label = { Text("Escribe un mensaje") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            //viewModel.saveMessage(inputText)
            inputText = ""
        }) {
            Text("Guardar en Room")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Último mensaje guardado:",
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
