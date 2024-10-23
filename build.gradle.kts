plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	id("org.springframework.boot") version "3.3.4"
	id("io.spring.dependency-management") version "1.1.6"
}



allprojects {
	group = "com.leaderboard"
	version = "0.0.1-SNAPSHOT"
	repositories {
		mavenCentral()
	}

	java {
		toolchain {
			languageVersion = JavaLanguageVersion.of(17)
		}
	}

	kotlin {
		compilerOptions {
			freeCompilerArgs.addAll("-Xjsr305=strict")
		}
	}

	tasks.withType<Test> {
		useJUnitPlatform()
	}

	subprojects {
		apply(plugin = "org.springframework.boot")
		apply(plugin = "io.spring.dependency-management")
		apply(plugin = "org.jetbrains.kotlin.jvm")
		apply(plugin = "org.jetbrains.kotlin.plugin.spring")

		dependencies {
			implementation("org.springframework.boot:spring-boot-starter-web")
			implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
			implementation("org.jetbrains.kotlin:kotlin-reflect")
			testImplementation("org.springframework.boot:spring-boot-starter-test")
			testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
			testRuntimeOnly("org.junit.platform:junit-platform-launcher")
		}

	}

}
