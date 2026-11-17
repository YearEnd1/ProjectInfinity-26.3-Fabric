pluginManagement {
    repositories {
        maven("https://maven.quiltmc.org/repository/release/") {
            content { includeGroup("org.quiltmc.parsers") }
        }
        maven("https://maven.kikugie.dev/releases") {
            content { includeGroupByRegex("dev\\.kikugie(\\..*)?") }
        }
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven2.fabricmc.net/") { name = "Fabric" }
        maven("https://maven.neoforged.net/releases/") { name = "NeoForged" }
        maven("https://maven.kikugie.dev/snapshots") { name = "KikuGie" }
        maven("https://maven.kikugie.dev/releases") { name = "KikuGie Releases" }
        maven("https://maven.parchmentmc.org") { name = "ParchmentMC" }
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
    id("dev.kikugie.stonecutter") version "0.9.6"
    id("dev.kikugie.loom-back-compat") version "0.4"
}

stonecutter {
    create(rootProject) {
        fun match(version: String, vararg loaders: String) = loaders
            .forEach { version("$version-$it", version).buildscript = "build.$it.gradle.kts" }

        // This port supports the 26.3 Fabric target.
        match("26.3", "fabric")

        vcsVersion = "26.3-fabric"
    }
}
