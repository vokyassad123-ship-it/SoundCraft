package com.example.soundcraft.ui.panel

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.soundcraft.data.Categoria
import com.example.soundcraft.data.Pista
import com.example.soundcraft.data.RecursosRepository
import com.example.soundcraft.ui.components.EstadoCargando
import com.example.soundcraft.ui.components.EstadoMensaje
import com.example.soundcraft.ui.recursos.CatalogoUiState
import com.example.soundcraft.ui.recursos.ReproductorState
import com.example.soundcraft.ui.theme.GrisCampo
import com.example.soundcraft.ui.theme.Negro
import com.example.soundcraft.ui.theme.RosaSoundCraft
import com.example.soundcraft.ui.theme.SoundCraftTheme
import com.example.soundcraft.ui.theme.SuperficieOscura
import com.example.soundcraft.ui.theme.TextoOscuro

private const val PADS = 16 // grilla de 4 x 4

/** Acciones del panel, agrupadas para no pasar 10 parámetros sueltos. */
data class AccionesPanel(
    val onPad: (Pista) -> Unit = {},
    val onPadVacio: () -> Unit = {},
    val onPlay: () -> Unit = {},
    val onSaltar: (lista: List<Pista>, paso: Int) -> Unit = { _, _ -> },
    val onProgreso: (Float) -> Unit = {},
    val onAleatorio: () -> Unit = {},
    val onRepetir: () -> Unit = {},
    val onOpcionMenu: (String) -> Unit = {},
    val onReintentar: () -> Unit = {},
)

/**
 * Panel de reproducción: grilla 4x4 de pads, filtro por categoría (LazyRow),
 * menú de tres puntos y reproductor abajo con degradado rosa.
 */
@Composable
fun PanelScreen(
    estado: CatalogoUiState,
    reproductor: ReproductorState,
    acciones: AccionesPanel,
    modifier: Modifier = Modifier,
) {
    // "todas" o el id de una categoría: la grilla se llena según la categoría elegida
    var filtro by rememberSaveable { mutableStateOf("todas") }

    Column(modifier.fillMaxSize()) {
        BarraSuperior(acciones.onOpcionMenu)

        when (estado) {
            CatalogoUiState.Cargando -> Box(Modifier.weight(1f)) { EstadoCargando() }
            is CatalogoUiState.Error -> Box(Modifier.weight(1f)) {
                EstadoMensaje(estado.mensaje, esError = true, textoBoton = "REINTENTAR", onBoton = acciones.onReintentar)
            }
            CatalogoUiState.Vacio -> Box(Modifier.weight(1f)) { EstadoMensaje("Agrega sonidos desde una categoría") }
            is CatalogoUiState.Exito -> {
                val visibles = estado.pistas
                    .filter { filtro == "todas" || it.categoriaId == filtro }
                    .take(PADS)
                val colores = estado.categorias.associate { it.id to it.color }

                FiltrosCategoria(estado.categorias, filtro) { filtro = it }
                GrillaPads(
                    pistas = visibles,
                    colores = colores,
                    seleccionada = reproductor.pistaActual,
                    onPad = acciones.onPad,
                    onPadVacio = acciones.onPadVacio,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                )
                Spacer(Modifier.weight(1f))
                Reproductor(reproductor, acciones, visibles)
            }
        }
    }
}

@Composable
private fun BarraSuperior(onOpcion: (String) -> Unit) {
    var menuAbierto by rememberSaveable { mutableStateOf(false) }
    Box(Modifier.fillMaxWidth().statusBarsPadding()) {
        Box(Modifier.align(Alignment.TopEnd)) {
            IconButton(onClick = { menuAbierto = true }) {
                Icon(Icons.Filled.MoreVert, contentDescription = "Más opciones", tint = Color.White)
            }
            DropdownMenu(
                expanded = menuAbierto,
                onDismissRequest = { menuAbierto = false },
                containerColor = Color.White,
            ) {
                listOf("Seleccionar y editar", "Agregar a la fila", "Compartir").forEachIndexed { i, opcion ->
                    if (i > 0) HorizontalDivider(color = GrisCampo, modifier = Modifier.padding(horizontal = 12.dp))
                    DropdownMenuItem(
                        text = { Text(opcion, color = TextoOscuro) },
                        onClick = { menuAbierto = false; onOpcion(opcion) },
                    )
                }
            }
        }
    }
}

/** Fila deslizable de filtros: TODAS + una por categoría. */
@Composable
private fun FiltrosCategoria(categorias: List<Categoria>, filtro: String, onFiltro: (String) -> Unit) {
    val opciones = listOf("todas" to "Todas") + categorias.map { it.id to it.nombre }
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp),
    ) {
        items(opciones, key = { it.first }) { (id, nombre) ->
            val color = categorias.firstOrNull { it.id == id }?.color ?: GrisCampo
            FilterChip(
                selected = filtro == id,
                onClick = { onFiltro(id) },
                label = { Text(nombre.uppercase(), fontWeight = FontWeight.Bold, fontSize = 12.sp) },
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = SuperficieOscura,
                    labelColor = Color.White,
                    selectedContainerColor = color,
                    selectedLabelColor = TextoOscuro,
                ),
                border = null,
            )
        }
    }
}

