plugins {
    java
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

group = "evgeniy"
version = "3.0"

repositories {
    mavenCentral()
    maven {
        // Paper repo
        url = uri("https://papermc.io/repo/repository/maven-public/")
    }
    maven {
        url = uri("https://repo.dmulloy2.net/repository/public/")
    }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.18.2-R0.1-SNAPSHOT")  // no nms
//    compileOnly(files("./run/versions/1.18.2/paper-1.18.2.jar"))  // nms obfuscated
//    paperweightDevelopmentBundle("io.papermc.paper:dev-bundle:1.18.2-R0.1-SNAPSHOT")  // nms deobfuscated

    // https://mvnrepository.com/artifact/org.jetbrains/annotations
    compileOnly("org.jetbrains:annotations:23.0.0")

    // https://mvnrepository.com/artifact/org.projectlombok/lombok
    compileOnly("org.projectlombok:lombok:1.18.24")
    annotationProcessor("org.projectlombok:lombok:1.18.24")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

tasks.processResources {
    filesMatching(listOf("plugin.yml")) {
        expand(project.properties)
    }
}

val jar by tasks.getting(Jar::class)

val devBuild by tasks.creating(Copy::class) {
    group = "plugin"

    from(jar)
    into("$projectDir/run/plugins")
    this.rename {"${project.name}.jar" }
}
