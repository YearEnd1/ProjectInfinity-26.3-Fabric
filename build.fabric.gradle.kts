@file:Suppress("UnstableApiUsage")

plugins {
    id("fabric-loom")
    id("dev.kikugie.postprocess.jsonlang")
    id("me.modmuss50.mod-publish-plugin")
}

val minecraft = stonecutter.current.version
val accesswidener = "$minecraft.accesswidener"
version = "${property("mod.version")}+${property("deps.minecraft")}"
if (property("mod.test_build") != "0") version = "$version-tb${property("mod.test_build")}"

tasks.named<ProcessResources>("processResources") {
    fun prop(name: String) = project.property(name) as String

    val props = HashMap<String, String>().apply {
        this["version"] = "$version"
        this["minecraft"] = prop("mod.mc_dep_fabric")
        this["aw_file"] = accesswidener
    }

    filesMatching(listOf("fabric.mod.json", "META-INF/neoforge.mods.toml", "META-INF/mods.toml")) {
        expand(props)
    }
}

version = "$version-fabric"
base.archivesName = property("mod.id") as String

jsonlang {
    languageDirectories = listOf("assets/${property("mod.id")}/lang")
    prettyPrint = true
}

repositories {
    maven {
        name = "Parchment Mappings"
        url = uri("https://maven.parchmentmc.org")
        content {
            includeGroupAndSubgroups("org.parchmentmc")
        }
    }
    maven {
        name = "Modrinth"
        url = uri("https://api.modrinth.com/maven")
        content {
            includeGroupAndSubgroups("maven.modrinth")
        }
    }
    maven {
        name = "Terraformers (Mod Menu)"
        url = uri("https://maven.terraformersmc.com/releases/")
        content {
            includeGroupAndSubgroups("com.terraformersmc")
            includeGroupAndSubgroups("dev.emi")
        }
    }
    maven {
        name = "Architectury"
        url = uri("https://maven.architectury.dev/")
        content {
            includeGroupAndSubgroups("dev.architectury")
            includeGroupAndSubgroups("me.shedaniel")
        }
    }
    maven {
        name = "Jitpack (DimLib)"
        url = uri("https://jitpack.io")
        content {
            includeGroupAndSubgroups("com.github.iPortalTeam")
            includeGroupAndSubgroups("com.github.qouteall")
        }
    }
    maven {
        name = "CC: Tweaked"
        url = uri("https://maven.squiddev.cc")
        content {
            includeGroupAndSubgroups("cc.tweaked")
        }
    }
    maven {
        name = "Fabricators of Create (Snapshots)"
        url = uri("https://mvn.devos.one/snapshots")
        content {
            includeGroupAndSubgroups("net.createmod")
            includeGroupAndSubgroups("dev.engine-room")
            includeGroupAndSubgroups("io.github.fabricators_of_create")
            includeGroupAndSubgroups("com.simibubi")
        }
    }
    maven {
        name = "Fabricators of Create (Releases)"
        url = uri("https://mvn.devos.one/releases")
        content {
            includeGroupAndSubgroups("net.createmod")
            includeGroupAndSubgroups("dev.engine-room")
            includeGroupAndSubgroups("io.github.fabricators_of_create")
            includeGroupAndSubgroups("com.simibubi")
        }
    }
    maven {
        name = "Create, Ponder, Flywheel"
        url = uri("https://maven.createmod.net")
        content {
            includeGroupAndSubgroups("net.createmod")
            includeGroupAndSubgroups("dev.engine-room")
        }
    }
    maven {
        name = "Fuzs Mod Resources"
        url = uri("https://raw.githubusercontent.com/Fuzss/modresources/main/maven/")
        content {
            includeGroupAndSubgroups("fuzs")
        }
    }
    maven {
        name = "reach-entity-attributes"
        url = uri("https://maven.jamieswhiteshirt.com/libs-release")
        content {
            includeGroupAndSubgroups("com.jamieswhiteshirt")
        }
    }
}

