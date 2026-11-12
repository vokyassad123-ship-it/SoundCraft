package com.example.soundcraft.data

import com.example.soundcraft.ui.theme.AguaFunk
import com.example.soundcraft.ui.theme.AmarilloBeats
import com.example.soundcraft.ui.theme.CelesteInstrumentos
import com.example.soundcraft.ui.theme.LimaPop
import com.example.soundcraft.ui.theme.MagentaRiser
import com.example.soundcraft.ui.theme.NaranjaRock
import com.example.soundcraft.ui.theme.RojoVoces
import com.example.soundcraft.ui.theme.RosaAmbiente
import com.example.soundcraft.ui.theme.VerdeJazz
import com.example.soundcraft.ui.theme.VioletaExplosiones
import kotlinx.coroutines.delay

/**
 * Fuente de datos de SoundCraft.
 * Por ahora guarda todo EN MEMORIA con datos de ejemplo (se pierde al cerrar la app).
 * Más adelante se puede cambiar por una base de datos local (Room) sin tocar las pantallas.
 */
class RecursosRepository {

    val categorias: List<Categoria> = listOf(
        Categoria("rock", "Rock", NaranjaRock),
        Categoria("explosiones", "Explosiones", VioletaExplosiones),
        Categoria("pop", "Pop", LimaPop),
        Categoria("ambiente", "Ambiente", RosaAmbiente),
        Categoria("funk", "Funk", AguaFunk),
        Categoria("voces", "Voces", RojoVoces),
        Categoria("jazz", "Jazz", VerdeJazz),
        Categoria("beats", "Beats", AmarilloBeats),
        Categoria("instrumentos", "Instrumentos", CelesteInstrumentos),
        Categoria("riser", "Riser", MagentaRiser),
    )

    private var siguienteId = 1L
    private fun ejemplo(nombre: String, autor: String, categoria: String) =
        Pista(siguienteId++, nombre, autor, categoria, "ejemplo/${nombre.lowercase().replace(" ", "_")}.wav")

    // El orden de las primeras 16 coincide con el panel 4x4 del diseño
    private val pistas = mutableListOf(
        ejemplo("Lluvia 1", "Ambient", "ambiente"),
        ejemplo("Viento 3", "Ambient", "ambiente"),
        ejemplo("Lluvia", "Ambient", "ambiente"),
        ejemplo("Cascada", "Ambient", "ambiente"),
        ejemplo("Guitarra 1", "Instrumentos", "instrumentos"),
        ejemplo("Violín 3", "Instrumentos", "instrumentos"),
        ejemplo("Her Eyes", "Charli Jeer", "pop"),
        ejemplo("Como Alí", "Los Piojos", "rock"),
        ejemplo("Guitarra 2", "Instrumentos", "instrumentos"),
        ejemplo("Boom", "Soundfree", "explosiones"),
        ejemplo("Pum", "Soundfree", "explosiones"),
        ejemplo("Boom Clap", "Soundfree", "explosiones"),
        ejemplo("Grito Llorón", "Voces", "voces"),
        ejemplo("Susurro 1", "Voces", "voces"),
        ejemplo("Livin la Vida Loca", "Ricky Martin", "pop"),
        ejemplo("Cypher Pt3", "BTS", "beats"),
        ejemplo("Booooom", "Soundfree", "explosiones"),
        ejemplo("Puuuum", "Soundfree", "explosiones"),
        ejemplo("Pum Pum", "Soundfree", "explosiones"),
        ejemplo("Groove 1", "Soundfree", "funk"),
        ejemplo("Riser 1", "Soundfree", "riser"),
        // "jazz" queda vacía a propósito, para mostrar el estado "vacío"
    )

    /** Simula leer los datos guardados (tarda un poco, como una base de datos real). */
    suspend fun cargarPistas(): List<Pista> {
        delay(600)
        return pistas.toList()
    }

    /** Guarda una pista nueva: solo ruta, nombre y clasificación. */
    fun agregarPista(nombre: String, ruta: String, categoriaId: String): Pista {
        val pista = Pista(siguienteId++, nombre, "Mis sonidos", categoriaId, ruta)
        pistas.add(pista)
        return pista
    }
}
