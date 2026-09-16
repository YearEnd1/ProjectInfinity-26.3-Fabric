plugins {
    id("net.neoforged.moddev.legacyforge")
    id("dev.kikugie.postprocess.jsonlang")
    id("me.modmuss50.mod-publish-plugin")
    id("maven-publish")
}

val minecraft = stonecutter.current.version
val mcVersion = stonecutter.current.project.substringBeforeLast('-')
version = "${property("mod.version")}+${property("deps.minecraft")}"
if (property("mod.test_build") != "0") version = "$version-tb${property("mod.test_build")}"

tasks.named<ProcessResources>("processResources") {
    fun prop(name: String) = project.property(name) as String

    val props = HashMap<String, String>().apply {
        this["version"] = "$version"
        this["minecraft"] = prop("mod.mc_dep_forge")
    }

    filesMatching(listOf("neoforge.mod.json", "META-INF/neoforge.mods.toml", "META-INF/mods.toml")) {
        expand(props)
    }
}

version = "$version-forge"
base.archivesName = property("mod.id") as String

jsonlang {
    languageDirectories = listOf("assets/${property("mod.id")}/lang")
    prettyPrint = true
}


repositories {
    maven ( "https://maven.minecraftforge.net" ) {
        name = "Minecraft Forge"
    }
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
        }
    }
    maven {
        name = "Fabricators of Create (Releases)"
        url = uri("https://mvn.devos.one/releases")
        content {
            includeGroupAndSubgroups("net.createmod")
            includeGroupAndSubgroups("dev.engine-room")
            includeGroupAndSubgroups("io.github.fabricators_of_create")
        }
    }
    maven {
        name = "Create, Ponder, Flywheel"
        url = uri("https://maven.createmod.net")
        content {
            includeGroupAndSubgroups("net.createmod")
            includeGroupAndSubgroups("dev.engine-room")
            includeGroupAndSubgroups("com.simibubi")
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
        name = "Registrate"
        url = uri("https://maven.ithundxr.dev/mirror")
        content {
            includeGroupAndSubgroups("com.tterrag")
        }
    }
    maven {
        name = "Sinytra (FFAPI)"
        url = uri("https://maven.su5ed.dev/releases")
        content {
            includeGroupAndSubgroups("dev.su5ed")
        }
    }
    maven {
        name = "Sinytra"
        url = uri("https://maven.su5ed.dev/releases")
        content {
            includeGroupAndSubgroups("org.sinytra")
            includeGroupAndSubgroups("dev.su5ed")
        }
    }
}

legacyForge {
    version = property("deps.forge") as String
    validateAccessTransformers = true

    if (hasProperty("deps.parchment")) parchment {
        val (mc, ver) = (property("deps.parchment") as String).split(':')
        mappingsVersion = ver
        minecraftVersion = mc
    }

    runs {
        register("client") {
            gameDirectory = file("run/")
            client()
        }
        register("server") {
            gameDirectory = file("run/")
            server()
        }
    }

    mods {
        register(property("mod.id") as String) {
            sourceSet(sourceSets["main"])
        }
    }
    sourceSets["main"].resources.srcDir("src/main/generated")
}


