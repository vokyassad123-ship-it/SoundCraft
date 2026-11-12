# Bitácora – 13/11/2026 – Gestión de recursos (pantallas principales)

**Asistente:** Claude (Claude Code) · **Entrada:** `ingenieria/diseno_ui_auth.jpg` y `requerimientos.md`

Formato de prompt estructurado de la guía (Etapa 3): **[ROL] [ENTRADA] [TAREA] [RESTRICCIONES]**.

---

## Prompt 6 – Estado y lógica (rol desarrolladora)
```
[ROL]: Desarrolladora Android experta en Kotlin, MVVM y StateFlow.
[ENTRADA]: @requerimientos.md (solo WAV/MP3; se guarda solo ruta, nombre y clasificación; 10 categorías).
[TAREA]: Crear el modelo de datos (Categoria, Pista), un repositorio con datos de ejemplo y un
RecursosViewModel con los estados Cargando, Exito, Vacio y Error, más el estado del reproductor.
[RESTRICCIONES]: Sin base de datos todavía (en memoria). Validar WAV/MP3 en el ViewModel.
Código comentado en español y con pruebas unitarias de la regla de formato.
```
**Archivos:** `data/Modelos.kt`, `data/RecursosRepository.kt`, `ui/recursos/RecursosViewModel.kt`, `test/RecursosTest.kt`
**¿Compiló al 1er intento?** Sí
**Auditoría humana:** la categoría Jazz quedó vacía a propósito para poder mostrar el estado "vacío".

## Prompt 7 – Categorías, Buscar y Detalle (rol diseñadora UI)
```
[ROL]: Diseñadora UI especialista en Jetpack Compose y Material 3.
[ENTRADA]: diseño de Inicio (buscador + tarjetas de colores) y de EXPLOSIONES (encabezado rosa + lista).
[TAREA]: Pantalla de categorías en grilla de 2 columnas, pantalla Buscar que filtre mientras se escribe,
y detalle de categoría con botón "Agregar pista" que abra el selector de archivos.
[RESTRICCIONES]: Tema oscuro y alto contraste. El selector solo muestra WAV/MP3.
Mostrar cargando/vacío/error. Cada pantalla con @Preview.
```
**Archivos:** `ui/categorias/`, `ui/buscar/`, `ui/components/FilaPista.kt`, `ui/components/Estados.kt`
**¿Compiló al 1er intento?** Sí

## Prompt 8 – Panel 4x4 (rol diseñadora UI)
```
[ROL]: Diseñadora UI especialista en Jetpack Compose.
[ENTRADA]: diseño del panel (16 pads de colores, menú de tres puntos, reproductor con degradado rosa).
[TAREA]: Grilla 4x4 que se llena según la categoría elegida (filtros en LazyRow), menú con
"Seleccionar y editar", "Agregar a la fila" y "Compartir", y reproductor con play/pausa,
anterior, siguiente, aleatorio, repetir y barra de progreso.
[RESTRICCIONES]: Botones táctiles amplios. Sin audio real todavía (reproductor simulado).
```
**Archivo:** `ui/panel/PanelScreen.kt`
**¿Compiló al 1er intento?** **No.**
**Auditoría humana:**
- Error de compilación: se importó `androidx.compose.foundation.layout.weight`, que es interno. Se quitó el import.
- En el emulador la barra de progreso salía gruesa (estilo nuevo de Material 3). Se reemplazó por una línea fina con bolita, como el diseño.

## Prompt 9 – Navegación principal (rol arquitecto)
```
[ROL]: Arquitecto de Software Android.
[ENTRADA]: AuthNavigationGraph existente y las pantallas nuevas.
[TAREA]: Agregar Splash al inicio y reemplazar el Inicio provisorio por una MainScreen con
barra inferior (Inicio, Buscar, Reproducir, Perfil) y la ruta del detalle de categoría.
[RESTRICCIONES]: Un solo RecursosViewModel compartido. Cerrar sesión vuelve al login sin historial.
```
**Archivos:** `ui/main/MainScreen.kt`, `ui/splash/SplashScreen.kt`, `ui/perfil/PerfilScreen.kt`, `navigation/AuthNavigationGraph.kt`
**¿Compiló al 1er intento?** Sí
**Probado en el emulador:** login con usuario → categorías → EXPLOSIONES → panel (tocar pad, mover barra, play) → menú → perfil.
Capturas en `ingenieria/capturas/`.

## Pendiente (QA – Paso 7)
- Reproducir audio de verdad (MediaPlayer/ExoPlayer) usando la ruta guardada.
- Guardar los datos al cerrar la app (Room o DataStore) y "Recuérdame".
- Opciones del menú del panel e "Iniciar con Google".
