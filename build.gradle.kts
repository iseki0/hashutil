import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.0" apply false
    kotlin("plugin.serialization") version "1.9.0" apply false
    id("org.jetbrains.dokka") version "1.9.0" apply false
}

allprojects {
    group = "space.iseki.hashutil"
    version = "0.3.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }

    tasks.withType<AbstractArchiveTask>().configureEach {
        isPreserveFileTimestamps = false
        isReproducibleFileOrder = true
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions.freeCompilerArgs += "-Xno-param-assertions"
        kotlinOptions.freeCompilerArgs += "-Xno-call-assertions"
        kotlinOptions.freeCompilerArgs += "-Xcontext-receivers"
        kotlinOptions.freeCompilerArgs += "-Xno-receiver-assertions"
        kotlinOptions.freeCompilerArgs += "-Xno-source-debug-extension"
        kotlinOptions.freeCompilerArgs += "-Xassertions=jvm"
        kotlinOptions.freeCompilerArgs += "-Xlambdas=indy"
    }
}

