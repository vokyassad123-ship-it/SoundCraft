package com.example.soundcraft.ui.categorias

import android.content.Intent
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.soundcraft.data.Categoria
import com.example.soundcraft.data.Pista
import com.example.soundcraft.data.RecursosRepository
import com.example.soundcraft.ui.components.EstadoCargando
import com.example.soundcraft.ui.components.EstadoMensaje
import com.example.soundcraft.ui.components.FilaPista
import com.example.soundcraft.ui.recursos.CatalogoUiState
import com.example.soundcraft.ui.theme.Negro
import com.example.soundcraft.ui.theme.RosaSoundCraft
import com.example.soundcraft.ui.theme.SoundCraftTheme
import com.example.soundcraft.ui.theme.TextoOscuro

/** Tipos de archivo que muestra el selector: solo WAV y MP3. */
private val TIPOS_AUDIO = arrayOf("audio/wav", "audio/x-wav", "audio/wave", "audio/mpeg", "audio/mp3")

/**
 * Detalle de una categoría (ej. EXPLOSIONES): encabezado rosa, botón "Agregar pista"
 * y la lista de sonidos. "Agregar pista" abre el selector de archivos del teléfono.
 */
@Composable
fun DetalleCategoriaScreen(
    categoria: Categoria,
    estado: CatalogoUiState,
    onVolver: () -> Unit,
    onPista: (Pista) -> Unit,
    onArchivoElegido: (nombreArchivo: String, ruta: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val selector = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
        if (uri != null) {
            // Pedimos permiso para seguir leyendo el archivo después (solo guardamos su ruta)
            runCatching {
                context.contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            val nombre = context.contentResolver.query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)
                ?.use { c -> if (c.moveToFirst()) c.getString(0) else null }
                ?: uri.lastPathSegment.orEmpty()
            onArchivoElegido(nombre, uri.toString())
        }
    }

    Column(modifier.fillMaxSize()) {
        Encabezado(categoria.nombre, onVolver, onAgregar = { selector.launch(TIPOS_AUDIO) })

        when (estado) {
            CatalogoUiState.Cargando -> EstadoCargando()
            is CatalogoUiState.Error -> EstadoMensaje(estado.mensaje, esError = true)
            CatalogoUiState.Vacio -> SinSonidos { selector.launch(TIPOS_AUDIO) }
            is CatalogoUiState.Exito -> {
                val pistas = estado.pistas.filter { it.categoriaId == categoria.id }
                if (pistas.isEmpty()) {
                    SinSonidos { selector.launch(TIPOS_AUDIO) }
                } else {
                    LazyColumn(Modifier.fillMaxSize()) {
                        items(pistas, key = { it.id }) { pista -> FilaPista(pista, onClick = { onPista(pista) }) }
                    }
                }
            }
        }
    }
}

@Composable
private fun SinSonidos(onAgregar: () -> Unit) {
    EstadoMensaje(
        mensaje = "Esta categoría todavía no tiene sonidos.\nAgrega un archivo WAV o MP3.",
        textoBoton = "AGREGAR PISTA",
        onBoton = onAgregar,
    )
}

/** Encabezado con degradado rosa → negro, flecha para volver y el nombre en grande. */
@Composable
private fun Encabezado(nombre: String, onVolver: () -> Unit, onAgregar: () -> Unit) {
    Box(
        Modifier
            .fillMaxWidth()
            .background(Brush.verticalGradient(listOf(RosaSoundCraft, Negro)))
            .statusBarsPadding()
            .height(170.dp),
    ) {
        IconButton(onClick = onVolver, modifier = Modifier.padding(4.dp)) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = Color.White)
        }
        Column(
            modifier = Modifier.align(Alignment.BottomStart).fillMaxWidth().padding(horizontal = 20.dp),
        ) {
            Text(
                text = nombre.uppercase(),
                fontSize = 34.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
            )
            Row(
                modifier = Modifier.align(Alignment.End).padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = "Agregar pista",
                    color = TextoOscuro,
                    fontSize = 13.sp,
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color.White)
                        .clickable(onClick = onAgregar)
                        .padding(horizontal = 14.dp, vertical = 4.dp),
                )
                IconButton(onClick = onAgregar) {
                    Icon(Icons.Filled.Add, contentDescription = "Agregar pista", tint = Color.White)
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000, heightDp = 800)
@Composable
private fun DetallePreview() {
    SoundCraftTheme {
        val repo = RecursosRepository()
        val explosiones = repo.categorias.first { it.id == "explosiones" }
        val pistas = listOf(
            Pista(1, "Boom", "Soundfree", "explosiones", ""),
            Pista(2, "Booooom", "Soundfree", "explosiones", ""),
            Pista(3, "Pum", "Soundfree", "explosiones", ""),
        )
        DetalleCategoriaScreen(explosiones, CatalogoUiState.Exito(repo.categorias, pistas), {}, {}, { _, _ -> })
    }
}
