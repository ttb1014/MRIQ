pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
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

rootProject.name = "MRIQ"
include(":core:network")
include(":core:database")
include(":core:ui")
include(":core:design-system")
include(":core:model")
include(":demo")
include(":core:data")
include(":feature:quiz")
include(":mock")
include(":test:database")
include(":core:local")
include(":feature:quiz-feed")
