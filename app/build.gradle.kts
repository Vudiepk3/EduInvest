plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.google.firebase.crashlytics)
}

android {
    namespace = "com.example.eduinvest"
    compileSdk = 35

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
    buildFeatures {
        viewBinding = true
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

    // BOM để quản lý phiên bản Firebase
    implementation(platform(libs.firebase.bom.v3340))

    // Firebase Authentication
    implementation(libs.firebase.auth)

    // Google Sign-In
    implementation (libs.play.services.auth.v2040) // Google Identity Services

    implementation ("com.google.android.material:material:1.11.0")
    implementation("com.squareup.okhttp3:okhttp:4.10.0")
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.inappmessaging.display)
    implementation(libs.firebase.crashlytics)


    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation ("me.relex:circleindicator:2.1.6")
    implementation ("com.tbuonomo:dotsindicator:4.3")

    //Rate
    implementation ("com.github.DinoLibrary:Rate:1.0")
}

