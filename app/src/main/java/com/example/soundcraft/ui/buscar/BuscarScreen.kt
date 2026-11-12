package com.example.soundcraft.ui.buscar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.soundcraft.data.Pista
import com.example.soundcraft.data.RecursosRepository
import com.example.soundcraft.ui.components.EstadoCargando
import com.example.soundcraft.ui.components.EstadoMensaje
import com.example.soundcraft.ui.components.FilaPista
import com.example.soundcraft.ui.recursos.CatalogoUiState
import com.example.soundcraft.ui.theme.SoundCraftTheme
import com.example.soundcraft.ui.theme.TextoOscuro

/** Buscar: escribe y filtra los sonidos por nombre, autor o categoría (recuperación rápida). */
@Composable
fun BuscarScreen(
    estado: CatalogoUiState,
    onPista: (Pista) -> Unit,
    modifier: Modifier = Modifier,
    enfocarAlAbrir: Boolean = true,
) {
    var texto by rememberSaveable { mutableStateOf("") }
    val foco = remember { FocusRequester() }
    LaunchedEffect(Unit) { if (enfocarAlAbrir) foco.requestFocus() }

    Column(modifier.fillMaxSize().statusBarsPadding()) {
        TextField(
            value = texto,
            onValueChange = { texto = it },
            placeholder = { Text("Buscar audios...") },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
            singleLine = true,
            shape = RoundedCornerShape(6.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedTextColor = TextoOscuro,
                unfocusedTextColor = TextoOscuro,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedLeadingIconColor = TextoOscuro,
                unfocusedLeadingIconColor = TextoOscuro,
                focusedPlaceholderColor = TextoOscuro.copy(alpha = 0.6f),
                unfocusedPlaceholderColor = TextoOscuro.copy(alpha = 0.6f),
            ),
            modifier = Modifier.fillMaxWidth().padding(16.dp).focusRequester(foco),
        )

        when (estado) {
            CatalogoUiState.Cargando -> EstadoCargando()
            is CatalogoUiState.Error -> EstadoMensaje(estado.mensaje, esError = true)
            CatalogoUiState.Vacio -> EstadoMensaje("Todavía no hay sonidos guardados")
            is CatalogoUiState.Exito -> {
                val consulta = texto.trim()
                val nombresCategoria = estado.categorias.associate { it.id to it.nombre }
                val resultados = estado.pistas.filter { pista ->
                    consulta.isEmpty() ||
                        pista.nombre.contains(consulta, ignoreCase = true) ||
                        pista.autor.contains(consulta, ignoreCase = true) ||
                        nombresCategoria[pista.categoriaId].orEmpty().contains(consulta, ignoreCase = true)
                }
                if (resultados.isEmpty()) {
                    EstadoMensaje("No encontramos sonidos con \"$consulta\"")
                } else {
                    LazyColumn(Modifier.fillMaxSize()) {
                        items(resultados, key = { it.id }) { pista -> FilaPista(pista, onClick = { onPista(pista) }) }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000, heightDp = 700)
@Composable
private fun BuscarPreview() {
    SoundCraftTheme {
        val repo = RecursosRepository()
        BuscarScreen(
            estado = CatalogoUiState.Exito(repo.categorias, emptyList()),
            onPista = {},
            enfocarAlAbrir = false,
        )
    }
}