dependencies {
    modImplementation("dev.architectury:architectury-forge:${property("deps.architectury")}")
    modImplementation("me.shedaniel.cloth:cloth-config-forge:${property("deps.cloth")}")
    modImplementation("cc.tweaked:cc-tweaked-${property("deps.minecraft")}-forge:${property("deps.cct")}")
    modCompileOnly("dev.emi:emi-forge:${property("deps.emi")}+${property("deps.minecraft")}:api")
    modRuntimeOnly("dev.emi:emi-forge:${property("deps.emi")}+${property("deps.minecraft")}")


    // Create
    modCompileOnly("com.simibubi.create:create-${property("deps.minecraft")}:${property("deps.create")}:slim") { isTransitive = false }
    modImplementation("net.createmod.ponder:Ponder-Forge-${property("deps.minecraft")}:${property("deps.ponder")}")
    modCompileOnly("dev.engine-room.flywheel:flywheel-forge-api-${property("deps.minecraft")}:${property("deps.flywheel")}")
    modRuntimeOnly("dev.engine-room.flywheel:flywheel-forge-${property("deps.minecraft")}:${property("deps.flywheel")}")
    modImplementation("com.tterrag.registrate:Registrate:${property("deps.registrate")}")

    // https://maven.su5ed.dev/#/releases/dev/su5ed/sinytra/fabric-api
    // The Forge backport bundles relevant modules of FFAPI instead of requiring the entire mod.
    modImplementation("dev.su5ed.sinytra:fabric-loader:2.7.11+0.16.5+1.20.1")
    modImplementation("dev.su5ed.sinytra.fabric-api:fabric-api-base:0.4.32+ef105b4977")
    modImplementation("dev.su5ed.sinytra.fabric-api:fabric-networking-api-v1:1.3.14+fb7c822677")
    modImplementation("dev.su5ed.sinytra.fabric-api:fabric-resource-loader-v0:0.11.12+4b4151c077")
    jarJar("dev.su5ed.sinytra:fabric-loader:2.7.11+0.16.5+1.20.1")
    jarJar("dev.su5ed.sinytra.fabric-api:fabric-api-base:0.4.32+ef105b4977")
    jarJar("dev.su5ed.sinytra.fabric-api:fabric-networking-api-v1:1.3.14+fb7c822677")
    jarJar("dev.su5ed.sinytra.fabric-api:fabric-resource-loader-v0:0.11.12+4b4151c077")


    annotationProcessor("org.spongepowered:mixin:0.8.5:processor")
    annotationProcessor("io.github.llamalad7:mixinextras-common:0.5.0")
    compileOnly("io.github.llamalad7:mixinextras-common:0.5.0")
    implementation("io.github.llamalad7:mixinextras-forge:0.5.0")
    jarJar("io.github.llamalad7:mixinextras-forge:0.5.0")
    implementation("org.jspecify:jspecify:1.0.0")
}


stonecutter {
    replacements.string {
        direction = eval(current.version, ">1.21.10")
        replace("ResourceLocation", "Identifier")
        replace("net.minecraft.advancements.critereon", "net.minecraft.advancements.criterion")
        replace("net.minecraft.Util", "net.minecraft.util.Util")
        replace("net.minecraft.BlockUtil", "net.minecraft.util.BlockUtil")
        replace("net.minecraft.world.entity.monster.Skeleton", "net.minecraft.world.entity.monster.skeleton.Skeleton")
        replace("net.minecraft.world.entity.monster.AbstractSkeleton", "net.minecraft.world.entity.monster.skeleton.AbstractSkeleton")
        replace("net.minecraft.world.entity.animal.AbstractFish", "net.minecraft.world.entity.animal.fish.AbstractFish")
        replace("net.minecraft.world.entity.animal.AbstractSchoolingFish", "net.minecraft.world.entity.animal.fish.AbstractSchoolingFish")
        replace("net.minecraft.world.entity.animal.TropicalFish", "net.minecraft.world.entity.animal.fish.TropicalFish")
        replace("net.minecraft.world.entity.animal.WaterAnimal", "net.minecraft.world.entity.animal.fish.WaterAnimal")
        replace("net.minecraft.client.model.SlimeModel", "net.minecraft.client.model.monster.slime.SlimeModel")
        replace("net.minecraft.client.model.SkeletonModel", "net.minecraft.client.model.monster.skeleton.SkeletonModel")
        replace("net.minecraft.world.entity.vehicle.AbstractMinecart", "net.minecraft.world.entity.vehicle.minecart.AbstractMinecart")
        replace("net.minecraft.world.entity.vehicle.Boat", "net.minecraft.world.entity.vehicle.boat.Boat")
        replace("net.minecraft.world.level.BlockAndTintGetter", "net.minecraft.client.renderer.block.BlockAndTintGetter")
        replace("net.minecraft.world.level.GameRules", "net.minecraft.world.level.gamerules.GameRules")
        replace("net.minecraft.world.entity.animal.Bee", "net.minecraft.world.entity.animal.bee.Bee")
        replace("net.minecraft.world.entity.projectile.AbstractArrow", "net.minecraft.world.entity.projectile.arrow.AbstractArrow")
        replace("RULE_DOMOBSPAWNING", "SPAWN_MOBS")
        replace("FabricDataOutput", "FabricPackOutput")
    }
    replacements.string {
        direction = eval(current.version, ">=26.1")
        replace("GuiGraphics ", "GuiGraphicsExtractor ")
        replace("GuiGraphics.", "GuiGraphicsExtractor.")
        replace("GuiGraphics;", "GuiGraphicsExtractor;")
    }
    replacements.string {
        direction = eval(current.version, "<26.1")
        replace(".fakeItem(", ".renderFakeItem(")
        replace(".itemDecorations(", ".renderItemDecorations(")
        replace(".horizontalLine(", ".hLine(")
        replace(".verticalLine(", ".vLine(")
        replace("guiGraphics.text(", "guiGraphics.drawString(")
        replace("guiGraphics().text(", "guiGraphics().drawString(")
        replace("guiGraphics.textWithWordWrap(", "guiGraphics.drawWordWrap(")
        replace("guiGraphics().textWithWordWrap(", "guiGraphics().drawWordWrap(")
        replace("net.minecraft.world.item.ItemStackTemplate", "cc.cassian.rrv.backport.ItemStackTemplate")
        replace("extractBackground", "renderBackground")
        replace("net.fabricmc.fabric.api.networking.v1.context.PacketContext", "xyz.nucleoid.packettweaker.PacketContext")
        replace("centeredText", "drawCenteredString")
    }
}


