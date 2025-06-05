package actual.budget.model

enum class UpcomingLengthOptions(val value: String) {
  OneDay("1"),
  OneWeek("7"),
  TwoWeeks("14"),
  OneMonth("oneMonth"),
  CurrentMonth("currentMonth"),
  Custom("custom"),
  ;

  companion object {
    fun from(value: String): UpcomingLengthOptions? = entries.firstOrNull { it.value == value }
  }
}
