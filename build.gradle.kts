// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
}

//buildscript {
    //ext {
     //   compose_version = '1.3.0'
      //  retrofit_version = '2.9.0'
      //  jwt_version = '4.0.0'
   // }
//}