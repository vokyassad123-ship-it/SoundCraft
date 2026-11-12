package com.example.soundcraft.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.soundcraft.ui.components.SoundCraftLogo
import com.example.soundcraft.ui.theme.SoundCraftTheme

/**
 * Inicio provisorio: confirma que la sesión se inició.
 * Más adelante se reemplaza por Categorías + Panel 4x4 con la barra inferior.
 */
@Composable
fun HomeScreen(usuario: String?, onLogout: () -> Unit) {
    Scaffold { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        ) {
            SoundCraftLogo()
            Text(
                text = "Sesión iniciada como\n${usuario ?: "invitado"}",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge,
            )
            Text(
                text = "Próximamente: categorías y panel 4x4",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            OutlinedButton(onClick = onLogout) { Text("Cerrar sesión") }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun HomePreview() {
    SoundCraftTheme { HomeScreen("amira@correo.com") {} }
}
