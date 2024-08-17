plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.20-Beta1"
//id("org.jetbrains.kotlin.plugin.serialization") version "1.9.22"
    id("com.google.gms.google-services")
}

android {
    namespace = "com.grocery.flash"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.grocery.flash"
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
        kotlinCompilerExtensionVersion = "1.5.2" //1.4.3
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.2")
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3") //material ui

    implementation("androidx.lifecycle:lifecycle-viewmodel:2.8.2") // view model
    implementation ("androidx.lifecycle:lifecycle-runtime-compose:2.6.0")
    implementation("androidx.navigation:navigation-compose:2.7.7")//navigation component library
    implementation("com.squareup.retrofit2:retrofit:2.11.0") //retrofit
    implementation("com.squareup.retrofit2:converter-scalars:2.11.0")//retrofit
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0") // convert json to kotlin object
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0")// serializing
    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.14")
    implementation("io.coil-kt:coil-compose:2.6.0")//The library io.coil-kt:coil-compose:2.6.0 is a powerful image loading library for Jetpack Compose in Android development. It simplifies the process of fetching, caching, and displaying
    // images from various sources in your Compose UI. Here's why you might need this library:

    implementation("androidx.datastore:datastore-preferences:1.1.1")

    // Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:33.1.1"))
    // When using the BoM, don't specify versions in Firebase dependencies
    // Add the dependency for the Realtime Database library
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation("com.google.firebase:firebase-database")

    implementation("com.google.firebase:firebase-analytics")
    // Add the dependencies for any other desired Firebase products
    // https://firebase.google.com/docs/android/setup#available-libraries
    //Auth
    // Add the dependency for the Firebase Authentication library
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation("com.google.firebase:firebase-auth")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}