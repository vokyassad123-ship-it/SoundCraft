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
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.soundcraft.ui.components.BotonPrincipal
import com.example.soundcraft.ui.components.CampoPassword
import com.example.soundcraft.ui.components.CampoTexto
import com.example.soundcraft.ui.components.SoundCraftLogo
import com.example.soundcraft.ui.theme.SoundCraftTheme

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
    onLogin: (email: String, password: String) -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToForgotPassword: () -> Unit,
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    val emailValido = AuthViewModel.esEmailValido(email)
    val passwordValida = AuthViewModel.esPasswordValida(password)
    val cargando = uiState is AuthUiState.Loading

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Spacer(Modifier.height(32.dp))
            SoundCraftLogo(tamanoS = 80)
            Spacer(Modifier.height(16.dp))
            Text(
                text = "¡Hola de nuevo!",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )

            CampoTexto(
                valor = email,
                onCambio = { email = it },
                etiqueta = "Correo electrónico",
                tipoTeclado = KeyboardType.Email,
                habilitado = !cargando,
                error = if (email.isNotEmpty() && !emailValido) "Correo no válido" else null,
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

            TextButton(
                onClick = onNavigateToForgotPassword,
                modifier = Modifier.align(Alignment.End),
            ) { Text("¿Olvidaste tu clave?") }

            if (uiState is AuthUiState.Error) {
                Text(uiState.errorMessage, color = MaterialTheme.colorScheme.error)
            }

            BotonPrincipal(
                texto = "INICIAR SESIÓN",
                onClick = { onLogin(email, password) },
                habilitado = emailValido && passwordValida,
                cargando = cargando,
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("¿No tienes cuenta?", style = MaterialTheme.typography.bodyMedium)
                TextButton(onClick = onNavigateToRegister) {
                    Text("Regístrate", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Preview(name = "Login", showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun LoginPreview() {
    SoundCraftTheme { LoginContent(AuthUiState.Idle, { _, _ -> }, {}, {}) }
}

@Preview(name = "Login con error", showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun LoginErrorPreview() {
    SoundCraftTheme {
        LoginContent(AuthUiState.Error("Correo o contraseña incorrectos"), { _, _ -> }, {}, {})
    }
}
