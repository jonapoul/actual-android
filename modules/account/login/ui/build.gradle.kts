plugins {
  alias(libs.plugins.module.compose)
}

dependencies {
  api(libs.androidx.compose.foundation.layout)
  api(libs.androidx.compose.runtime)
  api(libs.androidx.navigation.runtime)
  api(projects.account.login.vm)
  api(projects.core.ui)
  implementation(libs.androidx.compose.animation.core)
  implementation(libs.androidx.compose.foundation.core)
  implementation(libs.androidx.compose.hilt)
  implementation(libs.androidx.compose.material.icons.core)
  implementation(libs.androidx.compose.material3)
  implementation(libs.androidx.compose.ui.core)
  implementation(libs.androidx.compose.ui.graphics)
  implementation(libs.androidx.compose.ui.text)
  implementation(libs.androidx.compose.ui.tooling)
  implementation(libs.androidx.compose.ui.toolingPreview)
  implementation(libs.androidx.compose.ui.unit)
  implementation(libs.androidx.lifecycle.common)
  implementation(libs.androidx.lifecycle.runtime.compose)
  implementation(libs.androidx.lifecycle.viewmodel.compose)
  implementation(libs.androidx.lifecycle.viewmodel.core)
  implementation(libs.androidx.navigation.common)
  implementation(libs.kotlinx.coroutines)
  implementation(projects.account.login.domain)
  implementation(projects.account.login.nav)
  implementation(projects.account.login.res)
  implementation(projects.budget.list.nav)
  implementation(projects.core.res)
  implementation(projects.url.nav)
}
