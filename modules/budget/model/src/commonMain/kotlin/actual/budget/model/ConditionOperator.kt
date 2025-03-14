package actual.budget.model

enum class ConditionOperator(private val value: String) {
  And(value = "and"),
  Contains(value = "contains"),
  DoesNotContains(value = "doesNotContain"),
  Gt(value = "gt"),
  Gte(value = "gte"),
  HasTags(value = "hasTags"),
  Is(value = "is"),
  IsApprox(value = "isapprox"),
  IsBetween(value = "isbetween"),
  IsNot(value = "isNot"),
  Lt(value = "lt"),
  Lte(value = "lte"),
  Matches(value = "matches"),
  NotOneOf(value = "notOneOf"),
  OffBudget(value = "offBudget"),
  OnBudget(value = "onBudget"),
  OneOf(value = "oneOf"),
  ;

  override fun toString(): String = value
}
