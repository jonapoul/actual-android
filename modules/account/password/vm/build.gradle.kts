plugins {
  alias(libs.plugins.module.viewmodel)
}

dependencies {
  api(libs.javaxInject)
  api(projects.account.model)
  api(projects.account.password.domain)
  api(projects.core.colorscheme)
  implementation(libs.androidx.compose.runtime)
  implementation(libs.kotlinx.coroutines)
  implementation(projects.account.login.domain)
  implementation(projects.core.versions)
  implementation(projects.log)
  compileOnly(libs.alakazam.kotlin.compose.annotations)
  testImplementation(projects.core.connection)
  testImplementation(projects.test.buildconfig)
  testImplementation(projects.test.coroutines)
  testImplementation(projects.test.http)
  testImplementation(projects.test.prefs)
}
