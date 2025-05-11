plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.chatbotapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.chatbotapp"
        minSdk = 21
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.lottie)

    // OkHttp for network requests
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

// Gson for JSON parsing
    implementation("com.google.code.gson:gson:2.10.1")


}