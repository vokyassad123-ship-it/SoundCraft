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
import com.example.soundcraft.ui.components.BotonPill
import com.example.soundcraft.ui.components.CampoPassword
import com.example.soundcraft.ui.components.CampoTexto
import com.example.soundcraft.ui.components.FilaSwitch
import com.example.soundcraft.ui.components.SoundCraftLogoUnete
import com.example.soundcraft.ui.theme.SoundCraftTheme

/** Pantalla de registro conectada al ViewModel. */
@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState) {
        if (uiState is AuthUiState.Success) {
            viewModel.resetState()
            onRegisterSuccess()
        }
    }

    RegisterContent(
        uiState = uiState,
        onRegister = viewModel::register,
        onNavigateToLogin = { viewModel.resetState(); onNavigateToLogin() },
    )
}

@Composable
fun RegisterContent(
    uiState: AuthUiState,
    onRegister: (name: String, email: String, password: String) -> Unit,
    onNavigateToLogin: () -> Unit,
) {
    var nombre by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var repetir by rememberSaveable { mutableStateOf("") }
    var aceptaTerminos by rememberSaveable { mutableStateOf(false) }

    val emailValido = AuthViewModel.esEmailValido(email)
    val passwordValida = AuthViewModel.esPasswordValida(password)
    val coinciden = password == repetir
    val cargando = uiState is AuthUiState.Loading

    // El botón solo se habilita cuando TODO está bien
    val formularioValido = nombre.isNotBlank() && emailValido && passwordValida &&
        repetir.isNotEmpty() && coinciden && aceptaTerminos

    Scaffold { padding ->
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
            Spacer(Modifier.height(40.dp))
            SoundCraftLogoUnete()
            Spacer(Modifier.height(24.dp))
            Text(
                text = "CREAR CUENTA",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
            )

            CampoTexto(
                valor = nombre,
                onCambio = { nombre = it },
                etiqueta = "Nombre completo",
                habilitado = !cargando,
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
                habilitado = !cargando,
                error = if (password.isNotEmpty() && !passwordValida) {
                    "Mínimo ${AuthViewModel.MIN_PASSWORD} caracteres"
                } else null,
            )
            CampoPassword(
                valor = repetir,
                onCambio = { repetir = it },
                etiqueta = "Repetir contraseña",
                accionIme = ImeAction.Done,
                habilitado = !cargando,
                error = if (repetir.isNotEmpty() && !coinciden) "Las contraseñas no coinciden" else null,
            )

            FilaSwitch(
                texto = "He leído y acepto los términos",
                activado = aceptaTerminos,
                onCambio = { aceptaTerminos = it },
                habilitado = !cargando,
            )

            if (uiState is AuthUiState.Error) {
                Text(uiState.errorMessage, color = MaterialTheme.colorScheme.error)
            }

            BotonPill(
                texto = "REGISTRARSE",
                onClick = { onRegister(nombre, email, password) },
                habilitado = formularioValido,
                cargando = cargando,
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("¿Ya tienes cuenta?", style = MaterialTheme.typography.bodyMedium)
                TextButton(onClick = onNavigateToLogin) {
                    Text("INICIA SESIÓN", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Preview(name = "Registro", showBackground = true, backgroundColor = 0xFF000000, heightDp = 800)
@Composable
private fun RegisterPreview() {
    SoundCraftTheme { RegisterContent(AuthUiState.Idle, { _, _, _ -> }, {}) }
}

@Preview(name = "Registro cargando", showBackground = true, backgroundColor = 0xFF000000, heightDp = 800)
@Composable
private fun RegisterLoadingPreview() {
    SoundCraftTheme { RegisterContent(AuthUiState.Loading, { _, _, _ -> }, {}) }
}