/** 16 pads en 4 filas de 4. Si faltan sonidos, los pads quedan grises y vacíos. */
@Composable
private fun GrillaPads(
    pistas: List<Pista>,
    colores: Map<String, Color>,
    seleccionada: Pista?,
    onPad: (Pista) -> Unit,
    onPadVacio: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(10.dp)) {
        for (fila in 0 until 4) {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                for (columna in 0 until 4) {
                    val pista = pistas.getOrNull(fila * 4 + columna)
                    Pad(
                        pista = pista,
                        color = pista?.let { colores[it.categoriaId] } ?: SuperficieOscura,
                        seleccionado = pista != null && pista.id == seleccionada?.id,
                        onClick = { if (pista != null) onPad(pista) else onPadVacio() },
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }
    }
}

@Composable
private fun Pad(pista: Pista?, color: Color, seleccionado: Boolean, onClick: () -> Unit, modifier: Modifier) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(4.dp))
            .background(color)
            .then(if (seleccionado) Modifier.border(3.dp, Color.White, RoundedCornerShape(4.dp)) else Modifier)
            .clickable(onClick = onClick)
            .padding(6.dp),
        contentAlignment = Alignment.BottomStart,
    ) {
        if (pista != null) {
            Column {
                Text(
                    pista.nombre.uppercase(), color = TextoOscuro, fontSize = 10.sp,
                    fontWeight = FontWeight.ExtraBold, maxLines = 1, overflow = TextOverflow.Ellipsis,
                    lineHeight = 12.sp,
                )
                Text(
                    pista.autor.uppercase(), color = TextoOscuro, fontSize = 7.sp,
                    maxLines = 1, overflow = TextOverflow.Ellipsis, lineHeight = 9.sp,
                )
            }
        }
    }
}

/** Reproductor de abajo: título, autor, barra de progreso y controles. */
@Composable
private fun Reproductor(estado: ReproductorState, acciones: AccionesPanel, lista: List<Pista>) {
    Column(
        Modifier
            .fillMaxWidth()
            .background(Brush.verticalGradient(listOf(Negro, RosaSoundCraft)))
            .padding(horizontal = 24.dp, vertical = 16.dp),
    ) {
        Spacer(Modifier.size(24.dp))
        Text(
            text = estado.pistaActual?.nombre?.uppercase() ?: "ELIGE UN SONIDO",
            fontSize = 26.sp, fontWeight = FontWeight.ExtraBold, color = Color.White,
            maxLines = 1, overflow = TextOverflow.Ellipsis,
        )
        Text(
            text = estado.pistaActual?.autor?.uppercase().orEmpty(),
            fontSize = 16.sp, color = Color.White,
        )
        Slider(
            value = estado.progreso,
            onValueChange = acciones.onProgreso,
            colors = SliderDefaults.colors(
                thumbColor = Color.White,
                activeTrackColor = Color.White,
                inactiveTrackColor = Color.White.copy(alpha = 0.4f),
            ),
        )
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BotonControl(Icons.Filled.Shuffle, "Aleatorio", activo = estado.aleatorio, onClick = acciones.onAleatorio)
            BotonControl(Icons.Filled.SkipPrevious, "Anterior") { acciones.onSaltar(lista, -1) }
            IconButton(onClick = acciones.onPlay, modifier = Modifier.size(72.dp)) {
                Icon(
                    imageVector = if (estado.reproduciendo) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                    contentDescription = if (estado.reproduciendo) "Pausa" else "Reproducir",
                    tint = Color.White,
                    modifier = Modifier.size(64.dp),
                )
            }
            BotonControl(Icons.Filled.SkipNext, "Siguiente") { acciones.onSaltar(lista, 1) }
            BotonControl(Icons.Filled.Repeat, "Repetir", activo = estado.repetir, onClick = acciones.onRepetir)
        }
    }
}

@Composable
private fun BotonControl(icono: ImageVector, descripcion: String, activo: Boolean = true, onClick: () -> Unit) {
    IconButton(onClick = onClick, modifier = Modifier.size(52.dp)) {
        Icon(
            icono, contentDescription = descripcion,
            tint = if (activo) Color.White else Color.White.copy(alpha = 0.5f),
            modifier = Modifier.size(32.dp),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000, heightDp = 800)
@Composable
private fun PanelPreview() {
    SoundCraftTheme {
        val repo = RecursosRepository()
        val pistas = List(14) { i -> Pista(i.toLong(), "Sonido ${i + 1}", "Soundfree", repo.categorias[i % 10].id, "") }
        PanelScreen(
            estado = CatalogoUiState.Exito(repo.categorias, pistas),
            reproductor = ReproductorState(pistaActual = pistas[0], progreso = 0.85f),
            acciones = AccionesPanel(),
        )
    }
}
