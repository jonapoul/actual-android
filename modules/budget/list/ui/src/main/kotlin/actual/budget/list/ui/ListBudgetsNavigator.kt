package actual.budget.list.ui

import actual.account.model.LoginToken
import actual.budget.model.BudgetId
import androidx.compose.runtime.Immutable

@Immutable
interface ListBudgetsNavigator {
  fun toAbout()
  fun toChangePassword()
  fun toUrl()
  fun toSettings()
  fun toSyncBudget(token: LoginToken, id: BudgetId)
}
