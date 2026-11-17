plugins {
    id("dev.kikugie.stonecutter")
    id("co.uzzu.dotenv.gradle") version "4.0.0"
    id("net.neoforged.moddev") version "2.0.134" apply false
    id("dev.kikugie.postprocess.jsonlang") version "2.1-beta.4" apply false
    id("me.modmuss50.mod-publish-plugin") version "2.2.0" apply false
    id("org.moddedmc.wiki.toolkit") version "0.4.1"
}

stonecutter active "26.3-fabric"

stonecutter parameters {
    constants.match(node.metadata.project.substringAfterLast('-'), "fabric", "forge", "neoforge")
    filters.include("**/*.fsh", "**/*.vsh")
}


wiki {
    // The name of the object (examplemod) should match the registered wiki project ID (if it exists).
    docs.create("infinite-dimensions") {
        // The path to the folder containing the documentation metadata file (sinytra-wiki.json)
        root = file("docs")
    }
}