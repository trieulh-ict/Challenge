buildscript {
    repositories {
        google()
        mavenCentral()
        maven("https://plugins.gradle.org/m2/")
    }

    dependencies {
        with(Deps.Gradle) {
            classpath(pluginVersion)
            classpath(kotlin)
            classpath(kotlinSerialization)
            classpath(hilt)
            classpath(ktlint)
        }
    }
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}