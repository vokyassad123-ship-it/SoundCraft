# SoundCraft

Portafolio de recursos sonoros y musicales para almacenar, clasificar y recuperar
rápidamente. Hecho con **Kotlin, Jetpack Compose, Material 3 y MVVM con StateFlow**.

Requerimientos completos en [requerimientos.md](requerimientos.md) · Diseño UI en
[ingenieria/diseno_ui.webp](ingenieria/diseno_ui.webp).

## Cómo ejecutarla
1. Clonar el repo con GitHub Desktop y abrir la carpeta `SoundCraft` en **Android Studio**.
2. Esperar a que termine el *Gradle sync* (abajo a la derecha).
3. Elegir un emulador o un teléfono conectado y presionar **Run ▶**.
4. Para ver las pantallas sin emulador: abrir `LoginScreen.kt`, `RegisterScreen.kt`
   o `ForgotPasswordScreen.kt` y usar la vista **Split / Design** (los `@Preview`).

> La autenticación es **simulada**: entra cualquier correo con formato válido y una
> contraseña de 6 o más caracteres.

## Módulo de autenticación
| Pantalla | Qué hace |
|---|---|
| Login | Correo o usuario, contraseña con ojo, "Recuérdame", "¿Olvidaste tu contraseña?", "Iniciar con Google" (próximamente) y "Regístrate". El botón se habilita solo con datos válidos. |
| Registro | Nombre, correo, contraseña, repetir contraseña y switch de términos. Validación en vivo. |
| Recuperar contraseña | Correo + "Enviar enlace". Confirma con un Dialog. "Volver al inicio" regresa al login. |
| Inicio (provisorio) | Muestra el usuario y permite cerrar sesión. |

Estados de la UI (`AuthUiState`): `Idle`, `Loading` (círculo de carga), `Success`, `Error`.

### Capturas (emulador)
| Login | Registro | Recuperar clave |
|---|---|---|
| ![Login](ingenieria/capturas/login.png) | ![Registro](ingenieria/capturas/registro.png) | ![Recuperar](ingenieria/capturas/recuperar.png) |

## Estructura
```
app/src/main/java/com/example/soundcraft/
├── MainActivity.kt                 punto de entrada
├── navigation/AuthNavigationGraph.kt   rutas y NavHost
└── ui/
    ├── auth/      AuthUiState, AuthViewModel, Login, Register, ForgotPassword
    ├── home/      Inicio provisorio
    ├── components/ logo, campos de texto y botón principal
    └── theme/     colores y tema oscuro
```

## Bitácora de prompts e IA
| ID | Objetivo | Asistente | ¿Compiló al 1er intento? | Auditoría humana (errores, alucinaciones, cambios manuales) |
|---|---|---|---|---|
Los prompts 1 a 4 son los del docente (Caso de Uso, diapositivas 5–7) con "AppSeminario" cambiado
por "SoundCraft". El texto completo de cada prompt está en
[bitacoras/2026-11-12_modulo_autenticacion.md](bitacoras/2026-11-12_modulo_autenticacion.md).

| ID | Objetivo | Asistente | ¿Compiló al 1er intento? | Auditoría humana (errores, alucinaciones, cambios manuales) |
|---|---|---|---|---|
| P1 | `AuthUiState` + `AuthViewModel` con StateFlow y validación (Regex email, mín. 6) | Claude (Claude Code) | Sí | Validaciones públicas para validar en vivo desde la UI; pruebas unitarias; el login acepta correo o usuario (como pide el diseño). |
| P2 | `LoginScreen` | Claude (Claude Code) | Sí | Separado en `Screen` + `Content` para usar `@Preview` (*state hoisting*); `TextField` gris en vez de `OutlinedTextField` por el diseño. |
| P3 | `RegisterScreen` con validación activa | Claude (Claude Code) | Sí | Checkbox reemplazado por Switch y botón "REGISTRARSE", como en el diseño. |
| P4 | `ForgotPasswordScreen` + `NavHost` | Claude (Claude Code) | Sí | Un solo `AuthViewModel` compartido y `resetState()` al cambiar de pantalla. |
| P5 | Aplicar nuestro diseño y redibujar el logo "S" como vector | Claude (Claude Code) | Sí | Logo trazado a mano desde la imagen; violeta del logo en lugar del azul #2563EB de la guía; tipografía Poppins agregada. |

> **Equipo:** completar la columna de auditoría con lo que ustedes revisaron y probaron.
