package com.example.soundcraft.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.soundcraft.R
import com.example.soundcraft.ui.theme.SoundCraftTheme

/** Solo el ícono "S" de SoundCraft (vector en res/drawable/ic_soundcraft_logo.xml). */
@Composable
fun LogoIcono(modifier: Modifier = Modifier, tamano: Dp = 56.dp) {
    Image(
        painter = painterResource(R.drawable.ic_soundcraft_logo),
        contentDescription = "Logo de SoundCraft",
        modifier = modifier.size(tamano),
    )
}

/** Logo grande vertical: ícono arriba y SOUNDCRAFT abajo (pantalla de bienvenida). */
@Composable
fun SoundCraftLogo(modifier: Modifier = Modifier, tamano: Dp = 120.dp) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        LogoIcono(tamano = tamano)
        Spacer(Modifier.height(16.dp))
        Text(
            text = "SOUNDCRAFT",
            color = MaterialTheme.colorScheme.primary,
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
        )
    }
}

/** Logo horizontal: ícono a la izquierda y SOUNDCRAFT a la derecha (login y recuperar). */
@Composable
fun SoundCraftLogoHorizontal(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        LogoIcono(tamano = 48.dp)
        Text(
            text = "SOUNDCRAFT",
            color = MaterialTheme.colorScheme.primary,
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
        )
    }
}

/** Encabezado del registro: ícono + "ÚNETE A" en blanco y "SOUNDCRAFT" en violeta. */
@Composable
fun SoundCraftLogoUnete(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        LogoIcono(tamano = 48.dp)
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "ÚNETE A",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium,
            )
            Text(
                text = "SOUNDCRAFT",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun LogosPreview() {
    SoundCraftTheme {
        Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
            SoundCraftLogo()
            SoundCraftLogoHorizontal()
            SoundCraftLogoUnete()
        }
    }
}
