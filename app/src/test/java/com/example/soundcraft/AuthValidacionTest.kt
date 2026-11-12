package com.example.soundcraft

import com.example.soundcraft.ui.auth.AuthViewModel
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/** Pruebas rápidas de las reglas de validación (se corren sin emulador). */
class AuthValidacionTest {

    @Test
    fun emailValido() {
        assertTrue(AuthViewModel.esEmailValido("amira@correo.com"))
        assertTrue(AuthViewModel.esEmailValido("  ana.perez@unlar.edu.ar "))
    }

    @Test
    fun emailInvalido() {
        assertFalse(AuthViewModel.esEmailValido(""))
        assertFalse(AuthViewModel.esEmailValido("amira"))
        assertFalse(AuthViewModel.esEmailValido("amira@correo"))
    }

    @Test
    fun loginAceptaCorreoOUsuario() {
        assertTrue(AuthViewModel.esEmailOUsuarioValido("amira@correo.com"))
        assertTrue(AuthViewModel.esEmailOUsuarioValido("amira_assad"))
        assertFalse(AuthViewModel.esEmailOUsuarioValido("am"))
        assertFalse(AuthViewModel.esEmailOUsuarioValido("amira assad"))
    }

    @Test
    fun passwordMinimoSeisCaracteres() {
        assertFalse(AuthViewModel.esPasswordValida("12345"))
        assertTrue(AuthViewModel.esPasswordValida("123456"))
    }
}
