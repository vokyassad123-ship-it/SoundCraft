package com.example.soundcraft.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.soundcraft.ui.theme.GrisCampo
import com.example.soundcraft.ui.theme.SoundCraftTheme
import com.example.soundcraft.ui.theme.TextoOscuro

/** Colores de los campos grises del diseño (sin línea inferior, texto oscuro). */
@Composable
private fun coloresCampo() = TextFieldDefaults.colors(
    focusedContainerColor = GrisCampo,
    unfocusedContainerColor = GrisCampo,
    disabledContainerColor = GrisCampo.copy(alpha = 0.6f),
    errorContainerColor = GrisCampo,
    focusedTextColor = TextoOscuro,
    unfocusedTextColor = TextoOscuro,
    disabledTextColor = TextoOscuro.copy(alpha = 0.6f),
    errorTextColor = TextoOscuro,
    focusedPlaceholderColor = TextoOscuro.copy(alpha = 0.7f),
    unfocusedPlaceholderColor = TextoOscuro.copy(alpha = 0.7f),
    errorPlaceholderColor = TextoOscuro.copy(alpha = 0.7f),
    focusedIndicatorColor = MaterialTheme.colorScheme.primary,
    unfocusedIndicatorColor = Color.Transparent,
    errorIndicatorColor = MaterialTheme.colorScheme.error,
    cursorColor = MaterialTheme.colorScheme.primary,
    focusedTrailingIconColor = TextoOscuro,
    unfocusedTrailingIconColor = TextoOscuro,
    errorTrailingIconColor = TextoOscuro,
    errorSupportingTextColor = MaterialTheme.colorScheme.error,
)

private val formaCampo = RoundedCornerShape(2.dp)

/** Campo gris rectangular con el texto de ayuda adentro y el error debajo (en rojo). */
@Composable
fun CampoTexto(
    valor: String,
    onCambio: (String) -> Unit,
    etiqueta: String,
    modifier: Modifier = Modifier,
    error: String? = null,
    habilitado: Boolean = true,
    tipoTeclado: KeyboardType = KeyboardType.Text,
    accionIme: ImeAction = ImeAction.Next,
) {
    TextField(
        value = valor,
        onValueChange = onCambio,
        placeholder = { Text(etiqueta) },
        isError = error != null,
        supportingText = error?.let { { Text(it) } },
        singleLine = true,
        enabled = habilitado,
        keyboardOptions = KeyboardOptions(keyboardType = tipoTeclado, imeAction = accionIme),
        shape = formaCampo,
        colors = coloresCampo(),
        modifier = modifier.fillMaxWidth(),
    )
}

/** Campo de contraseña gris con el botón del ojo para mostrar u ocultar lo escrito. */
@Composable
fun CampoPassword(
    valor: String,
    onCambio: (String) -> Unit,
    etiqueta: String,
    modifier: Modifier = Modifier,
    error: String? = null,
    habilitado: Boolean = true,
    accionIme: ImeAction = ImeAction.Next,
) {
    var visible by rememberSaveable { mutableStateOf(false) }
    TextField(
        value = valor,
        onValueChange = onCambio,
        placeholder = { Text(etiqueta) },
        isError = error != null,
        supportingText = error?.let { { Text(it) } },
        singleLine = true,
        enabled = habilitado,
        visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = accionIme),
        trailingIcon = {
            IconButton(onClick = { visible = !visible }) {
                Icon(
                    imageVector = if (visible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                    contentDescription = if (visible) "Ocultar contraseña" else "Mostrar contraseña",
                )
            }
        },
        shape = formaCampo,
        colors = coloresCampo(),
        modifier = modifier.fillMaxWidth(),
    )
}

/** Los dos estilos de botón del diseño. */
enum class EstiloBoton {
    /** Violeta con texto blanco (ej. INICIAR CON GOOGLE, REGISTRARSE, ENVIAR ENLACE). */
    Violeta,

    /** Gris claro con texto violeta (ej. INICIAR SESIÓN, VOLVER AL INICIO). */
    Gris,
}

/** Botón redondeado (tipo pastilla). Mientras carga muestra el círculo en lugar del texto. */
@Composable
fun BotonPill(
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    estilo: EstiloBoton = EstiloBoton.Violeta,
    habilitado: Boolean = true,
    cargando: Boolean = false,
) {
    val violeta = MaterialTheme.colorScheme.primary
    val (fondo, letra) = when (estilo) {
        EstiloBoton.Violeta -> violeta to Color.White
        EstiloBoton.Gris -> GrisCampo to violeta
    }
    Button(
        onClick = onClick,
        enabled = habilitado && !cargando,
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = fondo,
            contentColor = letra,
            disabledContainerColor = fondo.copy(alpha = 0.45f),
            disabledContentColor = letra.copy(alpha = 0.7f),
        ),
        modifier = modifier.fillMaxWidth().height(52.dp),
    ) {
        if (cargando) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = letra,
                strokeWidth = 3.dp,
            )
        } else {
            Text(texto, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}

/** Fila con un texto a la izquierda y un interruptor violeta a la derecha. */
@Composable
fun FilaSwitch(
    texto: String,
    activado: Boolean,
    onCambio: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    habilitado: Boolean = true,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(texto, style = MaterialTheme.typography.bodyMedium)
        Switch(
            checked = activado,
            onCheckedChange = onCambio,
            enabled = habilitado,
            colors = SwitchDefaults.colors(
                checkedTrackColor = MaterialTheme.colorScheme.primary,
                checkedThumbColor = GrisCampo,
                uncheckedTrackColor = Color.DarkGray,
                uncheckedThumbColor = GrisCampo,
                uncheckedBorderColor = Color.Transparent,
            ),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun ComponentesPreview() {
    SoundCraftTheme {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            CampoTexto("", {}, "Correo electrónico o usuario")
            CampoPassword("secreto", {}, "Contraseña", error = "Mínimo 6 caracteres")
            FilaSwitch("Recuérdame", true, {})
            BotonPill("INICIAR SESIÓN", {}, estilo = EstiloBoton.Gris)
            BotonPill("INICIAR CON GOOGLE", {})
        }
    }
}
