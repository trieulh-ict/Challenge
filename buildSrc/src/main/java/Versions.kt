object Versions {
    const val minSdk = 21
    const val compileSdk = 32
    const val targetSdk = compileSdk

    const val kotlin = "1.7.10"
    const val dependencyCheckPlugin = "0.42.0"
    const val gradlePluginVersion = "7.2.1"
    const val hiltPlugin = "2.43.2"
    const val googleServices = "4.3.13"

    const val composeViewModel = "2.5.1"
    const val activityCompose = "1.5.1"

    const val material = "1.6.1"
    const val lifecycleKtx = "2.6.0-alpha01"
    const val lifecycleRuntimeKtx = lifecycleKtx

    const val ksp = "1.7.10-1.0.6"
    const val composeDestinations = "1.7.15-beta"

    const val junit = "4.13.2"
    const val junitX = "1.1.3"

    const val compose = "1.3.0-alpha02"
    const val composeCompiler = "1.3.0-rc01"
    const val composeUIDebug = "1.3.0-alpha02"
    const val composeNavigation = "2.5.1"
    const val accompanist = "0.26.0-alpha"

    const val hiltCompose = "1.0.0"

    const val firebaseBom = "30.3.1"
    const val coroutinePlayServices = "1.6.4"

    const val ktxSerialization = "1.3.3"
    const val mockk = "1.12.5"
}

object Deps {

    object Gradle {
        const val googleServices = "com.google.gms:google-services:${Versions.googleServices}"
        const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
        const val kotlinSerialization =
            "org.jetbrains.kotlin:kotlin-serialization:${Versions.kotlin}"
        const val dependencyCheckPlugin = "com.github.ben-manes.versions"
        const val pluginVersion = "com.android.tools.build:gradle:${Versions.gradlePluginVersion}"
        const val hilt = "com.google.dagger:hilt-android-gradle-plugin:${Versions.hiltPlugin}"
    }

    object Android {
        const val material = "com.google.android.material:material:${Versions.material}"
    }

    object AndroidX {
        const val lifecycleRuntimeKtx =
            "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycleRuntimeKtx}"
        const val lifecycleRuntimeCompose =
            "androidx.lifecycle:lifecycle-runtime-compose:${Versions.lifecycleRuntimeKtx}"
        const val activityCompose = "androidx.activity:activity-compose:${Versions.activityCompose}"
    }

    object Compose {
        const val ui = "androidx.compose.ui:ui:${Versions.compose}"
        const val material = "androidx.compose.material:material:${Versions.compose}"
        const val iconExtend =
            "androidx.compose.material:material-icons-extended:${Versions.compose}"
        const val navigation =
            "androidx.navigation:navigation-compose:${Versions.composeNavigation}"
        const val viewModel =
            "androidx.lifecycle:lifecycle-viewmodel-compose:${Versions.composeViewModel}"
        const val uiPreview = "androidx.compose.ui:ui-tooling-preview:${Versions.composeUIDebug}"
        const val uiTestJunit4 = "androidx.compose.ui:ui-test-junit4:${Versions.composeUIDebug}"
        const val uiTooling = "androidx.compose.ui:ui-tooling:${Versions.composeUIDebug}"

        const val composeDestinations =
            "io.github.raamcosta.compose-destinations:core:${Versions.composeDestinations}"
        const val composeDestinationAnimation =
            "io.github.raamcosta.compose-destinations:animations-core:${Versions.composeDestinations}"
        const val ksp =
            "io.github.raamcosta.compose-destinations:ksp:${Versions.composeDestinations}"

        const val accompanistMaterial =
            "com.google.accompanist:accompanist-navigation-material:${Versions.accompanist}"
        const val accompanistAnimation =
            "com.google.accompanist:accompanist-navigation-animation:${Versions.accompanist}"
    }

    object DI {
        const val hiltAndroid = "com.google.dagger:hilt-android:${Versions.hiltPlugin}"
        const val hiltKapt = "com.google.dagger:hilt-android-compiler:${Versions.hiltPlugin}"
        const val hiltCompose = "androidx.hilt:hilt-navigation-compose:${Versions.hiltCompose}"
    }

    object Ksp {
        const val api = "com.google.devtools.ksp:symbol-processing-api:${Versions.ksp}"
    }

    object KtxSerialization {
        const val json =
            "org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.ktxSerialization}"
    }

    object Firebase {
        const val firebaseBom = "com.google.firebase:firebase-bom:${Versions.firebaseBom}"
        const val firebaseAuth = "com.google.firebase:firebase-auth-ktx"
        const val firebaseFirestore = "com.google.firebase:firebase-firestore-ktx"
        const val coroutinePlayServices = "org.jetbrains.kotlinx:kotlinx-coroutines-play-services:${Versions.coroutinePlayServices}"
    }

    object Test {
        const val junit = "junit:junit:${Versions.junit}"
        const val junitX = "androidx.test.ext:junit:${Versions.junitX}"
        const val mockk = "io.mockk:mockk:${Versions.mockk}"
    }
}
