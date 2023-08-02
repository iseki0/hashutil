import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.0" apply false
    kotlin("plugin.serialization") version "1.9.0" apply false
}

allprojects {
    group = "space.iseki.hashutil"
    version = "0.1.4-SNAPSHOT"

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
        kotlinOptions.freeCompilerArgs += "-Xassertions=jvm"
        kotlinOptions.freeCompilerArgs += "-Xlambdas=indy"
    }
}

