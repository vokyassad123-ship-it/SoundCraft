package com.example.soundcraft.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.soundcraft.ui.components.BotonPrincipal
import com.example.soundcraft.ui.components.CampoTexto
import com.example.soundcraft.ui.theme.SoundCraftTheme

/** Pantalla para recuperar la clave, conectada al ViewModel. */
@Composable
fun ForgotPasswordScreen(
    viewModel: AuthViewModel,
    onNavigateToLogin: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    ForgotPasswordContent(
        uiState = uiState,
        onSendLink = viewModel::resetPassword,
        // Al cerrar el aviso de éxito o tocar "Volver", se limpia el estado y se vuelve al login
        onNavigateToLogin = { viewModel.resetState(); onNavigateToLogin() },
    )
}

@Composable
fun ForgotPasswordContent(
    uiState: AuthUiState,
    onSendLink: (email: String) -> Unit,
    onNavigateToLogin: () -> Unit,
) {
    var email by rememberSaveable { mutableStateOf("") }
    val emailValido = AuthViewModel.esEmailValido(email)
    val cargando = uiState is AuthUiState.Loading

    // Aviso (Dialog) cuando el enlace "se envió"
    if (uiState is AuthUiState.Success) {
        AlertDialog(
            onDismissRequest = onNavigateToLogin,
            title = { Text("Revisa tu correo") },
            text = { Text(uiState.message) },
            confirmButton = {
                TextButton(onClick = onNavigateToLogin) { Text("VOLVER AL LOGIN") }
            },
        )
    }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            TextButton(onClick = onNavigateToLogin) { Text("< Volver al Login") }
            Spacer(Modifier.height(16.dp))
            Text(
                text = "RECUPERAR CLAVE",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = "Ingresa tu e-mail registrado y te enviaremos un enlace para crear una clave nueva.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(8.dp))

            CampoTexto(
                valor = email,
                onCambio = { email = it },
                etiqueta = "Correo electrónico",
                tipoTeclado = KeyboardType.Email,
                accionIme = ImeAction.Done,
                habilitado = !cargando,
                error = if (email.isNotEmpty() && !emailValido) "Correo no válido" else null,
            )

            if (uiState is AuthUiState.Error) {
                Text(uiState.errorMessage, color = MaterialTheme.colorScheme.error)
            }

            BotonPrincipal(
                texto = "ENVIAR ENLACE",
                onClick = { onSendLink(email) },
                habilitado = emailValido,
                cargando = cargando,
            )
        }
    }
}

@Preview(name = "Recuperar clave", showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun ForgotPasswordPreview() {
    SoundCraftTheme { ForgotPasswordContent(AuthUiState.Idle, {}, {}) }
}

@Preview(name = "Recuperar clave enviado", showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun ForgotPasswordSuccessPreview() {
    SoundCraftTheme {
        ForgotPasswordContent(AuthUiState.Success("Te enviamos un enlace a amira@correo.com"), {}, {})
    }
}
