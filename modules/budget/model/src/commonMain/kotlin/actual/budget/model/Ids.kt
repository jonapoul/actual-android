package actual.budget.model

@JvmInline
value class AccountId(private val value: String) : Comparable<AccountId> {
  override fun toString(): String = value
  override fun compareTo(other: AccountId) = value.compareTo(other.value)
}

@JvmInline
value class BankId(private val value: String) : Comparable<BankId> {
  override fun toString(): String = value
  override fun compareTo(other: BankId) = value.compareTo(other.value)
}

@JvmInline
value class BudgetId(private val value: String) : Comparable<BudgetId> {
  override fun toString(): String = value
  override fun compareTo(other: BudgetId) = value.compareTo(other.value)
}

@JvmInline
value class CategoryId(private val value: String) : Comparable<CategoryId> {
  override fun toString(): String = value
  override fun compareTo(other: CategoryId) = value.compareTo(other.value)
}

@JvmInline
value class CategoryGroupId(private val value: String) : Comparable<CategoryGroupId> {
  override fun toString(): String = value
  override fun compareTo(other: CategoryGroupId) = value.compareTo(other.value)
}

@JvmInline
value class PayeeId(private val value: String) : Comparable<PayeeId> {
  override fun toString(): String = value
  override fun compareTo(other: PayeeId) = value.compareTo(other.value)
}

@JvmInline
value class RuleId(private val value: String) : Comparable<RuleId> {
  override fun toString(): String = value
  override fun compareTo(other: RuleId) = value.compareTo(other.value)
}

@JvmInline
value class ScheduleId(private val value: String) : Comparable<ScheduleId> {
  override fun toString(): String = value
  override fun compareTo(other: ScheduleId) = value.compareTo(other.value)
}

@JvmInline
value class ScheduleNextDateId(private val value: String) : Comparable<ScheduleNextDateId> {
  override fun toString(): String = value
  override fun compareTo(other: ScheduleNextDateId) = value.compareTo(other.value)
}

@JvmInline
value class TransactionId(private val value: String) : Comparable<TransactionId> {
  override fun toString(): String = value
  override fun compareTo(other: TransactionId) = value.compareTo(other.value)
}

@JvmInline
value class TransactionFilterId(private val value: String) : Comparable<TransactionFilterId> {
  override fun toString(): String = value
  override fun compareTo(other: TransactionFilterId) = value.compareTo(other.value)
}

@JvmInline
value class ZeroBudgetMonthId(private val value: String) : Comparable<ZeroBudgetMonthId> {
  override fun toString(): String = value
  override fun compareTo(other: ZeroBudgetMonthId) = value.compareTo(other.value)
}
