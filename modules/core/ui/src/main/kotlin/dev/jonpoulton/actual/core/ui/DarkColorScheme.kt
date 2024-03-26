@file:Suppress("ConvertObjectToDataObject")

package dev.jonpoulton.actual.core.ui

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

private val ButtonBareBackground = Color.Transparent
private val ButtonMenuSelectedBackground = ActualColors.orange200
private val ButtonMenuText = ActualColors.navy200
private val ButtonNormalDisabledText = ActualColors.navy500
private val ButtonNormalSelectedBackground = ActualColors.purple600
private val ButtonNormalText = ActualColors.navy150
private val ButtonPrimaryBackground = ActualColors.purple400
private val ButtonPrimaryDisabledBackground = ActualColors.navy400
private val ButtonPrimaryText = ActualColors.white
private val CardShadow = ActualColors.navy700
private val ErrorTextDark = ActualColors.red150
private val MobileHeaderBackground = ActualColors.purple800
private val PageText = ActualColors.navy150
private val PillBorder = ActualColors.navy700
private val TableBorder = ActualColors.navy600
private val TableText = ActualColors.navy150

@Immutable
internal class DarkColorScheme : ActualColorScheme {
  override val pageBackground = ActualColors.gray900
  override val pageBackgroundModalActive = ActualColors.gray800
  override val pageBackgroundTopLeft = ActualColors.navy800
  override val pageBackgroundBottomRight = ActualColors.gray700
  override val pageBackgroundLineTop = ActualColors.purple400
  override val pageBackgroundLineMid = ActualColors.navy900
  override val pageBackgroundLineBottom = ActualColors.navy150
  override val pageText = PageText
  override val pageTextLight = ActualColors.navy300
  override val pageTextSubdued = ActualColors.navy500
  override val pageTextDark = ActualColors.navy100
  override val pageTextPositive = ActualColors.purple200
  override val pageTextLink = ActualColors.purple400
  override val pageTextLinkLight = ActualColors.purple200

  override val cardBackground = ActualColors.gray800
  override val cardBorder = ActualColors.purple400
  override val cardShadow = CardShadow

  override val tableBackground = ActualColors.navy800
  override val tableRowBackgroundHover = ActualColors.navy700
  override val tableText = TableText
  override val tableTextLight = tableText
  override val tableTextSubdued = ActualColors.navy500
  override val tableTextSelected = ActualColors.navy150
  override val tableTextHover = ActualColors.navy400
  override val tableTextInactive = ActualColors.navy500
  override val tableHeaderText = ActualColors.navy300
  override val tableHeaderBackground = ActualColors.navy700
  override val tableBorder = TableBorder
  override val tableBorderSelected = ActualColors.purple400
  override val tableBorderHover = ActualColors.purple300
  override val tableBorderSeparator = ActualColors.navy400
  override val tableRowBackgroundHighlight = ActualColors.purple800
  override val tableRowBackgroundHighlightText = ActualColors.navy150
  override val tableRowHeaderBackground = ActualColors.navy700
  override val tableRowHeaderText = ActualColors.navy150

  override val sidebarBackground = ActualColors.navy900
  override val sidebarItemBackgroundPositive = ActualColors.green500
  override val sidebarItemBackgroundFailed = ActualColors.red300
  override val sidebarItemAccentSelected = ActualColors.purple200
  override val sidebarItemBackgroundHover = ActualColors.navy700
  override val sidebarItemText = ActualColors.navy150
  override val sidebarItemTextSelected = ActualColors.purple200

  override val menuBackground = ActualColors.navy600
  override val menuItemBackground = ActualColors.navy600
  override val menuItemBackgroundHover = ActualColors.navy500
  override val menuItemText = ActualColors.navy100
  override val menuItemTextHover = ActualColors.navy50
  override val menuItemTextSelected = ActualColors.purple400
  override val menuItemTextHeader = ActualColors.purple500
  override val menuBorder = ActualColors.navy800
  override val menuBorderHover = ActualColors.purple400
  override val menuKeybindingText = ActualColors.navy500
  override val menuAutoCompleteBackground = ActualColors.navy900
  override val menuAutoCompleteBackgroundHover = ActualColors.navy600
  override val menuAutoCompleteText = ActualColors.navy200
  override val menuAutoCompleteTextHeader = ActualColors.purple200

  override val modalBackground = ActualColors.gray800
  override val modalBorder = ActualColors.navy600
  override val mobileHeaderBackground = MobileHeaderBackground
  override val mobileHeaderText = ActualColors.navy150
  override val mobileHeaderTextSubdued = ActualColors.gray200
  override val mobileHeaderTextHover = Color(color = 0x26C8C8C8)
  override val mobilePageBackground = ActualColors.navy700
  override val mobileNavBackground = ActualColors.navy800
  override val mobileNavItem = ActualColors.navy150
  override val mobileNavItemSelected = ActualColors.purple400
  override val mobileAccountShadow = cardShadow
  override val mobileAccountText = ActualColors.blue800
  override val mobileModalBackground = ActualColors.navy900
  override val mobileModalText = ActualColors.white

  // Mobile view themes (for the top bar)
  override val mobileViewTheme = mobileHeaderBackground
  override val mobileConfigServerViewTheme = ActualColors.purple500

  override val markdownNormal = ActualColors.purple700
  override val markdownDark = ActualColors.purple500
  override val markdownLight = ActualColors.purple800

