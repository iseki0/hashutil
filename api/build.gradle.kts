import a.ftlGenerate
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    kotlin("jvm")
    id("space.iseki.tgenerator")
}


dependencies {
    compileOnly("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
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
