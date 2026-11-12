package com.example.soundcraft.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.soundcraft.ui.components.BotonPill
import com.example.soundcraft.ui.components.CampoPassword
import com.example.soundcraft.ui.components.CampoTexto
import com.example.soundcraft.ui.components.EstiloBoton
import com.example.soundcraft.ui.components.FilaSwitch
import com.example.soundcraft.ui.components.SoundCraftLogoHorizontal
import com.example.soundcraft.ui.theme.SoundCraftTheme
import kotlinx.coroutines.launch

/**
 * Pantalla de inicio de sesión conectada al ViewModel.
 * Escucha el estado y, si el login sale bien, avisa con onLoginSuccess.
 */
@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToForgotPassword: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState) {
        if (uiState is AuthUiState.Success) {
            viewModel.resetState()
            onLoginSuccess()
        }
    }

    LoginContent(
        uiState = uiState,
        onLogin = viewModel::login,
        onNavigateToRegister = { viewModel.resetState(); onNavigateToRegister() },
        onNavigateToForgotPassword = { viewModel.resetState(); onNavigateToForgotPassword() },
    )
}

/** Solo el dibujo de la pantalla (sin ViewModel), así se puede ver en @Preview. */
@Composable
fun LoginContent(
    uiState: AuthUiState,
    onLogin: (usuario: String, password: String) -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToForgotPassword: () -> Unit,
) {
    var usuario by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var recordarme by rememberSaveable { mutableStateOf(true) }

    val usuarioValido = AuthViewModel.esEmailOUsuarioValido(usuario)
    val passwordValida = AuthViewModel.esPasswordValida(password)
    val cargando = uiState is AuthUiState.Loading

    val snackbar = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(snackbarHost = { SnackbarHost(snackbar) }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 32.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Spacer(Modifier.height(56.dp))
            SoundCraftLogoHorizontal()
            Spacer(Modifier.height(32.dp))

            CampoTexto(
                valor = usuario,
                onCambio = { usuario = it },
                etiqueta = "Correo electrónico o usuario",
                tipoTeclado = KeyboardType.Email,
                habilitado = !cargando,
                error = if (usuario.isNotEmpty() && !usuarioValido) "Correo o usuario no válido" else null,
            )
            CampoPassword(
                valor = password,
                onCambio = { password = it },
                etiqueta = "Contraseña",
                accionIme = ImeAction.Done,
                habilitado = !cargando,
                error = if (password.isNotEmpty() && !passwordValida) {
                    "Mínimo ${AuthViewModel.MIN_PASSWORD} caracteres"
                } else null,
            )

            // "Recuérdame" por ahora solo cambia de posición: se guardará cuando haya datos reales
            FilaSwitch("Recuérdame", recordarme, { recordarme = it }, habilitado = !cargando)

            TextButton(onClick = onNavigateToForgotPassword) {
                Text("¿Olvidaste tu contraseña?", color = MaterialTheme.colorScheme.onBackground)
            }

            if (uiState is AuthUiState.Error) {
                Text(uiState.errorMessage, color = MaterialTheme.colorScheme.error)
            }

            BotonPill(
                texto = "INICIAR SESIÓN",
                onClick = { onLogin(usuario, password) },
                estilo = EstiloBoton.Gris,
                habilitado = usuarioValido && passwordValida,
                cargando = cargando,
            )
            BotonPill(
                texto = "INICIAR CON GOOGLE",
                onClick = { scope.launch { snackbar.showSnackbar("Próximamente: inicio con Google") } },
                habilitado = !cargando,
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("¿No tienes cuenta?", style = MaterialTheme.typography.bodyMedium)
                TextButton(onClick = onNavigateToRegister) {
                    Text("REGÍSTRATE", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Preview(name = "Login", showBackground = true, backgroundColor = 0xFF000000, heightDp = 800)
@Composable
private fun LoginPreview() {
    SoundCraftTheme { LoginContent(AuthUiState.Idle, { _, _ -> }, {}, {}) }
}

@Preview(name = "Login con error", showBackground = true, backgroundColor = 0xFF000000, heightDp = 800)
@Composable
private fun LoginErrorPreview() {
    SoundCraftTheme {
        LoginContent(AuthUiState.Error("Correo o contraseña incorrectos"), { _, _ -> }, {}, {})
    }
}
