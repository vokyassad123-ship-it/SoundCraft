package com.example.soundcraft.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.soundcraft.ui.theme.SoundCraftTheme

/** Logo de la app: la "S" violeta y el nombre debajo. */
@Composable
fun SoundCraftLogo(modifier: Modifier = Modifier, tamanoS: Int = 96) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "S",
            color = MaterialTheme.colorScheme.primary,
            fontSize = tamanoS.sp,
            fontWeight = FontWeight.Black,
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = "SOUNDCRAFT",
            color = MaterialTheme.colorScheme.primary,
            fontSize = (tamanoS / 4).sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 1.sp,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun SoundCraftLogoPreview() {
    SoundCraftTheme { SoundCraftLogo() }
}
