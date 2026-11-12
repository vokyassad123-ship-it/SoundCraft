package com.example.soundcraft.data

import androidx.compose.ui.graphics.Color

/** Una categoría del catálogo (ej. Explosiones). El color se usa en tarjetas y pads. */
data class Categoria(
    val id: String,
    val nombre: String,
    val color: Color,
)

/**
 * Un recurso sonoro. Según los requerimientos solo se guarda:
 * la ruta física del archivo, el nombre y la clasificación (categoría).
 * "autor" es el subtítulo que muestra el diseño (ej. SOUNDFREE).
 */
data class Pista(
    val id: Long,
    val nombre: String,
    val autor: String,
    val categoriaId: String,
    val ruta: String,
)
