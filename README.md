# Wiki REPO - Android App

Aplicación Android para gestionar y catalogar objetos recuperados (Loot) del juego R.E.P.O.

**Desarrollada por:** Alejandro Mejías Ramírez y Samuel Morán Hernández

---

## Descripción

Wiki REPO es una companion app para R.E.P.O. desarrollada con Jetpack Compose que permite gestionar y catalogar objetos recuperados durante las partidas.

### Funcionalidades Principales

- **Loot Log**: Registrar y gestionar objetos recuperados (valores, rareza, estado)
- **Bestiario**: Catálogo de enemigos con nivel de peligro y tácticas
- **Tienda**: Catálogo de items comprables con precios en SURPLUS
- **Ajustes**: Configuración de tema, ordenación y filtros persistidos

---

## Requisitos Técnicos

| Requisito | Versión |
|-----------|---------|
| Android Studio | Hedgehog (2023.1.1)+ |
| Kotlin | 1.9.22 |
| Min SDK | 24 (Android 7.0) |
| Target SDK | 36 |

### Tecnologías

- Jetpack Compose + Material 3
- Room Database (persistencia local)
- DataStore Preferences (configuración)
- Navigation Compose (rutas tipadas)
- MVVM + Coroutines/Flow
- Service Locator (inyección de dependencias)


---

## Instalación

### 1. Clonar el Repositorio

```bash
git clone https://github.com/tu-usuario/wiki-repo-amr-smh.git
cd wiki-repo-amr-smh
```

### 2. Abrir en Android Studio

1. Abre Android Studio
2. File → Open y selecciona la carpeta del proyecto
3. Espera a que Gradle Sync termine (2-3 minutos)

### 3. Ejecutar

1. Conecta un dispositivo o inicia un emulador
2. Click en Run (Shift + F10)
3. La app se instalará automáticamente

**Alternativa (línea de comandos):**

```bash
./gradlew installDebug
adb shell am start -n com.amrsmh.wiki_repo_amr_smh/.MainActivity
```

---

## Arquitectura

Wiki REPO sigue el patrón **MVVM** con separación clara de capas:

```
UI Layer (Compose)
    ↓
ViewModel Layer (StateFlow)
    ↓
Repository Layer (Flows)
    ↓
Data Layer (Room + DataStore)
```

### Flujo de Datos

1. **UI** llama funciones del ViewModel
2. **ViewModel** ejecuta operaciones en el Repository
3. **Repository** accede a Room/DataStore
4. **Room/DataStore** emiten cambios mediante Flows
5. **UI** se actualiza automáticamente con `collectAsState()`

---

## Estructura del Proyecto

```
app/src/main/java/com/amrsmh/wiki_repo_amr_smh/
├── data/                           # Capa de datos
│   ├── local/                      # Room Database
│   │   ├── RoomDb.kt               # Definición BD
│   │   ├── dao/LootDao.kt          # Operaciones BD
│   │   └── entities/LootEntity.kt  # Tabla Room
│   ├── datastore/                  # DataStore
│   │   └── PreferencesManager.kt   # Preferencias
│   └── repository/
│       └── LootRepository.kt       # Lógica de datos
├── domain/
│   └── models/LootItem.kt          # Modelo de negocio
├── ui/                             # Capa de presentación
│   ├── components/                 # Componentes reutilizables
│   ├── screens/                    # Pantallas
│   │   ├── loot/
│   │   ├── bestiary/
│   │   ├── shop/
│   │   └── settings/
│   ├── viewmodel/                  # ViewModels
│   ├── navigation/                 # Navegación tipada
│   └── theme/                      # Material 3 Theme
├── di/ServiceLocator.kt            # Inyección de dependencias
└── MainActivity.kt                 # Punto de entrada
```

---

## Componentes Principales

### Room Database
- Singleton con persistencia local
- Entity LootEntity con índices en `createdAt` e `isFavorite`
- DAO con `Flow<List<...>>` para observación reactiva
- Queries type-safe en tiempo de compilación

### Repository
- Mapeo Entity ↔ Domain
- Expone Flows para la UI
- Funciones suspend para escrituras

### ViewModel
- StateFlow para gestionar estado de UI
- Operaciones en `viewModelScope` (canceladas automáticamente)
- Integración con Repository

### DataStore
- Persistencia de preferencias sin SharedPreferences
- Type-safe acceso a datos
- Reactivo mediante Flows

### Navegación
- Routes con `@Serializable`
- Parámetros tipados (sin Strings mágicos)
- Type-safe en compile-time


---

## Configuración de Gradle

### Plugins Requeridos

```kotlin
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.22"
    id("com.google.devtools.ksp") version "1.9.22-1.0.17"
}
```

### Dependencias Principales

```kotlin
dependencies {
    // Compose + Material 3
    implementation(platform("androidx.compose:compose-bom:2024.02.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    
    // ViewModel + Lifecycle
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")
    
    // Navigation Compose + Serialización
    implementation("androidx.navigation:navigation-compose:2.7.7")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
    
    // Room Database
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")
    
    // DataStore Preferences
    implementation("androidx.datastore:datastore-preferences:1.0.0")
}
```

**Nota:** Sin hacer `Gradle Sync` después de cambios en `build.gradle.kts`, Android Studio no reconoce las nuevas clases (@Dao, @Entity, @Serializable, etc.)

---

## Decisiones Técnicas

| Decisión | Razón |
|----------|-------|
| **MVVM** | Separación de responsabilidades, testeable, reactivo |
| **Room** | Type-safe, menos boilerplate, Flows nativos |
| **DataStore** | Coroutines-first, type-safe, transaccional |
| **Service Locator** | Simplicidad en proyecto pequeño; escalable a Hilt |
| **Navegación Tipada** | Seguridad en compile-time, sin Strings mágicos |
| **Jetpack Compose** | UI moderna, declarativa, menos XML |

---

## Próximas Mejoras

- Búsqueda y filtros avanzados en tiempo real
- Integración de imágenes desde galería
- Dark mode automático según hora del día
- Exportar/importar datos a JSON
- Sincronización con Firebase
- Gráficas de estadísticas (valor total, items por categoría)



