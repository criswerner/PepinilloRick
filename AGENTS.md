# Guía del Repositorio y Contexto para Agentes de IA (`AGENTS.md`)

Este documento proporciona el contexto arquitectónico, técnico y las directrices obligatorias del proyecto **PepinilloRick** para asistir a agentes de Inteligencia Artificial (IA) en futuras iteraciones de desarrollo, refactorización y resolución de errores.

---

## 📌 1. Visión General del Proyecto

**PepinilloRick** 🥒 es una aplicación Android moderna y de alto rendimiento diseñada para explorar el universo de Rick & Morty consumiendo la [API pública de Rick & Morty](https://rickandmortyapi.com/documentation#rest).

### Enfoque Principal
- **Single Activity Architecture**: Arquitectura de actividad única basada en `MainActivity.kt`, donde toda la navegación entre pantallas (lista, detalle, favoritos) se gestiona mediante `Navigation Compose`.
- **UI 100% Jetpack Compose**: Toda la interfaz gráfica está construida exclusivamente en Compose.
- **Experiencia Inmersiva & Neón**: Estética inspirada en la serie ("Portal & Neon") con animaciones customizadas (corazones flotantes, escalado de portal, transiciones fluidas de pantalla).
- **Offline-First**: La aplicación garantiza disponibilidad inmediata de datos cacheados de forma local en SQLite/Room sincronizando con la red en segundo plano.
- **Rendimiento UI**: Optimización exhaustiva de recomposiciones en Jetpack Compose a 60fps.

---

## 🏗️ 2. Arquitectura del Proyecto

El proyecto está estructurado siguiendo los principios de **Clean Architecture** combinados con **MVVM** (Model-View-ViewModel), el patrón **Single Activity** y **UDF** (Unidirectional Data Flow).

### Estructura Modular
- `:app`: Módulo principal Android conteniendo la UI, ViewModels, DI (Hilt) y navegación.
- `:platform`: Módulo complementario conteniendo utilidades o abstracciones compartidas.

### Capas Arquitectónicas

```
┌─────────────────────────────────────────────────────────┐
│                      Capa de UI                         │
│   Jetpack Compose | ViewModels | Navigation Compose     │
└────────────────────────────┬────────────────────────────┘
                             │
                             ▼
┌─────────────────────────────────────────────────────────┐
│                    Capa de Dominio                      │
│   Use Cases | Domain Models | Repository Interfaces     │
│   (Pure Kotlin - Sin dependencias de Android / UI)       │
└────────────────────────────▲────────────────────────────┘
                             │
                             │
┌────────────────────────────┴────────────────────────────┐
│                    Capa de Datos                        │
│   Repositories Impl | Room DB (SSOT) | Retrofit API     │
└─────────────────────────────────────────────────────────┘
```

1. **Capa de Dominio (`domain`)**:
   - Contiene la lógica de negocio pura en Kotlin agnóstica a la plataforma.
   - **Modelos**: `Character`, `Status`, `Gender`, `UiError`, etc.
   - **Interfaces de Repositorio**: `CharacterRepository`.
   - **Casos de Uso**: `GetCharactersUseCase`, `ToggleFavoriteUseCase`, `GetFavoriteCharactersUseCase`, `GetCharacterByIdUseCase`, `ObserveCharacterUseCase`.

2. **Capa de Datos (`data`)**:
   - Implementa `CharacterRepository` actuando Room DB como Fuente Única de Verdad (SSOT - Single Source of Truth).
   - **Network Bound Resource**: Emite caché local de DB $\rightarrow$ realiza fetch de API $\rightarrow$ actualiza DB $\rightarrow$ Room notifica a los suscriptores automáticamente.
   - **Mapeadores**: Transformación entre DTOs de Retrofit, Entidades de Room y Modelos de Dominio.

3. **Capa de Presentación (`ui`)**:
   - **Single Activity**: Única actividad `MainActivity.kt` como punto de entrada y contenedor raíz de la UI.
   - **100% Jetpack Compose** con Material 3.
   - **Estados Sellados Atómicos**: Uso de `sealed interface` (`CharacterListUiState`, `CharacterDetailUiState`) para evitar estados imposibles.
   - **Navegación**: `Navigation Compose` en `MainActivity.kt` con transiciones personalizadas (`slideInVertically`, `fadeIn`, etc.).
   - **Componentes Custom**:
     - `FloatingHearts.kt`: Sistema de partículas para corazones flotantes a pantalla completa (`FloatingHeartsOverlay`) o ráfagas locales en botones (`LocalFloatingHeartsBurst`).
     - `CharacterItem`, `FavoriteButton`, `CustomAsyncImage`.

---

## 🛠️ 3. Tech Stack y Frameworks

| Categoría | Tecnología / Librería | Versión |
| :--- | :--- | :--- |
| **Lenguaje** | Kotlin | `2.4.10` |
| **Build System** | Gradle (Kotlin DSL `.kts`) | `9.5.0` |
| **Android Plugins (AGP)** | Android Application / Library | `9.3.1` |
| **Compiler & Processing** | KSP (Kotlin Symbol Processing) | `2.3.6` |
| **SDK Levels** | Min SDK: 24 \| Target: 36 \| Compile: 37 | - |
| **UI Framework** | Jetpack Compose (BOM) | `2026.08.00` |
| **Design System** | Material 3 & Material Icons Extended | - |
| **Inyección de Dependencias** | Google Hilt | `2.60.1` |
| **Persistencia Local** | Room Database (Runtime, KTX, Compiler) | `2.8.4` |
| **Red & HTTP** | Retrofit 2 + Gson Converter + OkHttp | `3.0.0` / `5.4.0` |
| **Carga de Imágenes** | Coil Compose | `2.7.0` |
| **Colecciones Inmutables** | `kotlinx-collections-immutable` | `0.5.1` |
| **Splash Screen** | `androidx.core:core-splashscreen` | `1.0.1` |

---

## ⛔ 4. Restricciones y Reglas de Código Obligatorias

Al modificar o crear nuevo código en este repositorio, se deben cumplir estrictamente las siguientes reglas:

1. **Documentación KDoc Obligatoria**:
   - **Todas las clases, componentes composables e interfaces deben tener un bloque KDoc (`/** ... */`) descriptivo.**
   - Explicar el propósito del componente, sus parámetros y comportamientos relevantes.

2. **UI Exclusivamente en Jetpack Compose**:
   - **Todas las pantallas y vistas deben ser desarrolladas en Jetpack Compose.** No utilizar layouts en XML ni Views tradicionales de Android.

3. **100% Cobertura de Tests en Dominio**:
   - **La capa de dominio (`domain`) debe estar 100% cubierta con pruebas unitarias.** Todos los Casos de Uso y entidades de negocio deben contar con su correspondiente test unitario en `test/`.

4. **Protección de Datos Sensibles**:
   - **NUNCA exponer datos sensibles** (claves de API secretas, credenciales, tokens privados o información personal de usuario) en código fuente, archivos públicos o logs de consola.

5. **Pureza Absoluta de la Capa de Dominio**:
   - La capa de dominio **NUNCA** debe importar bibliotecas de Android o AndroidX (`androidx.*`, `android.*`, `androidx.paging.*`).
   - Mantener los modelos de dominio agnósticos y libres de anotaciones de UI/Persistencia.

6. **Estabilidad y Rendimiento en Compose**:
   - Utilizar `ImmutableList` de `kotlinx-collections-immutable` para listas expuestas en modelos de UI.
   - Aplicar lectura diferida de estado pasando lambdas (`uiStateProvider`) a composables de contenido para evitar recomposiciones masivas.
   - Recordar callbacks con `remember` para preservar las optimizaciones de "Skipping".

7. **Manejo de Estados (UDF)**:
   - El `ViewModel` expone `StateFlow<UiState>`, la UI consume mediante `collectAsStateWithLifecycle()` y emite eventos hacia el ViewModel.

---

## 🧪 5. Estrategia de Pruebas (Testing)

El proyecto cuenta con suites de pruebas unitarias para garantizar el correcto funcionamiento de repositorios, ViewModels y casos de uso.

### Tecnologías de Test
- **Testing Unitario**: JUnit 4 (`4.13.2`)
- **Mocking**: MockK (`1.13.10`)
- **Corrutinas Async**: `kotlinx-coroutines-test` (`1.8.0`)
- **UI & Instrumentation**: `androidx.compose.ui:ui-test-junit4`

### Comandos de Verificación
- **Compilación Kotlin**:
  ```bash
  ./gradlew compileDebugKotlin
  ```
- **Ejecutar Pruebas Unitarias**:
  ```bash
  ./gradlew test
  ```
- **Ejecutar Lint / Build Completo**:
  ```bash
  ./gradlew assembleDebug
  ```

---

## 💡 6. Directivas para Agentes de IA

Cuando trabajes en esta base de código:
1. **Documentación KDoc**: Asegúrate de incluir comentarios KDoc explicativos en toda nueva clase, interface o composable que crees o refactores.
2. **Cobertura de tests**: Si agregas o modificas un caso de uso en `domain`, crea/actualiza su test unitario correspondiente.
3. **Verificación tras cambios**: Siempre ejecuta `./gradlew compileDebugKotlin` y `./gradlew test` tras modificar lógica o interfaz.
4. **Ubicación de código**:
   - `ui/`: Pantallas Compose, componentes visuales, viewmodels y modelos de UI.
   - `domain/`: Casos de uso, interfaces de repositorio y entidades de dominio.
   - `data/`: DAO de Room, servicios Retrofit, repositorios y entidades DB/DTO.
5. **No romper contratos de API ni de Seguridad**: Mantener el patrón Network Bound Resource y asegurar cero exposición de datos sensibles.
