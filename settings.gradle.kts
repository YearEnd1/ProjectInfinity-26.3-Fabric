pluginManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
        maven {
            name = "Fabric"
            url = uri("https://maven.fabricmc.net/")
            content {
                includeGroupAndSubgroups("net.fabricmc")
            }
        }
        maven {
            name = "NeoForged"
            url = uri("https://maven.neoforged.net/releases/")
            content {
                includeGroupAndSubgroups("net.neoforged")
            }
        }
        maven {
            name = "KikuGie Releases"
            url = uri("https://maven.kikugie.dev/snapshots")
            content {
                includeGroupAndSubgroups("dev.kikugie")
                includeGroupAndSubgroups("org.quiltmc")
            }
        }
        maven {
            name = "KikuGie Releases"
            url = uri("https://maven.kikugie.dev/releases")
            content {
                includeGroupAndSubgroups("dev.kikugie")
            }
        }
        maven {
            name = "Parchment Mappings"
            url = uri("https://maven.parchmentmc.org")
            content {
                includeGroup("org.parchmentmc.data")
            }
        }
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
    id("dev.kikugie.stonecutter") version "0.7.11"
}

stonecutter {
    create(rootProject) {
        fun match(version: String, vararg loaders: String) = loaders
            .forEach { version("$version-$it", version).buildscript = "build.$it.gradle.kts" }

        match("1.20.1", "fabric", "forge")
        match("1.21.1", "fabric", "neoforge")
        match("1.21.8", "fabric", "neoforge")

        vcsVersion = "1.21.1-fabric"
    }
}