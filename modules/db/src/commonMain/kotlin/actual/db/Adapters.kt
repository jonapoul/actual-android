package actual.db

import actual.budget.model.AccountId
import actual.budget.model.AccountSyncSource
import actual.budget.model.Amount
import actual.budget.model.BalanceType
import actual.budget.model.BankId
import actual.budget.model.CategoryGroupId
import actual.budget.model.CategoryId
import actual.budget.model.ConditionOperator
import actual.budget.model.CustomReportId
import actual.budget.model.CustomReportMode
import actual.budget.model.DateRangeType
import actual.budget.model.GraphType
import actual.budget.model.GroupBy
import actual.budget.model.Interval
import actual.budget.model.PayeeId
import actual.budget.model.ReportDate
import actual.budget.model.RuleId
import actual.budget.model.ScheduleId
import actual.budget.model.ScheduleJsonPathIndex
import actual.budget.model.ScheduleNextDateId
import actual.budget.model.SortBy
import actual.budget.model.Timestamp
import actual.budget.model.TransactionFilterId
import actual.budget.model.TransactionId
import actual.budget.model.WidgetId
import actual.budget.model.WidgetType
import actual.budget.model.YearAndMonth
import actual.budget.model.ZeroBudgetMonthId
import alakazam.db.sqldelight.enumStringAdapter
import alakazam.db.sqldelight.longAdapter
import alakazam.db.sqldelight.stringAdapter
import app.cash.sqldelight.ColumnAdapter
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import kotlin.uuid.Uuid

private val jsonObject = object : ColumnAdapter<JsonObject, String> {
  override fun encode(value: JsonObject): String = Json.encodeToString(value)
  override fun decode(databaseValue: String): JsonObject = Json.parseToJsonElement(databaseValue).jsonObject
}

private val localDate = longAdapter(
  encode = { date: LocalDate -> with(date) { "%04d%02d%02d".format(year, month, dayOfMonth).toLong() } },
  decode = { value: Long ->
    val str = value.toString()
    val year = str.substring(startIndex = 0, endIndex = 3).toInt()
    val month = str.substring(startIndex = 4, endIndex = 5).toInt()
    val day = str.substring(startIndex = 6, endIndex = 7).toInt()
    LocalDate(year, month, day)
  },
)

private val instantMsFromString = stringAdapter(
  encode = { it.toEpochMilliseconds().toString() },
  decode = { Instant.fromEpochMilliseconds(it.toLong()) },
)
private val amount = longAdapter(::Amount, Amount::toLong)
private val instantFromLong = longAdapter(Instant::fromEpochMilliseconds, Instant::toEpochMilliseconds)
private val yearAndMonth = longAdapter(::YearAndMonth, YearAndMonth::toLong)

private val accountId = stringAdapter(::AccountId)
private val accountSyncSource = stringAdapter(AccountSyncSource::fromString)
private val bankId = stringAdapter(::BankId)
private val categoryGroupId = stringAdapter(::CategoryGroupId)
private val categoryId = stringAdapter(::CategoryId)
private val customReportsId = stringAdapter(::CustomReportId)
private val payeeId = stringAdapter(::PayeeId)
private val reportDate = stringAdapter(ReportDate::parse)
private val ruleId = stringAdapter(::RuleId)
private val scheduleId = stringAdapter(::ScheduleId)
private val scheduleJsonPathIndex = stringAdapter(::ScheduleJsonPathIndex)
private val scheduleNextDateId = stringAdapter(::ScheduleNextDateId)
private val timestamp = stringAdapter(Timestamp::parse)
private val transactionFilterId = stringAdapter(::TransactionFilterId)
private val transactionId = stringAdapter(::TransactionId)
private val widgetId = stringAdapter(::WidgetId)
private val uuid = stringAdapter(Uuid::parse)
private val zeroBudgetMonthId = stringAdapter(::ZeroBudgetMonthId)

private val balanceType = enumStringAdapter<BalanceType>()
private val conditionOperator = enumStringAdapter<ConditionOperator>()
private val customReportMode = enumStringAdapter<CustomReportMode>()
private val dateRangeType = enumStringAdapter<DateRangeType>()
private val graphType = enumStringAdapter<GraphType>()
private val groupBy = enumStringAdapter<GroupBy>()
private val interval = enumStringAdapter<Interval>()
private val sortBy = enumStringAdapter<SortBy>()
private val widgetType = enumStringAdapter<WidgetType>()

