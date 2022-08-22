plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp") version Versions.ksp
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    id("org.jlleitschuh.gradle.ktlint")
}

android {
    compileSdk = Versions.compileSdk

    defaultConfig {
        applicationId = "io.trieulh.challenge"
        minSdk = Versions.minSdk
        targetSdk = Versions.targetSdk
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Versions.composeCompiler
    }
    packagingOptions {
        resources.excludes.add("/META-INF/{AL2.0,LGPL2.1}")
    }
    applicationVariants.all {
        kotlin.sourceSets {
            getByName(name) {
                kotlin.srcDir("build/generated/ksp/$name/kotlin")
            }
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.8.0")
    with(Deps.Compose) {
        implementation(ui)
        implementation(material)
        implementation(iconExtend)
        implementation(uiPreview)
        implementation(viewModel)
        implementation(navigation)
        implementation(composeDestinations)
        implementation(composeDestinationAnimation)

        androidTestImplementation(uiTestJunit4)
        debugImplementation(uiTooling)
    }
    ksp(Deps.Compose.ksp)

    with(Deps.DI) {
        implementation(hiltAndroid)
        implementation(hiltCompose)
        kapt(hiltKapt)
    }

    with(Deps.AndroidX) {
        implementation(lifecycleRuntimeKtx)
        implementation(lifecycleRuntimeCompose)
        implementation(activityCompose)
    }

    with(Deps.Test) {
        testImplementation(junit)
        testImplementation(mockk)
        testImplementation(coroutineTest)
        testImplementation(coreTesting)
        testImplementation(turbine)
        androidTestImplementation(junitX)
    }
}

ktlint {
    debug.set(true)
    disabledRules.set(setOf("no-wildcard-imports"))
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}
