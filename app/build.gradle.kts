plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.eduinvest"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.eduinvest"
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.database)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.storage)
    implementation(libs.cardview)
    implementation(libs.imagepicker)
    implementation(libs.imageslideshow)
    implementation(libs.glide)
    implementation(libs.fab)
    implementation(libs.dexter)

    // Firebase
    implementation (platform("com.google.firebase:firebase-bom:33.4.0") )// BOM để quản lý phiên bản
    implementation (libs.firebase.auth) // Firebase Authentication

    // Google Sign-In
    implementation ("com.google.android.gms:play-services-auth:20.5.0")

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}

