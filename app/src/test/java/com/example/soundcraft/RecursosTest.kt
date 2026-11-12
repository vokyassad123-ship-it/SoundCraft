package com.example.soundcraft

import com.example.soundcraft.ui.recursos.RecursosViewModel
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/** Regla de negocio: solo se aceptan archivos WAV o MP3. */
class RecursosTest {

    @Test
    fun aceptaWavYMp3() {
        assertTrue(RecursosViewModel.esArchivoPermitido("lluvia.wav"))
        assertTrue(RecursosViewModel.esArchivoPermitido("Boom.MP3"))
    }

    @Test
    fun rechazaOtrosFormatos() {
        assertFalse(RecursosViewModel.esArchivoPermitido("cancion.ogg"))
        assertFalse(RecursosViewModel.esArchivoPermitido("video.mp4"))
        assertFalse(RecursosViewModel.esArchivoPermitido("sin_extension"))
    }
}
