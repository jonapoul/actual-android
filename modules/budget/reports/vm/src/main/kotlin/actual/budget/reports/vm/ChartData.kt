package actual.budget.reports.vm

import actual.budget.model.Amount
import actual.core.model.Percent
import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.datetime.LocalDate
import kotlinx.datetime.YearMonth

@Immutable
sealed interface ChartData

@Immutable
data class CashFlowData(
  val items: ImmutableMap<YearMonth, CashFlowDatum>,
) : ChartData

@Immutable
data class CashFlowDatum(
  val income: Amount,
  val expenses: Amount,
  val transfers: Amount,
  val balance: Amount,
  val change: Amount = income + expenses + transfers,
)

@Immutable
data class NetWorthData(
  val items: ImmutableMap<YearMonth, Amount>,
) : ChartData

@Immutable
sealed interface SummaryData : ChartData, DateRange {
  data class Sum(
    override val start: LocalDate,
    override val end: LocalDate,
    val value: Amount,
  ) : SummaryData

  data class AveragePerMonth(
    override val start: LocalDate,
    override val end: LocalDate,
    val numMonths: Float,
    val total: Amount,
    val average: Amount,
  ) : SummaryData

  data class AveragePerTransaction(
    override val start: LocalDate,
    override val end: LocalDate,
    val numTransactions: Int,
    val total: Amount,
    val average: Amount,
  ) : SummaryData

  data class Percentage(
    override val start: LocalDate,
    override val end: LocalDate,
    val numerator: Amount,
    val denominator: Amount,
    val percent: Percent,
    val divisor: PercentageDivisor,
  ) : SummaryData
}

@Immutable
sealed interface PercentageDivisor : DateRange {
  data class Specific(override val start: LocalDate, override val end: LocalDate) : PercentageDivisor

  data object AllTime : PercentageDivisor {
    override val start = null
    override val end = null
  }
}

@Immutable
interface DateRange {
  val start: LocalDate?
  val end: LocalDate?
}

@Immutable
enum class SummaryChartType {
  Sum,
  AveragePerMonth,
  AveragePerTransaction,
  Percentage,
}
