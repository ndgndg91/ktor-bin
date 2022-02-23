val ktorVersion: String by project
val kotlinVersion: String by project
val logbackVersion: String by project
val exposedVersion: String by project
val pgjdbcNgVersion: String by project
val hikariCPVersion: String by project
val kodeinVersion: String by project

plugins {
    application
    kotlin("jvm") version "1.6.10"
}

group = "com.juju"
version = "0.0.1"
application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-auth:$ktorVersion")
    implementation("io.ktor:ktor-auth-jwt:$ktorVersion")
    implementation("io.ktor:ktor-server-host-common:$ktorVersion")
    implementation("io.ktor:ktor-jackson:$ktorVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("com.impossibl.pgjdbc-ng:pgjdbc-ng:$pgjdbcNgVersion")
    implementation("com.zaxxer:HikariCP:$hikariCPVersion")
    implementation("org.kodein.di:kodein-di-generic-jvm:$kodeinVersion")
    testImplementation("io.ktor:ktor-server-tests:$ktorVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")
}