  // Button
  override val buttonMenuText = ButtonMenuText
  override val buttonMenuTextHover = buttonMenuText
  override val buttonMenuBackground = Color.Transparent
  override val buttonMenuBackgroundHover = Color(color = 0x40C8C8C8)
  override val buttonMenuBorder = ActualColors.navy500
  override val buttonMenuSelectedText = ActualColors.green800
  override val buttonMenuSelectedTextHover = ActualColors.orange800
  override val buttonMenuSelectedBackground = ButtonMenuSelectedBackground
  override val buttonMenuSelectedBackgroundHover = ActualColors.orange300
  override val buttonMenuSelectedBorder = buttonMenuSelectedBackground

  override val buttonPrimaryText = ButtonPrimaryText
  override val buttonPrimaryTextHover = buttonPrimaryText
  override val buttonPrimaryBackground = ButtonPrimaryBackground
  override val buttonPrimaryBackgroundHover = ActualColors.purple600
  override val buttonPrimaryBorder = buttonPrimaryBackground
  override val buttonPrimaryShadow = Color(color = 0x99000000)
  override val buttonPrimaryDisabledText = ActualColors.navy700
  override val buttonPrimaryDisabledBackground = ButtonPrimaryDisabledBackground
  override val buttonPrimaryDisabledBorder = buttonPrimaryDisabledBackground

  override val buttonNormalText = ButtonNormalText
  override val buttonNormalTextHover = ActualColors.navy150
  override val buttonNormalBackground = ActualColors.navy800
  override val buttonNormalBackgroundHover = ActualColors.navy600
  override val buttonNormalBorder = ActualColors.navy300
  override val buttonNormalShadow = Color(color = 0x66000000)
  override val buttonNormalSelectedText = ActualColors.white
  override val buttonNormalSelectedBackground = ButtonNormalSelectedBackground
  override val buttonNormalDisabledText = ButtonNormalDisabledText
  override val buttonNormalDisabledBackground = ActualColors.navy800
  override val buttonNormalDisabledBorder = ActualColors.navy500

  override val calendarText = ActualColors.navy50
  override val calendarBackground = ActualColors.navy900
  override val calendarItemText = ActualColors.navy150
  override val calendarItemBackground = ActualColors.navy800
  override val calendarSelectedBackground = buttonNormalSelectedBackground

  override val buttonBareText = buttonNormalText
  override val buttonBareTextHover = buttonNormalText
  override val buttonBareBackground = ButtonBareBackground
  override val buttonBareBackgroundHover = Color(color = 0x4DC8C8C8)
  override val buttonBareBackgroundActive = Color(color = 0x80C8C8C8)
  override val buttonBareDisabledText = buttonNormalDisabledText
  override val buttonBareDisabledBackground = buttonBareBackground

  override val noticeBackground = ActualColors.green800
  override val noticeBackgroundLight = ActualColors.green900
  override val noticeBackgroundDark = ActualColors.green500
  override val noticeText = ActualColors.green300
  override val noticeTextLight = ActualColors.green500
  override val noticeTextDark = ActualColors.green150
  override val noticeTextMenu = ActualColors.green500
  override val noticeBorder = ActualColors.green800
  override val warningBackground = ActualColors.orange800
  override val warningText = ActualColors.orange300
  override val warningTextLight = ActualColors.orange500
  override val warningTextDark = ActualColors.orange100
  override val warningBorder = ActualColors.orange500
  override val errorBackground = ActualColors.red800
  override val errorText = ActualColors.red200
  override val errorTextDark = ErrorTextDark
  override val errorTextDarker = errorTextDark
  override val errorTextMenu = ActualColors.red500
  override val errorBorder = ActualColors.red500
  override val upcomingBackground = ActualColors.purple700
  override val upcomingText = ActualColors.purple100
  override val upcomingBorder = tableBorder

  override val formLabelText = ActualColors.purple150
  override val formLabelBackground = ActualColors.blue900
  override val formInputBackground = ActualColors.navy800
  override val formInputBackgroundSelected = ActualColors.navy700
  override val formInputBackgroundSelection = ActualColors.purple400
  override val formInputBorder = ActualColors.navy500
  override val formInputTextReadOnlySelection = ActualColors.navy800
  override val formInputBorderSelected = ActualColors.purple400
  override val formInputText = ActualColors.navy150
  override val formInputTextSelected = ActualColors.black
  override val formInputTextPlaceholder = ActualColors.navy500
  override val formInputTextPlaceholderSelected = ActualColors.navy600
  override val formInputTextSelection = ActualColors.navy800
  override val formInputShadowSelected = ActualColors.purple200
  override val formInputTextHighlight = ActualColors.purple400
  override val checkboxText = tableText
  override val checkboxBackgroundSelected = ActualColors.purple300
  override val checkboxBorderSelected = ActualColors.purple300
  override val checkboxShadowSelected = ActualColors.purple500
  override val checkboxToggleBackground = ActualColors.gray700

  override val pillBackground = ActualColors.navy800
  override val pillBackgroundLight = ActualColors.navy900
  override val pillText = ActualColors.navy200
  override val pillTextHighlighted = ActualColors.purple200
  override val pillBorder = PillBorder
  override val pillBorderDark = pillBorder
  override val pillBackgroundSelected = ActualColors.purple600
  override val pillTextSelected = ActualColors.navy150
  override val pillBorderSelected = ActualColors.purple400

  override val reportsRed = ActualColors.red300
  override val reportsBlue = ActualColors.blue400
  override val reportsLabel = pageText
  override val reportsInnerLabel = ActualColors.navy800
}
