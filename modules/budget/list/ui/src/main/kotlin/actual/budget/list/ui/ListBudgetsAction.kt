package actual.budget.list.ui

import actual.budget.model.Budget
import androidx.compose.runtime.Immutable

@Immutable
internal sealed interface ListBudgetsAction {
  data object Reload : ListBudgetsAction
  data object ChangePassword : ListBudgetsAction
  data object ChangeServer : ListBudgetsAction
  data object OpenAbout : ListBudgetsAction
  data object OpenInBrowser : ListBudgetsAction
  data object OpenSettings : ListBudgetsAction
  data class Open(val budget: Budget) : ListBudgetsAction
  data class Delete(val budget: Budget) : ListBudgetsAction
}
