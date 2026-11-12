package com.example.soundcraft.ui.perfil

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.soundcraft.ui.components.BotonPill
import com.example.soundcraft.ui.components.EstiloBoton
import com.example.soundcraft.ui.components.SoundCraftLogo
import com.example.soundcraft.ui.recursos.CatalogoUiState
import com.example.soundcraft.ui.theme.SoundCraftTheme

/** Perfil: quién inició sesión, cuántos sonidos tiene y el botón para salir. */
@Composable
fun PerfilScreen(
    usuario: String?,
    estado: CatalogoUiState,
    onCerrarSesion: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val cantidad = (estado as? CatalogoUiState.Exito)?.pistas?.size ?: 0

    Column(
        modifier = modifier.fillMaxSize().statusBarsPadding().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
    ) {
        SoundCraftLogo(tamano = 90.dp)
        Spacer(Modifier.height(16.dp))
        Text("Sesión iniciada como", color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(
            text = usuario ?: "invitado",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
        Text("$cantidad sonidos en tu portafolio", color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(16.dp))
        BotonPill("CERRAR SESIÓN", onCerrarSesion, estilo = EstiloBoton.Gris)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000, heightDp = 700)
@Composable
private fun PerfilPreview() {
    SoundCraftTheme { PerfilScreen("amira@correo.com", CatalogoUiState.Vacio, {}) }
}