internal val AccountsAdapter = Accounts.Adapter(
  idAdapter = accountId,
  balance_currentAdapter = amount,
  balance_availableAdapter = amount,
  balance_limitAdapter = amount,
  bankAdapter = uuid,
  account_sync_sourceAdapter = accountSyncSource,
  last_syncAdapter = instantMsFromString,
)

internal val BanksAdapter = Banks.Adapter(
  idAdapter = uuid,
  bank_idAdapter = bankId,
)

internal val DashboardAdapter = Dashboard.Adapter(
  idAdapter = widgetId,
  typeAdapter = widgetType,
  metaAdapter = jsonObject,
)

internal val PayeesAdapter = Payees.Adapter(
  idAdapter = payeeId,
  transfer_acctAdapter = accountId,
)

internal val TransactionsAdapter = Transactions.Adapter(
  acctAdapter = accountId,
  amountAdapter = amount,
  idAdapter = transactionId,
  categoryAdapter = categoryId,
  dateAdapter = localDate,
  errorAdapter = jsonObject,
  imported_descriptionAdapter = payeeId,
  transferred_idAdapter = transactionId,
  parent_idAdapter = transactionId,
  scheduleAdapter = scheduleId,
)

internal val CategoriesAdapter = Categories.Adapter(
  idAdapter = categoryId,
  cat_groupAdapter = categoryGroupId,
  goal_defAdapter = jsonObject,
)

internal val CategoryGroupsAdapter = Category_groups.Adapter(
  idAdapter = categoryGroupId,
)

internal val CategoryMappingAdapter = Category_mapping.Adapter(
  idAdapter = categoryId,
  transferIdAdapter = categoryId,
)

internal val CustomReportsAdapter = Custom_reports.Adapter(
  idAdapter = customReportsId,
  start_dateAdapter = reportDate,
  end_dateAdapter = reportDate,
  date_rangeAdapter = dateRangeType,
  modeAdapter = customReportMode,
  group_byAdapter = groupBy,
  balance_typeAdapter = balanceType,
  graph_typeAdapter = graphType,
  conditionsAdapter = jsonObject,
  conditions_opAdapter = conditionOperator,
  metadataAdapter = jsonObject,
  sort_byAdapter = sortBy,
  intervalAdapter = interval,
)

internal val MessagesClockAdapter = Messages_clock.Adapter(
  clockAdapter = jsonObject,
)

internal val MessagesCrdtAdapter = Messages_crdt.Adapter(
  timestampAdapter = timestamp,
)

internal val PayeeMappingAdapter = Payee_mapping.Adapter(
  idAdapter = payeeId,
  targetIdAdapter = payeeId,
)

internal val RulesAdapter = Rules.Adapter(
  idAdapter = ruleId,
  conditionsAdapter = jsonObject,
  actionsAdapter = jsonObject,
  conditions_opAdapter = conditionOperator,
)

internal val SchedulesAdapter = Schedules.Adapter(
  idAdapter = scheduleId,
  ruleAdapter = ruleId,
)

internal val SchedulesJsonPathsAdapter = Schedules_json_paths.Adapter(
  schedule_idAdapter = scheduleId,
  payeeAdapter = scheduleJsonPathIndex,
  accountAdapter = scheduleJsonPathIndex,
  amountAdapter = scheduleJsonPathIndex,
  dateAdapter = scheduleJsonPathIndex,
)

internal val SchedulesNextDateAdapter = Schedules_next_date.Adapter(
  idAdapter = scheduleNextDateId,
  schedule_idAdapter = scheduleId,
  local_next_dateAdapter = localDate,
  local_next_date_tsAdapter = instantFromLong,
  base_next_dateAdapter = localDate,
  base_next_date_tsAdapter = instantFromLong,
)

internal val ReflectBudgetsAdapter = Reflect_budgets.Adapter(
  monthAdapter = yearAndMonth,
  categoryAdapter = categoryId,
  amountAdapter = amount,
)

internal val TransactionFiltersAdapter = Transaction_filters.Adapter(
  idAdapter = transactionFilterId,
  conditionsAdapter = jsonObject,
  conditions_opAdapter = conditionOperator,
)

internal val ZeroBudgetMonthsAdapter = Zero_budget_months.Adapter(
  idAdapter = zeroBudgetMonthId,
)

internal val ZeroBudgetsAdapter = Zero_budgets.Adapter(
  monthAdapter = yearAndMonth,
  categoryAdapter = categoryId,
  amountAdapter = amount,
)
