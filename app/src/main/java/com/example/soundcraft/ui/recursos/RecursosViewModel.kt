package com.example.soundcraft.ui.recursos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soundcraft.data.Categoria
import com.example.soundcraft.data.Pista
import com.example.soundcraft.data.RecursosRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** Estados del catálogo de sonidos: cargando, éxito, vacío o error. */
sealed interface CatalogoUiState {
    data object Cargando : CatalogoUiState
    data class Exito(val categorias: List<Categoria>, val pistas: List<Pista>) : CatalogoUiState
    data object Vacio : CatalogoUiState
    data class Error(val mensaje: String) : CatalogoUiState
}

/** Estado del reproductor de abajo del panel 4x4 (simulado: todavía no suena). */
data class ReproductorState(
    val pistaActual: Pista? = null,
    val reproduciendo: Boolean = false,
    val progreso: Float = 0f,
    val aleatorio: Boolean = false,
    val repetir: Boolean = false,
)

/** Lógica del dominio "Gestión de recursos": listar, catalogar y reproducir. */
class RecursosViewModel(
    private val repositorio: RecursosRepository = RecursosRepository(),
) : ViewModel() {

    private val _catalogo = MutableStateFlow<CatalogoUiState>(CatalogoUiState.Cargando)
    val catalogo: StateFlow<CatalogoUiState> = _catalogo.asStateFlow()

    private val _reproductor = MutableStateFlow(ReproductorState())
    val reproductor: StateFlow<ReproductorState> = _reproductor.asStateFlow()

    init {
        cargar()
    }

    fun cargar() {
        viewModelScope.launch {
            _catalogo.value = CatalogoUiState.Cargando
            _catalogo.value = try {
                val pistas = repositorio.cargarPistas()
                if (pistas.isEmpty()) {
                    CatalogoUiState.Vacio
                } else {
                    // La primera pista queda lista en el reproductor
                    if (_reproductor.value.pistaActual == null) {
                        _reproductor.update { it.copy(pistaActual = pistas.first()) }
                    }
                    CatalogoUiState.Exito(repositorio.categorias, pistas)
                }
            } catch (e: Exception) {
                CatalogoUiState.Error("No se pudieron cargar los sonidos")
            }
        }
    }

    /**
     * Cataloga un archivo nuevo. Devuelve un mensaje de error, o null si salió bien.
     * Regla: solo WAV o MP3.
     */
    fun agregarPista(nombreArchivo: String, ruta: String, categoriaId: String): String? {
        if (!esArchivoPermitido(nombreArchivo)) return "Solo se aceptan archivos WAV o MP3"
        val nombre = nombreArchivo.substringBeforeLast('.').ifBlank { "Sonido nuevo" }
        val nueva = repositorio.agregarPista(nombre, ruta, categoriaId)
        _catalogo.update { estado ->
            when (estado) {
                is CatalogoUiState.Exito -> estado.copy(pistas = estado.pistas + nueva)
                else -> CatalogoUiState.Exito(repositorio.categorias, listOf(nueva))
            }
        }
        return null
    }

    fun reproducir(pista: Pista) {
        _reproductor.update { it.copy(pistaActual = pista, reproduciendo = true, progreso = 0f) }
    }

    fun alternarPlay() = _reproductor.update { it.copy(reproduciendo = !it.reproduciendo) }
    fun moverProgreso(valor: Float) = _reproductor.update { it.copy(progreso = valor) }
    fun alternarAleatorio() = _reproductor.update { it.copy(aleatorio = !it.aleatorio) }
    fun alternarRepetir() = _reproductor.update { it.copy(repetir = !it.repetir) }

    /** Pasa a la siguiente (o anterior) pista dentro de la lista que se ve en el panel. */
    fun saltar(lista: List<Pista>, paso: Int) {
        if (lista.isEmpty()) return
        val actual = lista.indexOf(_reproductor.value.pistaActual)
        val nuevo = Math.floorMod(actual + paso, lista.size)
        reproducir(lista[nuevo])
    }

    companion object {
        fun esArchivoPermitido(nombre: String): Boolean {
            val extension = nombre.substringAfterLast('.', "").lowercase()
            return extension == "wav" || extension == "mp3"
        }
    }
}
