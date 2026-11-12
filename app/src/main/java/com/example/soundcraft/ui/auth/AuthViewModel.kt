package com.example.soundcraft.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Lógica de autenticación de SoundCraft.
 *
 * Por ahora es SIMULADA: no hay servidor. Se espera un momento (como si
 * consultara internet) y responde. Más adelante se puede reemplazar el
 * interior de estas funciones por Firebase Auth o una API REST sin tocar
 * las pantallas.
 */
class AuthViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    /** Correo del usuario con sesión iniciada (null = nadie conectado). */
    private val _usuarioActual = MutableStateFlow<String?>(null)
    val usuarioActual: StateFlow<String?> = _usuarioActual.asStateFlow()

    fun login(email: String, password: String) {
        val error = validarEmail(email) ?: validarPassword(password)
        if (error != null) {
            _uiState.value = AuthUiState.Error(error)
            return
        }
        simularPeticion {
            _usuarioActual.value = email.trim()
            AuthUiState.Success("¡Hola de nuevo!")
        }
    }

    fun register(name: String, email: String, password: String) {
        val error = when {
            name.isBlank() -> "Escribe tu nombre completo"
            else -> validarEmail(email) ?: validarPassword(password)
        }
        if (error != null) {
            _uiState.value = AuthUiState.Error(error)
            return
        }
        simularPeticion {
            _usuarioActual.value = email.trim()
            AuthUiState.Success("¡Cuenta creada! Bienvenido/a, ${name.trim()}")
        }
    }

    fun resetPassword(email: String) {
        val error = validarEmail(email)
        if (error != null) {
            _uiState.value = AuthUiState.Error(error)
            return
        }
        simularPeticion {
            AuthUiState.Success("Te enviamos un enlace a ${email.trim()} para crear una clave nueva")
        }
    }

    fun logout() {
        _usuarioActual.value = null
        _uiState.value = AuthUiState.Idle
    }

    /** Vuelve a Idle (por ejemplo, al cambiar de pantalla o cerrar un aviso). */
    fun resetState() {
        _uiState.value = AuthUiState.Idle
    }

    private fun simularPeticion(resultado: () -> AuthUiState) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            delay(DEMORA_SIMULADA_MS)
            _uiState.value = resultado()
        }
    }

    private fun validarEmail(email: String): String? =
        if (esEmailValido(email)) null else "Ingresa un correo válido (ej. nombre@correo.com)"

    private fun validarPassword(password: String): String? =
        if (esPasswordValida(password)) null else "La contraseña debe tener al menos $MIN_PASSWORD caracteres"

    companion object {
        const val MIN_PASSWORD = 6
        private const val DEMORA_SIMULADA_MS = 1200L
        private val EMAIL_REGEX = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

        /** Las pantallas usan estas dos para validar en vivo mientras se escribe. */
        fun esEmailValido(email: String): Boolean = EMAIL_REGEX.matches(email.trim())
        fun esPasswordValida(password: String): Boolean = password.length >= MIN_PASSWORD
    }
}
