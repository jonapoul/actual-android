plugins {
  alias(libs.plugins.module.viewmodel)
}

dependencies {
  api(project(":modules:about:data"))
  api(libs.alakazam.android.core)
  api(libs.alakazam.kotlin.core)
  api(libs.androidx.lifecycle.viewmodel.core)
  api(libs.dagger.core)
  api(libs.javaxInject)
  api(libs.kotlinx.coroutines)
  implementation(libs.androidx.compose.runtime)
  implementation(libs.kotlinx.immutable)
  compileOnly(libs.alakazam.kotlin.compose.annotations)
  compileOnly(libs.hilt.core)
}
