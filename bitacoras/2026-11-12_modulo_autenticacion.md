# Bitácora – 12/11/2026 – Módulo de autenticación

**Asistente:** Claude (Claude Code) · **Guía:** `ingenieria/Caso_de_Uso_Autenticacion.pptx`

Se usaron los 4 prompts del docente (diapositivas 5, 6 y 7) como especificación,
cambiando "AppSeminario" por "SoundCraft". Abajo va cada prompt tal como se usó
y qué se ajustó a mano después.

---

## Prompt 1 – AuthUiState y AuthViewModel (diapositiva 5)
```
[ROL]: Desarrollador Android Senior experto en Kotlin y Clean Architecture.
[TAREA]: Crea una arquitectura de estado para el flujo de autenticación de la app "SoundCraft".
[CONTEXTO]: Necesito administrar los estados de tres pantallas: Login, Registro y Recuperación de Contraseña.
[REQUISITOS TÉCNICOS]:
- Define una sealed interface llamada `AuthUiState` con los estados: Idle, Loading, Success(val message: String), y Error(val errorMessage: String).
- Crea un `AuthViewModel` utilizando `StateFlow` y `asStateFlow()`.
- Agrega funciones para: `login(email, password)`, `register(name, email, password)`, y `resetPassword(email)`.
- Incluye validaciones básicas de Email Regex y contraseña de mínimo 6 caracteres dentro del ViewModel.
[FORMATO]: Código Kotlin limpio, comentado y sin dependencias innecesarias.
```
**Archivos:** `ui/auth/AuthUiState.kt`, `ui/auth/AuthViewModel.kt` · **¿Compiló al 1er intento?** Sí
**Auditoría humana:**
- Las validaciones quedaron como funciones públicas (`esEmailValido`, `esPasswordValida`) para que la UI valide en vivo.
- Se agregó `AuthValidacionTest` (pruebas unitarias).
- Como el diseño dice "Correo electrónico **o usuario**", el login acepta también un usuario (`esEmailOUsuarioValido`).

## Prompt 2 – LoginScreen (diapositiva 6)
```
[ROL]: Especialista en Jetpack Compose UI.
[TAREA]: Crea el composable `LoginScreen` para "SoundCraft".
[REQUISITOS]:
- Callbacks: `onLoginSuccess`, `onNavigateToRegister`, `onNavigateToForgotPassword`.
- OutlinedTextField para Email y Password (ojo de visibilidad interactivo).
- Botón principal de login y enlaces secundarios.
- Muestra `CircularProgressIndicator` si el estado es `AuthUiState.Loading`.
- Muestra mensajes de error en color `MaterialTheme.colorScheme.error`.
```
**Archivo:** `ui/auth/LoginScreen.kt` · **¿Compiló al 1er intento?** Sí
**Auditoría humana:**
- Se separó en `LoginScreen` (con ViewModel) y `LoginContent` (sin ViewModel) para poder usar `@Preview` (*state hoisting*).
- Se cambió `OutlinedTextField` por `TextField` relleno gris para respetar nuestro diseño (`ingenieria/diseno_ui_auth.jpg`).
- Se agregaron "Recuérdame" (switch) e "Iniciar con Google" (muestra "Próximamente") porque están en el diseño.

## Prompt 3 – RegisterScreen y validaciones (diapositiva 6)
```
[ROL]: Desarrollador Android Jetpack Compose.
[TAREA]: Diseña `RegisterScreen` con validación activa.
[REQUISITOS]:
- Campos: Nombre, Email, Contraseña, Confirmar Contraseña y Checkbox de Términos.
- Botón 'Crear Cuenta' deshabilitado (`enabled = false`) si las contraseñas no coinciden o no acepta términos.
- Feedback visual instantáneo bajo los campos si no coinciden.
- Estilos de Material 3 Theme.
```
**Archivo:** `ui/auth/RegisterScreen.kt` · **¿Compiló al 1er intento?** Sí
**Auditoría humana:**
- El Checkbox se reemplazó por un Switch ("He leído y acepto los términos") como en nuestro diseño.
- El botón dice "REGISTRARSE" (diseño) en lugar de "Crear Cuenta".

## Prompt 4 – Navegación con NavHost (diapositiva 7)
```
[ROL]: Arquitecto de Software Android.
[TAREA]: Configura la navegación del flujo de autenticación con Navigation Compose para SoundCraft.
[CONTEXTO]: Tengo las pantallas LoginScreen, RegisterScreen y ForgotPasswordScreen.
[REQUISITOS]:
- Define una sealed class o enum de rutas: AuthScreen.Login, AuthScreen.Register, AuthScreen.ForgotPassword, y AuthScreen.Home.
- Crea el Composable `AuthNavigationGraph(navController: NavHostController)`.
- Configura los callbacks de navegación de cada pantalla para permitir la transición fluida.
- Al iniciar sesión exitosamente, navegar a "Home" limpiando el backstack (`popUpTo(AuthScreen.Login.route) { inclusive = true }`).
[FORMATO]: Código Kotlin completo listo para integrar en MainActivity.
```
**Archivos:** `navigation/AuthNavigationGraph.kt`, `ui/auth/ForgotPasswordScreen.kt`, `ui/home/HomeScreen.kt`, `MainActivity.kt`
**¿Compiló al 1er intento?** Sí
**Auditoría humana:**
- Un solo `AuthViewModel` compartido y `resetState()` al cambiar de pantalla, para que un error no aparezca en la pantalla siguiente.
- La confirmación de "Enviar enlace" es un `AlertDialog`.

---

## Prompt 5 – Aplicar nuestro diseño y el logo (propio del equipo)
```
[ROL]: Diseñadora UI especialista en Jetpack Compose.
[ENTRADA]: ingenieria/diseno_ui_auth.jpg (pantallas de inicio de sesión, registro y recuperar contraseña).
[TAREA]: Rehacer Login, Registro y Recuperar con ese diseño y redibujar el logo "S" como vector.
[RESTRICCIONES]: Tema oscuro, campos grises rectangulares, botones tipo pastilla (violeta y gris),
switch violeta, el logo NO es una S común: son dos piezas iguales giradas 180°.
```
**Archivos:** `res/drawable/ic_soundcraft_logo.xml`, `ui/components/SoundCraftLogo.kt`, `ui/components/CamposAuth.kt` y las 3 pantallas
**¿Compiló al 1er intento?** Sí
**Auditoría humana:**
- El logo se redibujó a mano a partir de la imagen (no había archivo original). Comparar con el diseño y corregir si hace falta.
- Tipografía: se agregó Poppins (Google Fonts, licencia OFL) en `res/font` y se aplicó a todos los estilos en `Type.kt`.
- Probado en el emulador: capturas en `ingenieria/capturas/`.
