plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose") // Für Jetpack Compose unter Kotlin 2.x
    id("org.jetbrains.kotlin.kapt")            // Für Room Annotation Processing
}

android {
    namespace = "com.maral.happyplacesapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.maral.happyplacesapp"
        minSdk = 24
        targetSdk = 34
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

    buildFeatures {
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    // 🔷 Compose BOM zur Versionsverwaltung
    implementation(platform("androidx.compose:compose-bom:2024.02.00"))

    // 🔷 Jetpack Compose UI-Komponenten
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.ui:ui-text") // ✅ Für KeyboardOptions etc.
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.material3:material3")

    // 🔷 Navigation in Jetpack Compose
    implementation("androidx.navigation:navigation-compose:2.7.7")

    // 🔷 Lifecycle und Compose-Aktivität
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.2")

    // 🔷 Room Datenbank mit KAPT
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")

    // 🔷 Coil – für Bilder in Compose
    implementation("io.coil-kt:coil-compose:2.4.0")

    // 🔷 OSMDroid – OpenStreetMap in Android
    implementation("org.osmdroid:osmdroid-android:6.1.14")
    implementation("androidx.compose.material:material-icons-extended")

    // 🔷 Debugging und Tools
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // 🔷 Unit-Testing
    testImplementation("junit:junit:4.13.2")

    // 🔷 Instrumentierte UI-Tests
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.02.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
