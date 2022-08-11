buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        with(Deps.Gradle) {
            classpath(pluginVersion)
            classpath(kotlin)
            classpath(kotlinSerialization)
            classpath(hilt)
        }
    }
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}