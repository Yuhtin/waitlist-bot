plugins {
    id("java")
}

group = "com.yuhtin.quotes"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("net.dv8tion:JDA:VERSION") {
        exclude(module = "opus-java")
    }
}

tasks.test {
    useJUnitPlatform()
}