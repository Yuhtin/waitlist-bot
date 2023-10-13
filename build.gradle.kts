plugins {
    id("java")
}

group = "com.yuhtin.quotes"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("net.dv8tion:JDA:5.0.0-beta.15") {
        exclude(module = "opus-java")
        exclude(group = "org.apache.logging.log4j")
    }

    implementation("org.projectlombok:lombok:1.18.20")
    annotationProcessor("org.projectlombok:lombok:1.18.20")

    implementation("com.google.code.gson:gson:2.8.8")
}