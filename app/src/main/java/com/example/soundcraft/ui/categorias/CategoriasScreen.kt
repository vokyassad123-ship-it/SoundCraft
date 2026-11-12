package com.example.soundcraft.ui.categorias

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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.soundcraft.data.Categoria
import com.example.soundcraft.data.RecursosRepository
import com.example.soundcraft.ui.components.EstadoCargando
import com.example.soundcraft.ui.components.EstadoMensaje
import com.example.soundcraft.ui.recursos.CatalogoUiState
import com.example.soundcraft.ui.theme.SoundCraftTheme
import com.example.soundcraft.ui.theme.TextoOscuro

/**
 * Inicio: barra "Buscar audios..." y grilla de 2 columnas con una tarjeta por categoría.
 * Tocar la barra lleva a la pestaña Buscar.
 */
@Composable
fun CategoriasScreen(
    estado: CatalogoUiState,
    onBuscar: () -> Unit,
    onCategoria: (Categoria) -> Unit,
    onReintentar: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier.fillMaxSize().statusBarsPadding().padding(horizontal = 16.dp)) {
        BarraBuscarFalsa(onClick = onBuscar, modifier = Modifier.padding(vertical = 16.dp))

        when (estado) {
            CatalogoUiState.Cargando -> EstadoCargando()
            is CatalogoUiState.Error -> EstadoMensaje(estado.mensaje, esError = true, textoBoton = "REINTENTAR", onBoton = onReintentar)
            CatalogoUiState.Vacio -> EstadoMensaje("Todavía no hay sonidos guardados")
            is CatalogoUiState.Exito -> GrillaCategorias(estado.categorias, onCategoria)
        }
    }
}

@Composable
private fun GrillaCategorias(categorias: List<Categoria>, onCategoria: (Categoria) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier.fillMaxSize(),
    ) {
        items(categorias, key = { it.id }) { categoria ->
            TarjetaCategoria(categoria) { onCategoria(categoria) }
        }
    }
}

/** Tarjeta de color con el nombre en mayúsculas. */
@Composable
fun TarjetaCategoria(categoria: Categoria, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(96.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(categoria.color)
            .clickable(onClick = onClick)
            .padding(8.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = categoria.nombre.uppercase(),
            color = TextoOscuro,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 17.sp,
            textAlign = TextAlign.Center,
            maxLines = 2,
        )
    }
}

/** Parece un campo de búsqueda, pero solo lleva a la pestaña Buscar. */
@Composable
fun BarraBuscarFalsa(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(Color.White)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Icon(Icons.Filled.Search, contentDescription = null, tint = TextoOscuro)
        Text("Buscar audios...", color = TextoOscuro.copy(alpha = 0.6f))
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000, heightDp = 800)
@Composable
private fun CategoriasPreview() {
    SoundCraftTheme {
        CategoriasScreen(
            estado = CatalogoUiState.Exito(RecursosRepository().categorias, emptyList()),
            onBuscar = {}, onCategoria = {}, onReintentar = {},
        )
    }
}
