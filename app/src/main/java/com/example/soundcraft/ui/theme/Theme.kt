package com.example.soundcraft.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

/**
 * SoundCraft usa siempre tema oscuro de alto contraste,
 * sin importar cómo tenga configurado el teléfono el usuario.
 */
private val SoundCraftColorScheme = darkColorScheme(
    primary = VioletaSoundCraft,
    onPrimary = Blanco,
    secondary = RosaSoundCraft,
    onSecondary = Blanco,
    tertiary = CelesteInstrumentos,
    background = Negro,
    onBackground = Blanco,
    surface = Negro,
    onSurface = Blanco,
    surfaceVariant = SuperficieOscura,
    onSurfaceVariant = GrisTexto,
    outline = GrisTexto,
    error = RojoError,
    onError = Negro,
)

@Composable
fun SoundCraftTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = SoundCraftColorScheme,
        typography = Typography,
        content = content
    )
}