dependencies {
    minecraft("com.mojang:minecraft:${property("deps.minecraft")}")
    mappings(loom.layered {
        officialMojangMappings()
        if (hasProperty("deps.parchment"))
            parchment("org.parchmentmc.data:parchment-${property("deps.parchment")}@zip")
    })
    modImplementation("net.fabricmc:fabric-loader:${property("deps.fabric-loader")}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${property("deps.fabric_api")}")
    modImplementation("dev.architectury:architectury-fabric:${property("deps.architectury")}")

    modImplementation("me.shedaniel.cloth:cloth-config-fabric:${property("deps.cloth")}")
    modImplementation("com.terraformersmc:modmenu:${property("deps.modmenu")}")
    if (hasProperty("deps.dimlib")) {
        modApi("com.github.iPortalTeam:DimLib:${property("deps.dimlib")}")
    }
    if (hasProperty("deps.gravity_changer")) {
        modCompileOnly("com.github.qouteall:GravityChanger:v${property("deps.gravity_changer")}-mc${property("deps.minecraft")}") {
            isTransitive = false
        }
    }
    if (hasProperty("deps.emi")) {
        modCompileOnly("dev.emi:emi-fabric:${property("deps.emi")}+${property("deps.minecraft")}:api")
        modLocalRuntime("dev.emi:emi-fabric:${property("deps.emi")}+${property("deps.minecraft")}")
    }
    if (hasProperty("deps.eiv")) {
        modCompileOnly("maven.modrinth:eiv:${property("deps.eiv")}-fabric")
    }


    if (hasProperty("deps.cct")) {
        modImplementation("cc.tweaked:cc-tweaked-${property("deps.minecraft")}-fabric:${property("deps.cct")}")
    } else {
        modCompileOnly("cc.tweaked:cc-tweaked-1.21.1-fabric:1.115.0")
    }
    if (hasProperty("deps.ponder")) {
        modCompileOnly("net.createmod.ponder:Ponder-Fabric-${property("deps.minecraft")}:${property("deps.ponder")}")
    } else {
        modCompileOnly("net.createmod.ponder:Ponder-Fabric-1.21.1:1.0.59")
    }

    if (stonecutter.eval(stonecutter.current.version, ">=1.21")) {
        modCompileOnly("com.simibubi.create:create-fucked-up-1.21.1:6.0.6+053024d") { isTransitive = false }
    } else {
        modCompileOnly("com.simibubi.create:create-fabric-1.20.1:6.0.7.0+mc1.20.1-build.1716")
        { isTransitive = false }
    }

//    val modules = listOf("transitive-access-wideners-v1", "registry-sync-v0", "resource-loader-v0")
//    for (it in modules) modImplementation(fabricApi.module("fabric-$it", property("deps.fabric_api") as String))
}


configurations.all {
    resolutionStrategy {
        force("net.fabricmc:fabric-loader:${property("deps.fabric-loader")}")
        force("net.fabricmc:fabric-api:${property("deps.fabric_api")}")
    }
}


//fabricApi {
//    configureDataGeneration() {
//        outputDirectory = file("$rootDir/src/main/generated")
//        client = true
//    }
//}

tasks.named("processResources") {
    dependsOn(":${stonecutter.current.project}:stonecutterGenerate")
}

tasks {
    processResources {
        exclude("**/neoforge.mods.toml", "**/mods.toml")
    }

    register<Copy>("buildAndCollect") {
        group = "build"
        from(remapJar.map { it.archiveFile })
        into(rootProject.layout.buildDirectory.file("libs/${project.property("mod.version")}"))
        dependsOn("build")
    }
}

java {
    withSourcesJar()
    val javaCompat = if (stonecutter.eval(stonecutter.current.version, ">=1.21")) {
        JavaVersion.VERSION_21
    } else {
        JavaVersion.VERSION_17
    }
    sourceCompatibility = javaCompat
    targetCompatibility = javaCompat
}

val additionalVersionsStr = findProperty("publish.additionalVersions") as String?
val additionalVersions: List<String> = additionalVersionsStr
    ?.split(",")
    ?.map { it.trim() }
    ?.filter { it.isNotEmpty() }
    ?: emptyList()

publishMods {
    file = tasks.remapJar.map { it.archiveFile.get() }
    additionalFiles.from(tasks.remapSourcesJar.map { it.archiveFile.get() })

    type = STABLE
    displayName = "${property("mod.name")} ${property("mod.version")} for ${stonecutter.current.version} Fabric"
    version = "${property("mod.version")}+${property("deps.minecraft")}-fabric"
    changelog = provider { rootProject.file("CHANGELOG-LATEST.md").readText() }
    modLoaders.add("fabric")

    modrinth {
        projectId = property("publish.modrinth") as String
        accessToken = env.MODRINTH_API_KEY.orNull()
        minecraftVersions.add(stonecutter.current.version)
        minecraftVersions.addAll(additionalVersions)
        requires("fabric-api")
        optional("mcqoy")
    }

    curseforge {
        projectId = property("publish.curseforge") as String
        accessToken = env.CURSEFORGE_API_KEY.orNull()
        minecraftVersions.add(stonecutter.current.version)
        minecraftVersions.addAll(additionalVersions)
        requires("fabric-api")
    }
}

loom {
    accessWidenerPath = rootProject.file("src/main/resources/accesswideners/$accesswidener")
}

tasks.processResources {
    filesMatching("fabric.mod.json") {
        expand(mapOf(
            // other properties
            "aw_file" to accesswidener,
        ))
    }
    from(rootProject.file("src/${minecraft}/resources")) {
        include("/*")
    }
}
