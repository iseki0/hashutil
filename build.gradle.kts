plugins {
    kotlin("jvm") version "1.8.21"
    kotlin("plugin.serialization") version "1.8.21"
    id("org.jetbrains.dokka") version "1.8.10"
    signing
    `maven-publish`
}

allprojects {
    group = "space.iseki.hashutil"
    version = "1.0.2-SNAPSHOT"

    repositories {
        mavenCentral()
    }

}

dependencies {
    testImplementation(kotlin("test"))
    compileOnly("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
    target.compilations.all {
        compilerOptions.configure {
            freeCompilerArgs.add("-Xno-call-assertions")
            freeCompilerArgs.add("-Xno-param-assertions")
        }
    }
}

tasks.withType<AbstractArchiveTask>().configureEach {
    isPreserveFileTimestamps = false
    isReproducibleFileOrder = true
}

java {
    withSourcesJar()
}

publishing {
    repositories {
        maven {
            name = "Central"
            url = if (version.toString().endsWith("SNAPSHOT")) {
                // uri("https://s01.oss.sonatype.org/content/repositories/snapshots")
                uri("https://oss.sonatype.org/content/repositories/snapshots")
            } else {
                // uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2")
                uri("https://oss.sonatype.org/service/local/staging/deploy/maven2")
            }
            credentials {
                username = properties["ossrhUsername"]?.toString() ?: System.getenv("OSSRH_USERNAME")
                password = properties["ossrhPassword"]?.toString() ?: System.getenv("OSSRH_PASSWORD")
            }
        }
    }
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            val publication = this
            val javadocJar = tasks.register("${publication.name}JavadocJar", Jar::class) {
                archiveClassifier.set("javadoc")
                from(tasks.dokkaJavadoc)
                archiveBaseName.set("${archiveBaseName.get()}-${publication.name}")
            }
            artifact(javadocJar)
            pom {
                name.set("hashutil")
                description.set("Utils hash")
                url.set("https://github.com/iseki0/hashutil")
                licenses {
                    license {
                        name.set("Apache-2.0")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0")
                    }
                }
                developers {
                    developer {
                        id.set("iseki0")
                        name.set("iseki zero")
                        email.set("iseki@iseki.space")
                    }
                }
                scm {
                    connection.set("scm:git:https://github.com/iseki0/hashutil.git")
                    developerConnection.set("scm:git:https://github.com/iseki0/hashutil.git")
                    url.set("https://github.com/iseki0/hashutil")
                }
            }
        }
    }
}

afterEvaluate {
    signing {
        // To use local gpg command, configure gpg options in ~/.gradle/gradle.properties
        // reference: https://docs.gradle.org/current/userguide/signing_plugin.html#example_configure_the_gnupgsignatory
        useGpgCmd()
        publishing.publications.forEach { sign(it) }
    }
}
