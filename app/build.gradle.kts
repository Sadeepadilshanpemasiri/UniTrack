//import androidx.glance.appwidget.compose

//import androidx.glance.appwidget.compose

//import androidx.glance.appwidget.compose
//import androidx.navigation.compose.navigation

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.unitrack"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.unitrack"
        minSdk = 26
        targetSdk = 34
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
        // Updated to a more modern Java version
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17


    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // --- Standard AndroidX & Compose Dependencies ---
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom)) // Bill of Materials
    implementation(libs.androidx.ui.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.ui.tooling.preview)

    // --- Feature Dependencies (Corrected Aliases) ---
    // Navigation for switching between screens
    implementation(libs.androidx.navigation.compose)

    // ViewModel for handling UI logic
    implementation(libs.androidx.lifecycle.viewmodel.compose)


    // Extended Material Icons
    implementation(libs.androidx.material.icons.extended) // <-- To this

    implementation(libs.androidx.glance.appwidget)
    implementation(libs.androidx.glance.material3)
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.calendar.compose)
    implementation(libs.coil.compose)



    // ... (your test dependencies)

    // --- Test Dependencies ---
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    // The Compose BOM already covers this, so no need for a second platform() call
    androidTestImplementation(libs.androidx.ui.test.junit4)


    // --- Debug Dependencies ---
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
