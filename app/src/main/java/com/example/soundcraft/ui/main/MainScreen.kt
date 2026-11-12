package com.example.soundcraft.ui.main

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.soundcraft.ui.buscar.BuscarScreen
import com.example.soundcraft.ui.categorias.CategoriasScreen
import com.example.soundcraft.ui.categorias.DetalleCategoriaScreen
import com.example.soundcraft.ui.components.EstadoCargando
import com.example.soundcraft.ui.panel.AccionesPanel
import com.example.soundcraft.ui.panel.PanelScreen
import com.example.soundcraft.ui.perfil.PerfilScreen
import com.example.soundcraft.ui.recursos.CatalogoUiState
import com.example.soundcraft.ui.recursos.RecursosViewModel
import com.example.soundcraft.ui.theme.GrisCampo
import kotlinx.coroutines.launch

/** Las 4 pestañas de la barra inferior del diseño. */
private enum class Pestana(val ruta: String, val icono: ImageVector, val descripcion: String) {
    Inicio("inicio", Icons.Filled.Home, "Inicio"),
    Buscar("buscar", Icons.Filled.Search, "Buscar"),
    Reproducir("panel", Icons.Filled.PlayArrow, "Panel de reproducción"),
    Perfil("perfil", Icons.Filled.Person, "Perfil"),
}

private const val RUTA_DETALLE = "categoria/{id}"

/**
 * Parte principal de la app (después de iniciar sesión):
 * barra inferior blanca + las pantallas de cada pestaña.
 */
@Composable
fun MainScreen(
    usuario: String?,
    onCerrarSesion: () -> Unit,
    viewModel: RecursosViewModel = viewModel(),
) {
    val nav = rememberNavController()
    val catalogo by viewModel.catalogo.collectAsState()
    val reproductor by viewModel.reproductor.collectAsState()

    val snackbar = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val mostrar: (String) -> Unit = { mensaje -> scope.launch { snackbar.showSnackbar(mensaje) } }

    val entradaActual by nav.currentBackStackEntryAsState()
    val rutaActual = entradaActual?.destination?.route

    // Cambiar de pestaña sin apilar pantallas repetidas
    val irA: (String) -> Unit = { ruta ->
        nav.navigate(ruta) {
            popUpTo(nav.graph.findStartDestination().id) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        snackbarHost = { SnackbarHost(snackbar) },
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                Pestana.entries.forEach { pestana ->
                    // El detalle de categoría cuenta como "Inicio"
                    val seleccionada = rutaActual == pestana.ruta ||
                        (pestana == Pestana.Inicio && rutaActual == RUTA_DETALLE)
                    NavigationBarItem(
                        selected = seleccionada,
                        onClick = { irA(pestana.ruta) },
                        icon = { Icon(pestana.icono, contentDescription = pestana.descripcion) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.Black,
                            unselectedIconColor = Color.Black,
                            indicatorColor = GrisCampo,
                        ),
                    )
                }
            }
        },
    ) { padding ->
        NavHost(nav, startDestination = Pestana.Inicio.ruta, modifier = Modifier.padding(padding)) {
            composable(Pestana.Inicio.ruta) {
                CategoriasScreen(
                    estado = catalogo,
                    onBuscar = { irA(Pestana.Buscar.ruta) },
                    onCategoria = { nav.navigate("categoria/${it.id}") },
                    onReintentar = viewModel::cargar,
                )
            }
            composable(Pestana.Buscar.ruta) {
                BuscarScreen(catalogo, onPista = { viewModel.reproducir(it); irA(Pestana.Reproducir.ruta) })
            }
            composable(Pestana.Reproducir.ruta) {
                PanelScreen(
                    estado = catalogo,
                    reproductor = reproductor,
                    acciones = AccionesPanel(
                        onPad = viewModel::reproducir,
                        onPadVacio = { mostrar("Agrega más sonidos desde una categoría") },
                        onPlay = viewModel::alternarPlay,
                        onSaltar = viewModel::saltar,
                        onProgreso = viewModel::moverProgreso,
                        onAleatorio = viewModel::alternarAleatorio,
                        onRepetir = viewModel::alternarRepetir,
                        onOpcionMenu = { opcion -> mostrar("$opcion: próximamente") },
                        onReintentar = viewModel::cargar,
                    ),
                )
            }
            composable(Pestana.Perfil.ruta) {
                PerfilScreen(usuario, catalogo, onCerrarSesion)
            }
            composable(RUTA_DETALLE) { entrada ->
                val id = entrada.arguments?.getString("id")
                val categoria = (catalogo as? CatalogoUiState.Exito)?.categorias?.firstOrNull { it.id == id }
                if (categoria == null) {
                    EstadoCargando()
                } else {
                    DetalleCategoriaScreen(
                        categoria = categoria,
                        estado = catalogo,
                        onVolver = { nav.popBackStack() },
                        onPista = { viewModel.reproducir(it); irA(Pestana.Reproducir.ruta) },
                        onArchivoElegido = { nombre, ruta ->
                            val error = viewModel.agregarPista(nombre, ruta, categoria.id)
                            mostrar(error ?: "\"$nombre\" se agregó a ${categoria.nombre}")
                        },
                    )
                }
            }
        }
    }
}
