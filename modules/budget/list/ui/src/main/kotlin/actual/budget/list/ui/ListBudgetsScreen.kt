package actual.budget.list.ui

import actual.budget.list.res.BudgetListStrings
import actual.budget.list.vm.Budget
import actual.budget.list.vm.ListBudgetsState
import actual.budget.list.vm.ListBudgetsViewModel
import actual.core.colorscheme.ColorSchemeType
import actual.core.ui.LocalTheme
import actual.core.ui.PreviewScreen
import actual.core.ui.ScreenPreview
import actual.core.ui.Theme
import actual.core.ui.VersionsText
import actual.core.ui.VerticalSpacer
import actual.core.ui.WavyBackground
import actual.core.ui.topAppBarColors
import actual.core.versions.ActualVersions
import alakazam.kotlin.core.exhaustive
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import kotlinx.collections.immutable.persistentListOf

// TODO: Remove suppression
@Suppress("UNUSED_PARAMETER")
@Composable
fun ListBudgetsScreen(
  navController: NavHostController,
  viewModel: ListBudgetsViewModel = hiltViewModel(),
) {
  val versions by viewModel.versions.collectAsStateWithLifecycle()
  val serverUrl by viewModel.serverUrl.collectAsStateWithLifecycle()
  val themeType by viewModel.themeType.collectAsStateWithLifecycle()
  val state by viewModel.state.collectAsStateWithLifecycle()

  DisposableEffect(Unit) {
    onDispose {
      viewModel.clearState()
    }
  }

  var launchBrowser by remember { mutableStateOf(false) }
  val context = LocalContext.current
  LaunchedEffect(launchBrowser) {
    if (launchBrowser) {
      val intent = Intent(Intent.ACTION_VIEW)
      intent.data = Uri.parse(serverUrl.toString())
      context.startActivity(intent, null)
      launchBrowser = false
    }
  }

  var budgetToDelete by remember { mutableStateOf<Budget?>(null) }
  budgetToDelete?.let { budget ->
    DeleteBudgetDialog(
      budget = budget,
      onAction = { action ->
        when (action) {
          DeleteDialogAction.DeleteLocal -> TODO()
          DeleteDialogAction.DeleteRemote -> TODO()
          DeleteDialogAction.Dismiss -> budgetToDelete = null
        }
      },
    )
  }

  ListBudgetsScaffold(
    versions = versions,
    state = state,
    themeType = themeType,
    onAction = { action ->
      when (action) {
        ListBudgetsAction.ChangeServer -> TODO()
        ListBudgetsAction.OpenInBrowser -> launchBrowser = true
        ListBudgetsAction.Reload -> TODO()
        is ListBudgetsAction.Delete -> budgetToDelete = action.budget
        is ListBudgetsAction.Open -> TODO()
      }
    },
  )
}

@Composable
private fun ListBudgetsScaffold(
  versions: ActualVersions,
  state: ListBudgetsState,
  themeType: ColorSchemeType,
  onAction: (ListBudgetsAction) -> Unit,
) {
  val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
  val theme = LocalTheme.current
  Scaffold(
    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    topBar = {
      TopAppBar(
        colors = theme.topAppBarColors(),
        title = { ScaffoldTitle() },
        scrollBehavior = scrollBehavior,
      )
    },
  ) { innerPadding ->
    Box {
      WavyBackground(themeType)

      ListBudgetsContent(
        modifier = Modifier.padding(innerPadding),
        versions = versions,
        state = state,
        onAction = onAction,
        theme = theme,
      )
    }
  }
}

@Stable
@Composable
private fun ScaffoldTitle() = Text(
  text = BudgetListStrings.listBudgetsToolbar,
  maxLines = 1,
  overflow = TextOverflow.Ellipsis,
)

@Stable
@Composable
private fun ListBudgetsContent(
  versions: ActualVersions,
  state: ListBudgetsState,
  onAction: (ListBudgetsAction) -> Unit,
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
) {
  Column(
    modifier = modifier
      .fillMaxSize()
      .padding(16.dp),
  ) {
    Box(
      modifier = Modifier.weight(1f),
      contentAlignment = Alignment.Center,
    ) {
      when (state) {
        is ListBudgetsState.Loading -> {
          ContentLoading(
            modifier = Modifier.fillMaxSize(),
            theme = theme,
          )
        }

        is ListBudgetsState.Failure -> {
          ContentFailure(
            modifier = Modifier.fillMaxSize(),
            reason = state.reason,
            onClickRetry = { onAction(ListBudgetsAction.Reload) },
            theme = theme,
          )
        }

        is ListBudgetsState.Success -> {
          if (state.budgets.isEmpty()) {
            ContentEmpty(
              modifier = Modifier.fillMaxSize(),
              theme = theme,
              onCreateBudgetInBrowser = { onAction(ListBudgetsAction.OpenInBrowser) },
            )
          } else {
            ContentSuccess(
              modifier = Modifier.fillMaxSize(),
              budgets = state.budgets,
              theme = theme,
              onClickOpen = { budget -> onAction(ListBudgetsAction.Open(budget)) },
              onClickDelete = { budget -> onAction(ListBudgetsAction.Delete(budget)) },
            )
          }
        }
      }.exhaustive
    }

    VerticalSpacer()

    VersionsText(
      modifier = Modifier.align(Alignment.End),
      versions = versions,
    )
  }
}

@ScreenPreview
@Composable
private fun Success() = PreviewScreen { type ->
  ListBudgetsScaffold(
    versions = PreviewVersions,
    themeType = type,
    state = ListBudgetsState.Success(
      budgets = persistentListOf(PreviewBudgetSynced, PreviewBudgetSyncing, PreviewBudgetBroken),
    ),
    onAction = {},
  )
}

@ScreenPreview
@Composable
private fun Loading() = PreviewScreen { type ->
  ListBudgetsScaffold(
    versions = PreviewVersions,
    themeType = type,
    state = ListBudgetsState.Loading,
    onAction = {},
  )
}

@ScreenPreview
@Composable
private fun Failure() = PreviewScreen { type ->
  ListBudgetsScaffold(
    versions = PreviewVersions,
    themeType = type,
    state = ListBudgetsState.Failure(reason = "Something broke lol"),
    onAction = {},
  )
}
