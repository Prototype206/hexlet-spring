
plugins {
  java
  id("org.springframework.boot") version "4.1.0-RC1"
  id("io.spring.dependency-management") version "1.1.7"
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
  
  compileOnly("org.projectlombok:lombok:1.18.42")
  annotationProcessor("org.projectlombok:lombok:1.18.42")
  
  runtimeOnly("com.h2database:h2")
}

tasks.withType<Test> {
  useJUnitPlatform()
}