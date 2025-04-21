import blueprint.core.commonMainDependencies

plugins {
  alias(libs.plugins.module.multiplatform)
}

kotlin {
  commonMainDependencies {
    api(projects.budget.model)
    implementation(libs.metro.runtime)
  }
}
