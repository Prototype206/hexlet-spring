
plugins {
  java
  id("org.springframework.boot") version "4.1.0-RC1"
  id("io.spring.dependency-management") version "1.1.7"
  jacoco
  id("org.sonarqube") version "7.3.0.8198"
}

group = "io.hexlet"
version = "0.0.1-SNAPSHOT"

repositories {
  mavenCentral()
}

dependencies {
  implementation("org.springframework.boot:spring-boot-starter")
  implementation("org.springframework.boot:spring-boot-starter-web")
  developmentOnly("org.springframework.boot:spring-boot-devtools")
  implementation("org.springframework.boot:spring-boot-starter-data-jpa")
  
  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation("net.javacrumbs.json-unit:json-unit-assertj:3.2.2")
  testImplementation(platform("org.junit:junit-bom:5.12.0"))
  testImplementation("org.junit.jupiter:junit-jupiter")
  testImplementation("org.springframework.security:spring-security-test")
  
  testImplementation("org.assertj:assertj-core:3.24.2")
  testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
  implementation("org.instancio:instancio-junit:3.3.0")
  implementation("org.springframework.boot:spring-boot-starter-validation")
  implementation("net.datafaker:datafaker:2.5.4")
  compileOnly("org.projectlombok:lombok:1.18.42")
  annotationProcessor("org.projectlombok:lombok:1.18.42")
  runtimeOnly("com.h2database:h2")
}

tasks.test {
    useJUnitPlatform()
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
}

tasks.check {
    dependsOn(tasks.jacocoTestReport)
}

sonarqube {
    properties {
        property("sonar.projectKey", "Prototype206_hexlet-spring")
        property("sonar.organization", "prototypes-organization") // для SonarCloud
        property("sonar.host.url", "https://sonarcloud.io") // или ваш SonarQube
        property("sonar.java.coveragePlugin", "jacoco")
        property("sonar.junit.reportPaths", "build/test-results/test")
        property("sonar.jacoco.reportPaths", "build/jacoco/test.exec")
        property("sonar.coverage.jacoco.xmlReportPaths", "build/reports/jacoco/test/jacocoTestReport.xml")
    }
}