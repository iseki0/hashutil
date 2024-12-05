import a.ftlGenerate

plugins {
    kotlin("jvm")
    id("space.iseki.tgenerator")
    signing
    `maven-publish`
    id("org.jetbrains.kotlinx.binary-compatibility-validator")
}

dependencies {
    compileOnly("org.jetbrains:annotations:24.0.1")
    compileOnly("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
    testImplementation(kotlin("test"))
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    jvmToolchain(17)
}

tasks.named("compileJava", JavaCompile::class.java) {
    options.compilerArgumentProviders.add(CommandLineArgumentProvider {
        // Provide compiled Kotlin classes to javac – needed for Java/Kotlin mixed sources to work
        listOf("--patch-module", "space.iseki.hashutil=${sourceSets["main"].output.asPath}")
    })
}

ftlGenerate {
    fun hash(name: String, bits: Int) {
        val size = bits / 8 / 4
        val data = mapOf("typename" to name, "size" to size)
        render("hash.ftl", "space/iseki/hashutil/$name.java", data)
        render("serializer.ftl", "space/iseki/hashutil/${name}Serializer.kt", data)
        render("helper.ftl", "space/iseki/hashutil/${name}.kt", data)
    }
    hash("MD2", 128)
    hash("MD5", 128)
    hash("SHA1", 160)
    hash("SHA128", 128)
    hash("SHA224", 224)
    hash("SHA256", 256)
    hash("SHA512", 512)
    hash("SM3", 256)
}

java {
    withSourcesJar()
}

val emptyJavadoc = tasks.create("emptyJavadoc", Jar::class) {
    archiveClassifier.set("javadoc")
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
            artifact(emptyJavadoc)
            pom {
                val projectUrl = "https://github.com/iseki0/hashutil"
                name = "hashutil"
                description = "Utils hash"
                url = projectUrl
                licenses {
                    license {
                        name = "Apache-2.0"
                        url = "https://www.apache.org/licenses/LICENSE-2.0"
                    }
                }
                developers {
                    developer {
                        id = "iseki0"
                        name = "iseki zero"
                        email = "iseki@iseki.space"
                    }
                }
                inceptionYear = "2024"
                scm {
                    connection = "scm:git:$projectUrl.git"
                    developerConnection = "scm:git:$projectUrl.git"
                    url = projectUrl
                }
                issueManagement {
                    system = "GitHub"
                    url = "$projectUrl/issues"
                }
                ciManagement {
                    system = "GitHub"
                    url = "$projectUrl/actions"
                }
            }
        }
    }
}

signing {
    // To use local gpg command, configure gpg options in ~/.gradle/gradle.properties
    // reference: https://docs.gradle.org/current/userguide/signing_plugin.html#example_configure_the_gnupgsignatory
    useGpgCmd()
    sign(publishing.publications)
}