mixin {
    add(sourceSets["main"], "infinity.refmap.json")
    config("infinity-forge.mixins.json")
}

tasks.named<Jar>("jar") {
    manifest {
        attributes(
            "MixinConfigs" to "infinity-forge.mixins.json"
        )
    }
}

tasks.register<Sync>("syncDatagen") {
    from(project(":${minecraft}-fabric").tasks.named("runDatagen"))
    into(file("src/main/generated/"))
}

tasks {
    processResources {
        exclude("**/fabric.mod.json", "**/*.accesswidener", "**/neoforge.mods.toml")
        exclude("**/config/21")
        exclude("**/config/22")
    }

    named("createMinecraftArtifacts") {
        dependsOn("stonecutterGenerate")
    }

    register<Copy>("buildAndCollect") {
        group = "build"
        from(jar.map { it.archiveFile })
        into(rootProject.layout.buildDirectory.file("libs/${project.property("mod.version")}"))
        dependsOn("build")
    }
}

java {
    withSourcesJar()
    val javaCompat = if (stonecutter.eval(stonecutter.current.version, ">=1.20.5")) {
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
    file = (tasks.named<org.gradle.jvm.tasks.Jar>("reobfJar").map { it.archiveFile.get() })
    additionalFiles.from(tasks.named<org.gradle.jvm.tasks.Jar>("sourcesJar").map { it.archiveFile.get() })

    type = STABLE
    displayName = "${property("mod.name")} ${property("mod.version")} for ${stonecutter.current.version} Forge"
    version = "${property("mod.version")}+${property("deps.minecraft")}-forge"
    changelog = provider { rootProject.file("CHANGELOG-LATEST.md").readText() }
    modLoaders.add("forge")

    modrinth {
        projectId = property("publish.modrinth") as String
        accessToken = env.MODRINTH_API_KEY.orNull()
        minecraftVersions.add(stonecutter.current.version)
        minecraftVersions.addAll(additionalVersions)
        requires("architectury-api")
        requires("cloth-config")
        if (hasProperty("deps.emi")) {
            optional("emi")
        }
        if (hasProperty("deps.rrv")) {
            optional("rrv")
        }

    }

    curseforge {
        projectId = property("publish.curseforge") as String
        accessToken = env.CURSEFORGE_API_KEY.orNull()
        minecraftVersions.add(stonecutter.current.version)
        minecraftVersions.addAll(additionalVersions)
        server = true
        client = true
        requires("architectury-api")
        requires("cloth-config")
        if (hasProperty("deps.emi")) {
            optional("emi")
        }
        if (hasProperty("deps.rrv")) {
            optional("rrv")
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "net.codexarchonic"
            artifactId = "infinity"
            version = "${property("mod.version")}+${property("deps.minecraft")}"

            from(components["java"])
        }
    }
}