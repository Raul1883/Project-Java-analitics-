plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("com.opencsv:opencsv:3.7")

    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.2")
    implementation("com.j256.ormlite:ormlite-core:5.1")
    implementation("com.j256.ormlite:ormlite-jdbc:5.1")
    implementation("org.xerial:sqlite-jdbc:3.41.2.2")



}

tasks.test {
    useJUnitPlatform()
}