plugins {
  alias(libs.plugins.module.viewmodel)
}

dependencies {
  api(libs.alakazam.android.core)
  api(libs.javaxInject)
  api(libs.kotlinx.coroutines)
  api(projects.about.info.data)
  api(projects.core.buildconfig)
  implementation(libs.androidx.compose.runtime)
  implementation(libs.kotlinx.datetime)
  implementation(libs.molecule)
  implementation(projects.log)
  compileOnly(libs.alakazam.kotlin.compose.annotations)
  compileOnly(libs.hilt.core)
  testImplementation(projects.test.buildconfig)
  testImplementation(projects.test.coroutines)
}
