plugins {
  alias(libs.plugins.module.viewmodel)
}

dependencies {
  api(projects.account.login.domain)
  api(projects.account.model)
  api(projects.core.versions)
  api(projects.url.model)
  implementation(libs.alakazam.kotlin.core)
  implementation(libs.alakazam.kotlin.logging)
  implementation(libs.androidx.compose.runtime)
  implementation(libs.preferences.core)
  implementation(projects.account.login.domain)
  implementation(projects.url.prefs)
  compileOnly(libs.alakazam.kotlin.compose.annotations)
  testImplementation(projects.core.connection)
  testImplementation(projects.test.buildconfig)
  testImplementation(projects.test.coroutines)
  testImplementation(projects.test.http)
  testImplementation(projects.test.prefs)
}
