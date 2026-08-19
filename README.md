# Pepinillo Rick 🥒

Una aplicación Android moderna y de alto rendimiento diseñada para explorar el universo de Rick & Morty. Este proyecto consume la [API pública de Rick & Morty](https://rickandmortyapi.com/documentation#rest) y ha sido construido enfocándose en la escalabilidad, el rendimiento y una experiencia de usuario inmersiva.

[Android Build](https://github.com/criswerner/PepinilloRick/actions)

## 🚀 Arquitectura y Organización

El proyecto implementa **Clean Architecture** dividida en tres capas fundamentales, garantizando una separación clara de responsabilidades y facilidad de testeo:

*   **Capa de Dominio**: El corazón de la app. Contiene los modelos de negocio (`Character`), las interfaces de repositorio y los Casos de Uso (`GetCharactersUseCase`, `ToggleFavoriteUseCase`, `GetFavoriteCharactersUseCase`, `GetCharacterByIdUseCase`, `ObserveCharacterUseCase`). Es puramente Kotlin y no tiene dependencias de Android, manteniendo la lógica de negocio aislada.
*   **Capa de Datos**: Implementa la lógica de persistencia y red. Utiliza **Room** como Fuente Única de Verdad (SSOT) y **Retrofit** para la comunicación con la API. Incluye mapeadores para transformar DTOs en entidades y modelos de dominio.
*   **Capa de UI (Presentación)**: Basada en **Jetpack Compose** y el patrón **MVVM**. Implementa flujos de datos unidireccionales (UDF) para una gestión de estado predecible.

## 🛠 Especificaciones Técnicas

*   **Lenguaje**: 100% Kotlin.
*   **Interfaz de Usuario**: 100% Jetpack Compose.
*   **Build System**: Gradle con Kotlin DSL (`.kts`).
*   **Android SDK**:
    *   Min SDK: 24
    *   Target SDK: 36
    *   Compile SDK: 37 (Extension level 1)

## 💡 Decisiones Técnicas y Enfoques

### 📡 Offline-First (Network Bound Resource)
La aplicación prioriza la disponibilidad inmediata de los datos mediante el patrón **Network Bound Resource**:
1.  Se emiten instantáneamente los datos cacheados en la DB (Estado `Loading` con datos).
2.  Se sincroniza con la API en segundo plano de forma transparente.
3.  Si hay éxito, se actualiza la DB y Room notifica automáticamente el cambio (Estado `Success`).
4.  Si falla (ej. falta de internet), se emite un error estructurado pero **se mantienen los datos locales**, permitiendo una navegación ininterrumpida.

### 💖 Gestión de Favoritos
Los usuarios pueden marcar personajes como favoritos para acceder a ellos rápidamente. Esta información se gestiona mediante una tabla dedicada en **Room** y se persiste **únicamente de manera local**.

### 🎭 Estados Reactivos Sellados
Para evitar "estados imposibles", utilizamos `sealed interface` para representar el estado de cada pantalla de forma atómica (ej. `InitialLoading`, `InitialError`, `Success`). Esto garantiza que la UI sea determinista y fácil de razonar.

### ⚡ Optimización de Recomposición e Interfaz
Para garantizar un scroll suave a 60fps y una UI reactiva, hemos implementado:
*   **Lectura Diferida de Estado**: Separación de componentes en `Screen` (orquestación) y `Content` (visualización), pasando el estado mediante lambdas (`uiStateProvider`) para minimizar las recomposiciones innecesarias.
*   **Colecciones Inmutables**: Uso de `kotlinx-collections-immutable` para que el compilador de Compose reconozca las listas como estables.
*   **Anotación @Immutable**: Aplicada a los modelos de UI para permitir el "Skipping" de redibujado.
*   **Lambdas Memorizadas**: Uso de `remember` para estabilizar callbacks entre componentes.

### 🎨 Identidad Visual "Portal & Neon"
*   **Diseño Custom**: Estética neón inspirada en la serie con efectos de resplandor y portales dimensionales.
*   **Skeleton Loading**: Uso de *Shimmer effect* para una carga inicial fluida que anticipa la estructura del contenido.
*   **Dynamic Color**: Soporte nativo para Material You (Android 12+), adaptando la paleta de colores al fondo de pantalla del usuario.

## 📖 Guía de Uso

1.  **Exploración**: Al abrir la aplicación, se presenta la lista completa de personajes del multiverso.
2.  **Detalle**: Al hacer tap sobre cualquier personaje, navegarás a una pantalla detallada con su información biográfica, origen y última ubicación conocida.
3.  **Favoritos**: Puedes marcar o desmarcar personajes como favoritos pulsando el icono del corazón, tanto desde la lista principal como desde la pantalla de detalle.
4.  **Sección de Favoritos**: A través de la barra de navegación inferior, puedes acceder a tu colección personalizada de personajes guardados localmente.

## 🧪 Estrategia de Testing

*   **Tests Unitarios**: Cobertura exhaustiva de ViewModels, Mappers y Repositorios utilizando **MockK** y `kotlinx-coroutines-test`.
*   **Tests de Componentes**: Pruebas de instrumentación con `createComposeRule` para verificar la integridad visual y el comportamiento de los componentes.

## 📦 Instalación

La aplicación utiliza **GitHub Actions** para CI/CD. Cada versión estable genera automáticamente un ejecutable.

1.  Ve a la sección de [Releases](https://github.com/criswerner/PepinilloRick/releases).
2.  Descarga el último release generado (en este caso, `app-debug.apk`).
3.  Instálalo en tu dispositivo Android.

## ⚖️ Trade-offs

*   **Paginación Manual vs Paging 3**: Se optó por una gestión manual de la paginación con `RemoteKeys` para **preservar la pureza de la capa de dominio**. El uso de Paging 3 habría forzado dependencias de `androidx.paging` (PagingData) en el dominio, contaminando una capa que debe ser puramente agnóstica a la plataforma. La paginación manual nos otorga control total sobre la Fuente Única de Verdad y simplifica los estados de carga.
*   **Single Module vs Multi-module**: Aunque el proyecto cuenta con estructura modular (`:app`, `:platform`), se prioriza una separación lógica clara por paquetes para agilizar el desarrollo manteniendo la escalabilidad futura.

---
Desarrollado con 🥒 por [Cristian Werner](https://github.com/criswerner)
