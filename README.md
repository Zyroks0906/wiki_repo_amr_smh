# Wiki REPO - Android App

![Android](https://img.shields.io/badge/Platform-Android-brightgreen) ![Kotlin](https://img.shields.io/badge/Language-Kotlin-7f52ff) ![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-Material3-blueviolet)

**Aplicación Android para gestionar objetos, enemigos y equipamiento del juego R.E.P.O.**

**Desarrollada por:** Alejandro Mejías Ramírez y Samuel Morán Hernández

---

## Tabla de Contenido

- [Descripción](#descripción)
- [Funcionalidades Principales](#funcionalidades-principales)
- [Tecnologías](#tecnologías)
- [Requisitos](#requisitos)
- [Instalación y Ejecución](#instalación-y-ejecución)
- [Arquitectura](#arquitectura)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [Características Destacadas](#características-destacadas)
- [Decisiones Técnicas](#decisiones-técnicas)
- [Dependencias Principales](#dependencias-principales)
- [Autores](#autores)
- [Licencia](#licencia)

---

## Descripción

Wiki REPO es una aplicación companion para el juego **R.E.P.O.**, desarrollada con **Jetpack Compose** y **Material 3**. Permite a los jugadores catalogar y gestionar información del juego de forma organizada y persistente.

### Datos Predeterminados Incluidos

- **15 objetos** de botín de diferentes ubicaciones
- **12 tipos** de enemigos con distintos niveles de peligro
- **20 artículos** de tienda (armas, consumibles y utilidades)

---

## Funcionalidades Principales

### Botín

- Registro de objetos por **ubicación** (Genérico, Mágico, Mansión, Ártico, Museo)
- Sistema de **valoración** y clasificación por **peso**
- **Notas personalizables** y sistema de **favoritos**

### Bestiario

- Catálogo de enemigos con **niveles de peligro** (1-3)
- **Código de colores** (Verde / Amarillo / Rojo)
- **Método de detección** (Visión / Audio)
- **Notas tácticas** por enemigo

### Tienda

- Catálogo de artículos por **categoría** (Armas, Consumibles, Utilidad)
- **Precios** y **descripciones** detalladas
- Sistema de **lista de deseos**

### Configuración

- **Tema:** claro / oscuro / sistema
- **Ordenamiento** personalizado
- **Filtro** de favoritos
- **Persistencia** de preferencias

---

## Tecnologías

### Core

- **Jetpack Compose** - UI declarativa moderna
- **Material 3** - Sistema de diseño de Google
- **Kotlin Coroutines + Flow** - Programación reactiva asíncrona

### Persistencia

- **Room Database** - Base de datos local con type-safety
- **DataStore Preferences** - Almacenamiento de preferencias

### Arquitectura

- **MVVM** - UI / ViewModel / Repository / Data
- **Service Locator** - Inyección de dependencias
- **Navigation Compose** - Navegación entre pantallas
---

## Requisitos

| Componente | Versión |
|-----------|---------|
| **Android Studio** | Hedgehog (2023.1.1) o superior |
| **Kotlin** | 1.9.22 |
| **Min SDK** | 24 (Android 7.0) |
| **Target SDK** | 36 |

---
## Instalación y Ejecución

### 1. Clonar el Repositorio

git clone https://github.com/Zyroks0906/wiki_repo_amr_smh.git
cd wiki_repo_amr_smh

### 2. Abrir y Ejecutar en Android Studio

1. Abre **Android Studio**
2. **File → Open** → Selecciona la carpeta del proyecto
3. Espera a que **Gradle Sync** termine
4. Conecta un dispositivo o inicia un emulador
5. Click en **Run** o presiona **Shift + F10**

### 3. Limpiar Datos (si es necesario)

Si ya tenías una versión anterior instalada:

adb uninstall com.amrsmh.wiki_repo_amr_smh

---

## Arquitectura

La aplicación sigue el patrón **MVVM** con separación clara de capas:
```
UI Layer (Compose)
    ↓ collectAsState()
ViewModel Layer (StateFlow)
    ↓ suspend functions
Repository Layer (Flows)
    ↓ Room queries
Data Layer (Room + DataStore)
```

---

## Estructura del Proyecto
```
app/src/main/java/com/amrsmh/wiki_repo_amr_smh/
│
├── data/
│   ├── local/           # Room Database (DAOs + Entities)
│   ├── datastore/       # PreferencesManager
│   └── repository/      # Repositorios (mapeo Entity ↔ Model)
│
├── domain/
│   └── models/          # Modelos de negocio (LootItem, Monster, ShopItem)
│
├── ui/
│   ├── components/      # Componentes reutilizables
│   ├── screens/         # Pantallas (Main, Loot, Bestiary, Shop, Settings)
│   ├── viewmodel/       # ViewModels con StateFlow
│   ├── navigation/      # NavGraphs
│   └── theme/           # Material 3 Theme
│
├── di/
│   └── ServiceLocator.kt
│
└── MainActivity.kt
```

---

## Características Destacadas

### Datos Iniciales Automáticos

Al instalar por primera vez se insertan datos de ejemplo mediante callback de Room.

### Tema Dinámico

Cambios de tema aplicados **inmediatamente** (RepoTheme observa preferencias vía Flow).

### Filtrado y Ordenamiento Reactivo

ViewModels combinan Flows (datos + preferencias) para recomponer UI.

### Listas Eficientes

`LazyColumn` con `key = { it.id }` para renderizado optimizado.

### Dropdowns Controlados

`ExposedDropdownMenuBox` para evitar errores de tipeo en datos categóricos.

---

## Decisiones Técnicas

| Decisión | Razón |
|----------|-------|
| **MVVM** | Separación de responsabilidades, testeable |
| **Room** | Type-safety y soporte nativo para Flows |
| **DataStore** | Reemplazo moderno de SharedPreferences |
| **StateFlow** | Reactividad y compatibilidad con Compose |
| **Service Locator** | Simplicidad para proyecto pequeño |
| **Jetpack Compose** | UI moderna y declarativa |

---

## Dependencias Principales

// Compose + Material 3
implementation(platform("androidx.compose:compose-bom:2024.02.00"))
implementation("androidx.compose.material3:material3")

// ViewModel + Lifecycle
implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")

// Navigation + Serialization
implementation("androidx.navigation:navigation-compose:2.7.7")
implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")

// Room Database
implementation("androidx.room:room-runtime:2.6.1")
implementation("androidx.room:room-ktx:2.6.1")
ksp("androidx.room:room-compiler:2.6.1")

// DataStore Preferences
implementation("androidx.datastore:datastore-preferences:1.0.0")

---

## Autores

**Alejandro Mejías Ramírez** - [@Zyroks0906](https://github.com/Zyroks0906)

**Samuel Morán Hernández** - [@SamuelMoranHdz](https://github.com/SamuelMoranHdz)

---

## Licencia

Proyecto académico desarrollado para la asignatura de **Desarrollo de Aplicaciones Móviles**.

---

**Repositorio:** [https://github.com/Zyroks0906/wiki_repo_amr_smh](https://github.com/Zyroks0906/wiki_repo_amr_smh)

**Versión:** 1.0.0
