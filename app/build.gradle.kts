 plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    /* plugin ksp */
    alias(libs.plugins.google.devtools.ksp)

    /* plugin hilt */
    alias(libs.plugins.dagger.hilt.android)

    /* plugin serialization */
    alias(libs.plugins.jetbrains.serialization)

     /*Secrets Gradle Plugin */
     id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
    namespace = "com.example.hotelfinder"
    compileSdk = 35



    defaultConfig {
        applicationId = "com.example.hotelfinder"
        minSdk = 24
        //noinspection OldTargetApi
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField(
            "String",
            "PLACE_API_KEY",
            "\"${findProperty("PLACE_API_KEY") ?: ""}\""
        )

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

        buildConfig = true
    }
}




dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    /* dependecies */

    /* librerie retrofit */
    implementation(libs.retrofit)
    implementation(libs.converter.gson)


    /* dependency injection */
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

    /* room database*/
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    /* navigation di compose */
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.serialization.json)

    /* View model compose */
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    /* GoogleMaps */
    implementation(libs.maps.compose)

    implementation(libs.gson)

    /* Runtime Permission */
    implementation(libs.accompanist.permissions)
    implementation(libs.androidx.lifecycle.runtime.compose)

    implementation(libs.coil.compose)



}