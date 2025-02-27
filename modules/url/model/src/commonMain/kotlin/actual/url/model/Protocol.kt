package actual.url.model

enum class Protocol(private val value: String) {
  Http("http"),
  Https("https"),
  ;

  override fun toString(): String = value

  companion object {
    fun fromString(value: String): Protocol = entries
      .firstOrNull { it.value == value }
      ?: error("No protocol matching $value")
  }
}
