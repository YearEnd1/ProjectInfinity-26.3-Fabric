plugins {
    id("net.neoforged.moddev")
    id ("dev.kikugie.postprocess.jsonlang")
    id("me.modmuss50.mod-publish-plugin")
}

val minecraft = stonecutter.current.version
val mcVersion = stonecutter.current.project.substringBeforeLast('-')
version = "${property("mod.version")}+${property("deps.minecraft")}"
if (property("mod.test_build") != "0") version = "$version-tb${property("mod.test_build")}"

tasks.named<ProcessResources>("processResources") {
    fun prop(name: String) = project.property(name) as String

    val props = HashMap<String, String>().apply {
        this["version"] = "$version"
        this["minecraft"] = prop("mod.mc_dep_forgelike")
    }

    filesMatching(listOf("fabric.mod.json", "META-INF/neoforge.mods.toml", "META-INF/mods.toml")) {
        expand(props)
    }
}

version = "$version-neoforge"
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
        name = "CC: Tweaked"
        url = uri("https://maven.squiddev.cc")
        content {
            includeGroupAndSubgroups("cc.tweaked")
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
        name = "Registrate"
        url = uri("https://maven.ithundxr.dev/snapshots")
        content {
            includeGroupAndSubgroups("com.tterrag")
        }
    }
    maven {
        name = "Sable, Aeronautics"
        url = uri("https://maven.ryanhcode.dev/releases")
        content {
            includeGroupAndSubgroups("dev.simulated_team")
            includeGroupAndSubgroups("dev.ryanhcode")
        }
    }
    maven {
        name = "Foundry Veil"
        url = uri("https://maven.blamejared.com")
        content {
            includeGroupAndSubgroups("foundry.veil")
        }
    }
    maven {
        name = "Curios"
        url = uri("https://artifactory.theillusivec4.top/artifactory/list/releases/")
        content {
            includeGroupAndSubgroups("top.theillusivec4.curios")
        }
    }
    exclusiveContent {
        forRepository {
            maven {
                name = "Cassian's Maven"
                url = uri("https://maven.cassian.cc")
            }
        }
        filter {
            includeGroupAndSubgroups("cc.cassian")
        }
    }

}

neoForge {
    version = property("deps.neoforge") as String
    validateAccessTransformers = true

    if (hasProperty("deps.parchment")) parchment {
        val (mc, ver) = (property("deps.parchment") as String).split(':')
        mappingsVersion = ver
        minecraftVersion = mc
    }

    runs {
        configureEach {
            systemProperty("neoforge.warnings.onlyin.hide", "true")
        }
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
    implementation("dev.architectury:architectury-neoforge:${property("deps.architectury")}")
    api("me.shedaniel.cloth:cloth-config-neoforge:${property("deps.cloth")}")

    if (hasProperty("deps.cct")) {
        implementation("cc.tweaked:cc-tweaked-${property("deps.minecraft")}-forge:${property("deps.cct")}")
    } else {
        compileOnly("cc.tweaked:cc-tweaked-1.21.1-forge:1.115.0")
    }
    if (hasProperty("deps.emi")) {
        compileOnly("dev.emi:emi-neoforge:${property("deps.emi")}+${property("deps.minecraft")}:api")
        implementation("dev.emi:emi-neoforge:${property("deps.emi")}+${property("deps.minecraft")}")
    }
    if (hasProperty("deps.rrv")) {
        implementation("cc.cassian.rrv:reliable-recipe-viewer-neoforge:${property("deps.rrv")}")
    }


    if (hasProperty("deps.create")) {
        // Create
        compileOnly("com.simibubi.create:create-${property("deps.minecraft")}:${property("deps.create")}:slim") { isTransitive = false }
        implementation("net.createmod.ponder:ponder-neoforge:${property("deps.ponder")}+mc${property("deps.minecraft")}")
        compileOnly("dev.engine-room.flywheel:flywheel-neoforge-api-${property("deps.minecraft")}:${property("deps.flywheel")}")
        runtimeOnly("dev.engine-room.flywheel:flywheel-neoforge-${property("deps.minecraft")}:${property("deps.flywheel")}")
        implementation("com.tterrag.registrate:Registrate:${property("deps.registrate")}")

    }
    if (hasProperty("deps.aero")) {
        compileOnly("dev.simulated_team.simulated:simulated-neoforge-${property("deps.minecraft")}:${property("deps.aero")}") { isTransitive = false }
    }
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

tasks.named("processResources") {
    dependsOn(":${stonecutter.current.project}:stonecutterGenerate")
}

tasks.register<Sync>("syncDatagen") {
    from(project(":${minecraft}-fabric").tasks.named("runDatagen"))
    exclude("**/.cache")
    into(file("src/main/generated/"))
}

tasks {
    processResources {
        exclude("**/fabric.mod.json", "infinity-forge.mixins.json", "**/*.accesswidener", "**/mods.toml")
        if (stonecutter.eval(stonecutter.current.version, ">1.21.5")) {
            exclude("**/resourcepacks/20")
            exclude("**/resourcepacks/21")
            exclude("**/config/20")
            exclude("**/config/21")
        }
        else if (stonecutter.eval(stonecutter.current.version, "=1.21.1")) {
            exclude("**/resourcepacks/20")
            exclude("**/resourcepacks/22")
            exclude("**/config/20")
            exclude("**/config/22")
        }
        else {
            exclude("**/resourcepacks/21")
            exclude("**/resourcepacks/22")
            exclude("**/config/21")
            exclude("**/config/22")
        }
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
    val javaCompat = if (stonecutter.eval(stonecutter.current.version, ">26")) {
        JavaVersion.VERSION_25
    } else {
        JavaVersion.VERSION_21
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
    file = tasks.jar.map { it.archiveFile.get() }
    additionalFiles.from(tasks.named<org.gradle.jvm.tasks.Jar>("sourcesJar").map { it.archiveFile.get() })

    type = if (stonecutter.eval(stonecutter.current.version, ">=1.21.2")) {
        ALPHA
    } else {
        STABLE
    }
    displayName = "${property("mod.name")} ${property("mod.version")} for ${stonecutter.current.version} NeoForge"
    version = "${property("mod.version")}+${property("deps.minecraft")}-neoforge"
    changelog = provider { rootProject.file("CHANGELOG-LATEST.md").readText() }
    modLoaders.add("neoforge")

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