plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.notes"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.notes"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes += setOf(
                "META-INF/{" +
                        "LICENSE.md," +
                        "LICENSE.txt," +
                        "NOTICE.md," +
                        "NOTICE.txt," +
                        "LICENSE-notice.md" +
                        "}"
            )
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.material3)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.ui.text.google.fonts)
    implementation(libs.androidx.room.common)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.auth)
    implementation(platform(libs.firebase.bom))
    implementation(libs.googleid)
    implementation(libs.firebase.database)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.firestore)
    implementation(libs.core.ktx)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    // Declare koin-bom version
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.android)
    implementation(libs.koin.android.compat)
    implementation(libs.koin.compose)
    implementation(libs.koin.compose.viewmodel)
    implementation(libs.koin.compose.viewmodel.navigation)
    implementation(libs.koin.core.coroutines)
    implementation(libs.koin.androidx.workmanager)
    implementation(libs.koin.androidx.compose)
    implementation(libs.koin.androidx.compose.navigation)
    implementation(libs.koin.androidx.startup)
    //kotlinx serialization
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.androidx.datastore.preferences)

    implementation(libs.gson)

    //Coil
    implementation(libs.coil.compose)

    testImplementation(libs.mockk)
    androidTestImplementation(libs.mockk.android)

    // Works with test libraries too!
    testImplementation(libs.junit)
    testImplementation(libs.koin.test.junit4)
    testImplementation(libs.koin.android.test)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}