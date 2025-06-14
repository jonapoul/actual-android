package actual.about.data

sealed interface LicensesLoadState {
  data class Success(val libraries: List<ArtifactDetail>) : LicensesLoadState
  data class Failure(val cause: String) : LicensesLoadState
}
