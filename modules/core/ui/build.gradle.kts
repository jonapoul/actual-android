import actual.gradle.koverExcludes

plugins {
  alias(libs.plugins.module.compose)
}

dependencies {
  api(libs.androidx.compose.runtime)
  api(libs.androidx.navigation.common)
  api(libs.androidx.navigation.runtime)
  api(libs.kotlinx.immutable)
  api(libs.lazycolumn.scrollbar)
  api(projects.core.colorscheme)
  api(projects.core.versions)
  api(projects.url.model)
  implementation(libs.androidx.compose.animation.core)
  implementation(libs.androidx.compose.foundation.core)
  implementation(libs.androidx.compose.foundation.layout)
  implementation(libs.androidx.compose.material.icons.core)
  implementation(libs.androidx.compose.material.icons.extended)
  implementation(libs.androidx.compose.material3)
  implementation(libs.androidx.compose.ui.core)
  implementation(libs.androidx.compose.ui.graphics)
  implementation(libs.androidx.compose.ui.text)
  implementation(libs.androidx.compose.ui.tooling)
  implementation(libs.androidx.compose.ui.toolingPreview)
  implementation(libs.androidx.compose.ui.unit)
  implementation(libs.androidx.compose.ui.util)
  implementation(libs.androidx.coreKtx)
  implementation(libs.kotlinx.coroutines)
  implementation(projects.core.res)
}

koverExcludes {
  packages(
    "actual.core.icons",
    "actual.core.ui",
  )
}
