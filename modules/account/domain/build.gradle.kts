plugins {
  alias(libs.plugins.module.multiplatform)
}

kotlin {
  commonMainDependencies {
    api(libs.javaxInject)
    api(project(":modules:account:model"))
    implementation(libs.alakazam.kotlin.core)
    implementation(libs.kotlinx.coroutines)
    implementation(libs.preferences.core)
    implementation(project(":modules:api:actual"))
    implementation(project(":modules:logging"))
    implementation(project(":modules:prefs"))
    compileOnly(libs.alakazam.kotlin.compose.annotations)
  }

  androidUnitTestDependencies {
    implementation(project(":modules:api:builder"))
    implementation(project(":modules:core:connection"))
  }
}
