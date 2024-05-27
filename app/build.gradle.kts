plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.dagger.plugin)
}

android {
    namespace = "com.k10tetry.xchange"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.k10tetry.xchange"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true

            buildConfigField("String", "BASE_URL", "\"https://openexchangerates.org\"")
            buildConfigField("String", "API_KEY", "\"61ff4a69821b4f13b48122dce322da1c\"")
            buildConfigField("String", "DATASTORE_FILE", "\"xchange\"")

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        debug {
            applicationIdSuffix = ".debug"
            buildConfigField("String", "BASE_URL", "\"https://openexchangerates.org\"")
            buildConfigField("String", "API_KEY", "\"61ff4a69821b4f13b48122dce322da1c\"")
            buildConfigField("String", "DATASTORE_FILE", "\"xchange_debug\"")
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
        viewBinding = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.work.testing)

    // Memory Leak
    debugImplementation(libs.leakcanary.android)

    // Unit Test
    testImplementation(libs.android.test.core.ktx)
    testImplementation(libs.junit)
    testImplementation(libs.google.truth)
    testImplementation(libs.mockito)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.json)

    // Instrument Test
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.runner)

    // Network
    implementation(libs.square.retrofit)
    implementation(libs.square.retrofit.gson.converter)
    implementation(libs.logging.interceptor)

    // DI
    implementation(libs.google.hilt)
    kapt(libs.google.hilt.compiler)
    kapt(libs.hilt.compiler)

    // Worker
    implementation(libs.work.runtime.ktx)
    implementation(libs.hilt.work)

    // Local
    implementation(libs.datastore.preferences)
}