// settings.gradle.kts

pluginManagement {
    repositories {
        google()
        mavenCentral()
        // ¡IMPORTANTE! Añade esta línea para que Gradle encuentre el plugin KSP y otros.
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "wiki_repo_amr_smh"
include(":app")
