// build.gradle.kts (Project Level)
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    // It's a good practice to manage the KSP version via an alias as well
    // but for a direct fix, you can specify it here.
    // Ensure this version aligns with your Kotlin version.
    id("com.google.devtools.ksp") version "2.0.0-1.0.21" apply false // Example of a valid version
}
