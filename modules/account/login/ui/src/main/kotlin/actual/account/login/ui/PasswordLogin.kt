package actual.account.login.ui

import actual.account.login.res.LoginStrings
import actual.account.model.Password
import actual.core.ui.PreviewColumn
import actual.core.ui.PrimaryTextButtonWithLoading
import actual.core.ui.TextField
import actual.core.ui.keyboardFocusRequester
import alakazam.android.ui.compose.VerticalSpacer
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
internal fun PasswordLogin(
  isLoading: Boolean,
  enteredPassword: Password,
  onAction: (LoginAction) -> Unit,
  modifier: Modifier = Modifier,
) {
  Column(
    modifier = modifier,
  ) {
    val keyboard = LocalSoftwareKeyboardController.current

    TextField(
      modifier = Modifier
        .fillMaxWidth(1f)
        .focusRequester(keyboardFocusRequester(keyboard)),
      value = enteredPassword.toString(),
      onValueChange = { password -> onAction(LoginAction.EnterPassword(password)) },
      placeholderText = LoginStrings.passwordHint,
      visualTransformation = PasswordVisualTransformation(),
      keyboardOptions = KeyboardOptions(
        autoCorrectEnabled = false,
        capitalization = KeyboardCapitalization.None,
        keyboardType = KeyboardType.Password,
        imeAction = ImeAction.Go,
      ),
      keyboardActions = KeyboardActions(
        onGo = {
          keyboard?.hide()
          onAction(LoginAction.SignIn)
        },
      ),
    )

    VerticalSpacer(20.dp)

    PrimaryTextButtonWithLoading(
      modifier = Modifier
        .padding(5.dp)
        .fillMaxWidth(),
      text = LoginStrings.signIn,
      isLoading = isLoading,
      onClick = { onAction(LoginAction.SignIn) },
    )
  }
}

@Preview
@Composable
private fun Loading() = PreviewColumn {
  PasswordLogin(
    isLoading = true,
    enteredPassword = Password.Dummy,
    onAction = {},
  )
}

@Preview
@Composable
private fun Filled() = PreviewColumn {
  PasswordLogin(
    isLoading = false,
    enteredPassword = Password.Dummy,
    onAction = {},
  )
}

@Preview
@Composable
private fun Empty() = PreviewColumn {
  PasswordLogin(
    isLoading = false,
    enteredPassword = Password.Empty,
    onAction = {},
  )
}
