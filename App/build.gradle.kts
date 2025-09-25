plugins {
    id("application")
    id("java")
}
repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.code.gson:gson:2.10.1")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}



application {
    mainClass.set("org.escaperoom.main")
}

tasks.test {
    useJUnitPlatform()
}



