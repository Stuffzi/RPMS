plugins {
    id("java")
}

group = "com.FMR"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.apptasticsoftware:rssreader:3.9.3") //https://github.com/w3stling/rssreader
    implementation("com.squareup.okhttp3:okhttp:5.1.0") //https://square.github.io/okhttp/#releases
    implementation("com.opencsv:opencsv:5.11.2") //https://opencsv.sourceforge.net/
}

tasks.test {
    useJUnitPlatform()
}