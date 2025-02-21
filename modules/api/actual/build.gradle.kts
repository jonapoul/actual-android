import actual.gradle.commonMainDependencies
import actual.gradle.commonTestDependencies

plugins {
  alias(libs.plugins.module.multiplatform)
  alias(libs.plugins.kotlin.serialization)
  alias(libs.plugins.sekret)
}

commonMainDependencies {
  api(libs.alakazam.kotlin.core)
  api(libs.alakazam.kotlin.serialization)
  api(libs.javaxInject)
  api(libs.kotlinx.serialization.json)
  api(libs.retrofit.core)
  api(projects.account.model)
  api(projects.url.model)
  compileOnly(libs.sekret)
}

commonTestDependencies {
  implementation(projects.test.coroutines)
  implementation(projects.test.http)
  implementation(projects.test.resources)
}
