package com.example.soundcraft.ui.auth

/**
 * Los cuatro estados posibles de las pantallas de autenticación.
 * La pantalla "mira" este estado y se dibuja según cuál sea.
 */
sealed interface AuthUiState {
    /** Esperando que el usuario haga algo. */
    data object Idle : AuthUiState

    /** Procesando: se muestra el círculo de carga. */
    data object Loading : AuthUiState

    /** Todo salió bien. */
    data class Success(val message: String) : AuthUiState

    /** Algo falló: se muestra el mensaje en rojo. */
    data class Error(val errorMessage: String) : AuthUiState
}
