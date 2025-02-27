package actual.core.versions

data class ActualVersions(
  val app: String,
  val server: String?,
) {
  override fun toString() = "App: ${app.optionalPrefixed()} | Server: ${server.optionalPrefixed()}"

  private fun String?.optionalPrefixed(): String = when {
    this == null -> "Unknown"
    this.startsWith(prefix = "v") -> this
    else -> "v$this"
  }

  companion object {
    val Dummy = ActualVersions(app = "1.2.3", server = "24.3.0")
  }
}
