# Bitácora – 12/11/2026 – Módulo de autenticación

**Asistente:** Claude (Claude Code) · **Guía:** `ingenieria/Caso_de_Uso_Autenticacion.pptx`

## P1 – Estado y ViewModel
**[ROL]** Desarrollador Android Senior experto en Kotlin y Clean Architecture.
**[TAREA]** Crear `AuthUiState` (Idle, Loading, Success, Error) y `AuthViewModel` con StateFlow.
**[CONTEXTO]** SoundCraft, pantallas Login, Registro y Recuperar clave. Autenticación simulada.
**[RESTRICCIONES]** Funciones `login`, `register`, `resetPassword`; validar email con Regex y clave de mínimo 6.
**Resultado:** compiló al primer intento. 3 pruebas unitarias de validación pasan.

## P2 – Tema
**[ROL]** Diseñadora UI. **[ENTRADA]** `ingenieria/diseno_ui.webp`.
**[TAREA]** Paleta y tema oscuro fijo de Material 3 con los colores del diseño.
**Resultado:** compiló. Decisión: violeta del logo como `primary` (la guía sugiere azul #2563EB).

## P3 – Login y Registro
**[ROL]** Especialista en Jetpack Compose UI.
**[TAREA]** `LoginScreen` (callbacks `onLoginSuccess`, `onNavigateToRegister`, `onNavigateToForgotPassword`,
ojo de visibilidad, loader y errores en `colorScheme.error`) y `RegisterScreen` (botón deshabilitado si las claves
no coinciden o no acepta términos, feedback bajo los campos).
**Resultado:** compiló. Se agregaron `@Preview` en tema oscuro.

## P4 – Recuperar clave y navegación
**[ROL]** Arquitecto de Software Android.
**[TAREA]** `ForgotPasswordScreen` con Dialog de confirmación y `AuthNavigationGraph` con rutas
`AuthScreen.Login/Register/ForgotPassword/Home`; al iniciar sesión `popUpTo(Login) { inclusive = true }`.
**Resultado:** compiló. APK de debug generado. **Pendiente:** probar en emulador y tomar capturas.
