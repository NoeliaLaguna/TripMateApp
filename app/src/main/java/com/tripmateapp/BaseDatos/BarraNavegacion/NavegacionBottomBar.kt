package com.tripmateapp.BaseDatos.BarraNavegacion

import android.R.attr.content
import android.R.id.content
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.tripmateapp.BaseDatos.BarraNavegacion.ui.BottomBar

@Composable
fun NavegacionBottomBar(
    navController: NavHostController,
    content: @Composable (Modifier) -> Unit
) {
    Scaffold (
    bottomBar = {
        BottomBar(navController)
    }
    ) { paddingValues ->
        content(Modifier.padding(paddingValues))
    }
}
