# SoundCraft – Requerimientos

## Propósito
Portafolio de recursos sonoros y musicales para almacenar,
clasificar y recuperar rápidamente. NO es un controlador de música en vivo.

## Alcance de esta entrega
Prototipo **navegable**: todas las pantallas existen y se puede pasar de una a otra,
con datos de ejemplo (no se guardan archivos reales ni se reproduce audio todavía).
Los botones que aún no funcionan muestran un aviso ("Próximamente").
La autenticación es simulada con un ViewModel (sin servidor).

## Stack obligatorio
Kotlin, Jetpack Compose, Material Design 3, MVVM con StateFlow, Navigation Compose.

## Reglas funcionales
- Solo se aceptan archivos WAV o MP3 (filtro en el selector de archivos).
- Almacenamiento local: se guarda solo ruta física, nombre y clasificación.
- Categorías iniciales (según el diseño): Rock, Explosiones, Pop, Ambiente, Funk,
  Voces, Jazz, Beats, Instrumentos, Riser.

## Reglas de autenticación (guía del docente: Caso de Uso Autenticación)
- **Login:** email con formato válido, contraseña con botón de ojo para mostrar/ocultar,
  botón "Iniciar sesión" habilitado solo si los datos son válidos, enlaces a
  "¿Olvidaste tu clave?" y "Regístrate".
- **Registro:** Nombre completo, Email, Contraseña, Repetir clave y checkbox
  "Acepto términos". Validación en vivo: mínimo 6 caracteres y que las claves coincidan.
  El botón queda deshabilitado mientras algo no se cumpla.
- **Recuperar clave:** ingresar el email registrado y "Enviar enlace"; se confirma con
  un SnackBar o Dialog. Enlace "< Volver al Login".
- **Estados de la UI:** Idle, Loading (círculo de carga), Success y Error con mensajes amigables.
- Al iniciar sesión se va al Inicio y se borra el historial (no se vuelve al login con "atrás").

## Pantallas (diseño en `ingenieria/diseno_ui.webp`)
1. **Splash:** logo "S" violeta y texto SOUNDCRAFT sobre fondo negro.
2. **Login, Registro y Recuperar clave** (con el estilo oscuro de SoundCraft).
3. **Inicio / Categorías:** barra "Buscar audios..." y grilla de 2 columnas con
   tarjetas de colores, una por categoría.
4. **Detalle de categoría:** encabezado con degradado rosa y el nombre (ej. EXPLOSIONES),
   botón "Agregar pista +" y lista de pistas (nombre + autor).
5. **Panel de reproducción:** grilla 4x4 de pads de colores, menú de tres puntos
   (Seleccionar y editar, Agregar a la fila, Compartir) y reproductor abajo
   (título, categoría, barra de progreso, aleatorio, anterior, play, siguiente, repetir).
6. **Perfil:** datos del usuario y "Cerrar sesión".

Barra inferior en las pantallas principales: Inicio, Buscar, Reproducir, Perfil.

## Estilo visual
- Tema oscuro, alto contraste, botones táctiles amplios.
- Fondo negro, color principal violeta (logo) y rosa/magenta en degradados.
- Tipografía en mayúsculas y negrita para títulos y tarjetas.
- Material 3: OutlinedTextField, Button/TextButton, Checkbox,
  CircularProgressIndicator, Scaffold, LazyVerticalGrid, Cards.

## Dominios
- Autenticación (iniciar sesión, crear cuenta, recuperar contraseña).
- Gestión de recursos (subir, clasificar, listar, reproducir).

## Fuera de alcance (por ahora)
- Autenticación real con servidor (a futuro: Firebase Authentication o API REST).
- Nube o sincronización entre dispositivos (todo queda en el teléfono).
- Edición de audio (recortar, aplicar efectos).
- Música en vivo, mezcla o funciones de DJ.
