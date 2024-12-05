plugins {
    kotlin("jvm") version "2.0.20"
    `java-gradle-plugin`
}

allprojects {
    repositories {
        mavenCentral()
    }
    tasks.withType<AbstractArchiveTask>().configureEach {
        isPreserveFileTimestamps = false
        isReproducibleFileOrder = true
    }
}

dependencies {
    implementation("org.freemarker:freemarker:2.3.32")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.0")
}

gradlePlugin {
    plugins {
        create("t-generator") {
            id = "space.iseki.tgenerator"
            implementationClass = "a.G"
        }
    }
}
