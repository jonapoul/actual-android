package dev.jonpoulton.actual.serverurl.ui

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.jonpoulton.actual.core.model.ActualVersions
import dev.jonpoulton.actual.core.model.Protocol
import dev.jonpoulton.actual.core.ui.ActualExposedDropDownMenu
import dev.jonpoulton.actual.core.ui.ActualFontFamily
import dev.jonpoulton.actual.core.ui.ActualScreenPreview
import dev.jonpoulton.actual.core.ui.ActualTextField
import dev.jonpoulton.actual.core.ui.HorizontalSpacer
import dev.jonpoulton.actual.core.ui.LocalActualColorScheme
import dev.jonpoulton.actual.core.ui.OnDispose
import dev.jonpoulton.actual.core.ui.PreviewActualScreen
import dev.jonpoulton.actual.core.ui.PrimaryActualTextButtonWithLoading
import dev.jonpoulton.actual.core.ui.VersionsText
import dev.jonpoulton.actual.core.ui.VerticalSpacer
import dev.jonpoulton.actual.serverurl.vm.ServerUrlViewModel
import dev.jonpoulton.actual.serverurl.vm.ShouldNavigate
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import dev.jonpoulton.actual.core.res.R as ResR

@Composable
fun ServerUrlScreen(
  navigator: ServerUrlNavigator,
  viewModel: ServerUrlViewModel = hiltViewModel(),
) {
  val versions by viewModel.versions.collectAsStateWithLifecycle()
  val enteredUrl by viewModel.baseUrl.collectAsStateWithLifecycle()
  val protocol by viewModel.protocol.collectAsStateWithLifecycle()
  val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
  val shouldNavigate by viewModel.shouldNavigate.collectAsStateWithLifecycle(initialValue = false)
  val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle(initialValue = null)
  var clickedBack by remember { mutableStateOf(false) }

  OnDispose {
    viewModel.clearState()
    clickedBack = false
  }

  val context = LocalContext.current
  val activity = remember { context as? Activity ?: error("$context isn't an activity?") }

  when {
    clickedBack -> SideEffect { activity.finish() }
    shouldNavigate is ShouldNavigate.ToBootstrap -> SideEffect { navigator.navigateToBootstrap() }
    shouldNavigate is ShouldNavigate.ToLogin -> SideEffect { navigator.navigateToLogin() }
  }

  ServerUrlScreenImpl(
    url = enteredUrl,
    protocol = protocol,
    protocols = viewModel.protocols,
    versions = versions,
    isLoading = isLoading,
    errorMessage = errorMessage,
    onClickBack = { clickedBack = true },
    onClickConfirm = viewModel::onClickConfirm,
    onUrlEntered = viewModel::onUrlEntered,
    onProtocolSelected = viewModel::onProtocolSelected,
  )
}

@Composable
private fun ServerUrlScreenImpl(
  url: String,
  protocol: Protocol,
  protocols: ImmutableList<String>,
  versions: ActualVersions,
  isLoading: Boolean,
  errorMessage: String?,
  onClickBack: () -> Unit,
  onClickConfirm: () -> Unit,
  onUrlEntered: (String) -> Unit,
  onProtocolSelected: (Protocol) -> Unit,
) {
  val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
  val colorScheme = LocalActualColorScheme.current
  Scaffold(
    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    topBar = {
      TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = colorScheme.mobileHeaderBackground,
          titleContentColor = colorScheme.mobileHeaderText,
        ),
        navigationIcon = {
          IconButton(onClick = onClickBack) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Localized description",
            )
          }
        },
        title = {
          Text(
            text = stringResource(id = ResR.string.server_url_toolbar),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
          )
        },
        scrollBehavior = scrollBehavior,
      )
    },
  ) { innerPadding ->
    Content(
      modifier = Modifier.padding(innerPadding),
      url = url,
      protocol = protocol,
      protocols = protocols,
      versions = versions,
      isLoading = isLoading,
      errorMessage = errorMessage,
      onClickConfirm = onClickConfirm,
      onUrlEntered = onUrlEntered,
      onProtocolSelected = onProtocolSelected,
    )
  }
}

