import actual.gradle.commonMainDependencies

plugins {
  alias(libs.plugins.module.navigation)
}

commonMainDependencies {
  api(projects.login.model)
}
