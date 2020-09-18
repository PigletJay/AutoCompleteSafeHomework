import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.10"
    kotlin("plugin.serialization") version "1.4.10"
}

repositories {
    jcenter()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.4.10")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.9")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.0.0-RC")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = listOf(
            "-Xuse-experimental=kotlin.ExperimentalStdlibApi",
            "-Xuse-experimental=kotlinx.serialization.InternalSerializationApi"
        )
    }
}