@Stable
@Composable
private fun Content(
  url: String,
  protocol: Protocol,
  protocols: ImmutableList<String>,
  versions: ActualVersions,
  isLoading: Boolean,
  errorMessage: String?,
  onClickConfirm: () -> Unit,
  onUrlEntered: (String) -> Unit,
  onProtocolSelected: (Protocol) -> Unit,
  modifier: Modifier = Modifier,
) {
  val colorScheme = LocalActualColorScheme.current
  Box(
    modifier = modifier
      .background(colorScheme.pageBackground)
      .fillMaxSize()
      .padding(16.dp),
    contentAlignment = Alignment.TopCenter,
  ) {
    Column(
      modifier = Modifier
        .wrapContentWidth()
        .wrapContentHeight(),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.Start,
    ) {
      Text(
        text = stringResource(id = ResR.string.server_url_title),
        style = MaterialTheme.typography.headlineLarge,
      )

      VerticalSpacer(height = 15.dp)

      Text(
        text = stringResource(id = ResR.string.server_url_message),
        color = colorScheme.tableRowHeaderText,
        style = MaterialTheme.typography.bodyLarge,
      )

      VerticalSpacer(height = 20.dp)

      Row(
        modifier = Modifier.fillMaxWidth(),
      ) {
        ActualExposedDropDownMenu(
          modifier = Modifier.width(110.dp),
          value = protocol.toString(),
          options = protocols,
          onValueChange = { onProtocolSelected(Protocol.fromString(it)) },
        )

        HorizontalSpacer(width = 5.dp)

        val focusManager = LocalFocusManager.current

        ActualTextField(
          modifier = Modifier.weight(1f),
          value = url,
          onValueChange = { onUrlEntered(it.lowercase()) },
          placeholderText = EXAMPLE_URL,
          keyboardOptions = KeyboardOptions(
            autoCorrect = false,
            capitalization = KeyboardCapitalization.None,
            keyboardType = KeyboardType.Uri,
            imeAction = ImeAction.Go,
          ),
          keyboardActions = KeyboardActions(
            onGo = {
              focusManager.clearFocus()
              onClickConfirm()
            },
          ),
        )
      }
      VerticalSpacer(height = 20.dp)

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        PrimaryActualTextButtonWithLoading(
          text = stringResource(id = ResR.string.server_url_confirm),
          isLoading = isLoading,
          onClick = onClickConfirm,
        )
      }

      if (errorMessage != null) {
        VerticalSpacer(20.dp)

        Text(
          modifier = Modifier.fillMaxWidth(),
          text = errorMessage,
          fontFamily = ActualFontFamily,
          color = colorScheme.errorText,
          textAlign = TextAlign.Center,
        )
      }
    }

    VersionsText(
      modifier = Modifier.align(Alignment.BottomEnd),
      versions = versions,
    )
  }
}

private const val EXAMPLE_URL = "example.com"

@ActualScreenPreview
@Composable
private fun Regular() = PreviewActualScreen {
  ServerUrlScreenImpl(
    url = "",
    protocol = Protocol.Https,
    protocols = persistentListOf("http", "https"),
    versions = ActualVersions(app = "1.2.3", server = "24.3.0"),
    isLoading = false,
    onClickBack = {},
    onClickConfirm = {},
    onUrlEntered = {},
    onProtocolSelected = {},
    errorMessage = null,
  )
}

@ActualScreenPreview
@Composable
private fun WithErrorMessage() = PreviewActualScreen {
  ServerUrlScreenImpl(
    url = "my.server.com:1234/path",
    protocol = Protocol.Http,
    protocols = persistentListOf("http", "https"),
    versions = ActualVersions(app = "1.2.3", server = "24.3.0"),
    isLoading = true,
    onClickBack = {},
    onClickConfirm = {},
    onUrlEntered = {},
    onProtocolSelected = {},
    errorMessage = "Hello this is an error message, split over multiple lines so you can see how it behaves",
  )
}
