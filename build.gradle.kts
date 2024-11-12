plugins {
    id("com.gradleup.shadow") version "8.3.5"
    id("java")
    kotlin("jvm")
}

group = "me.centauri07.villagecontrol"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven(url = uri("https://repo.papermc.io/repository/maven-public/"))
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")
    implementation(kotlin("stdlib-jdk8"))
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}