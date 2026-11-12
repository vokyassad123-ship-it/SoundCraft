package com.example.soundcraft.ui.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.soundcraft.ui.components.SoundCraftLogo
import com.example.soundcraft.ui.theme.SoundCraftTheme
import kotlinx.coroutines.delay

/** Pantalla de bienvenida: el logo grande durante un momento y después sigue sola. */
@Composable
fun SplashScreen(onTerminar: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(1500)
        onTerminar()
    }
    Box(
        Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center,
    ) {
        SoundCraftLogo(tamano = 140.dp)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000, heightDp = 700)
@Composable
private fun SplashPreview() {
    SoundCraftTheme { SplashScreen {} }
}
