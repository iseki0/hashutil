import a.ftlGenerate
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    kotlin("jvm")
    id("space.iseki.tgenerator")
    signing
    `maven-publish`
}

dependencies {
    compileOnly("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
    testImplementation(kotlin("test"))
}

tasks.withType<Jar> {
//    manifest.attributes("Multi-Release" to true)
//    into("META-INF/versions/9") {
//        from(sourceSets["java17"].output)
//    }
}

tasks.named<JavaCompile>("compileJava") {
    options.release.set(17)
}
//tasks.named<JavaCompile>("compileJava17Java"){
//    options.release.set(17)
//}

tasks.withType<KotlinCompile> {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
//        jvmTarget.set(JvmTarget.JVM_1_8)
    }
}

ftlGenerate {
    fun hash(name: String, bits: Int) {
        val size = bits / 8 / 4
        val data = mapOf("typename" to name, "size" to size)
        render("hash.ftl", "space/iseki/hashutil/$name.java", data)
        render("serializer.ftl", "space/iseki/hashutil/${name}Serializer.kt", data)
        render("helper.ftl", "space/iseki/hashutil/${name}.kt", data)
    }
    hash("SHA1", 160)
    hash("SHA128", 128)
    hash("SHA224", 224)
    hash("SHA256", 256)
    hash("SHA512", 512)
}

java {
    withSourcesJar()
    withJavadocJar()
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
            pom {
//                name.set("hashutil-api")
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
