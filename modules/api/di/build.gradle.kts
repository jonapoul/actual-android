import blueprint.core.commonMainDependencies

plugins {
  alias(libs.plugins.module.metro)
}

kotlin {
  commonMainDependencies {
    api(libs.alakazam.kotlin.core)
    api(projects.api.github)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.okhttp.core)
    implementation(libs.retrofit.core)
    implementation(projects.api.builder)
    implementation(projects.url.model)
  }
}
