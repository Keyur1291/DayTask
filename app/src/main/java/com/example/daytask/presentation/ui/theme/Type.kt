package com.example.daytask.presentation.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.example.daytask.R

val splashFontFamily = FontFamily(
    Font(R.font.pilat_extended_semi_bold, FontWeight.SemiBold),
    Font(R.font.pilat_extended_regular, FontWeight.Normal)
)

val interFontFamily = FontFamily(
    Font(R.font.inter_black, FontWeight.Black),
    Font(R.font.inter_bold, FontWeight.Bold),
    Font(R.font.inter_extra_bold, FontWeight.ExtraBold),
    Font(R.font.inter_extra_light, FontWeight.ExtraLight),
    Font(R.font.inter_light, FontWeight.Light),
    Font(R.font.inter_regular, FontWeight.Normal),
    Font(R.font.inter_italic, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.inter_medium, FontWeight.Medium),
    Font(R.font.inter_semi_bold, FontWeight.SemiBold),
    Font(R.font.inter_thin, FontWeight.Thin),
)

val Typography = Typography()

val AppTypography = Typography(
    displayLarge = Typography.displayLarge.copy(fontFamily = interFontFamily),
    displayMedium = Typography.displayMedium.copy(fontFamily = interFontFamily),
    displaySmall = Typography.displaySmall.copy(fontFamily = interFontFamily),
    headlineLarge = Typography.headlineLarge.copy(fontFamily = interFontFamily),
    headlineMedium = Typography.headlineMedium.copy(fontFamily = interFontFamily),
    headlineSmall = Typography.headlineSmall.copy(fontFamily = interFontFamily),
    titleLarge = Typography.titleLarge.copy(fontFamily = interFontFamily),
    titleMedium = Typography.titleMedium.copy(fontFamily = interFontFamily),
    titleSmall = Typography.titleSmall.copy(fontFamily = interFontFamily),
    bodyLarge = Typography.bodyLarge.copy(fontFamily = interFontFamily),
    bodyMedium = Typography.bodyMedium.copy(fontFamily = interFontFamily),
    bodySmall = Typography.bodySmall.copy(fontFamily = interFontFamily),
    labelLarge = Typography.labelLarge.copy(fontFamily = interFontFamily),
    labelMedium = Typography.labelMedium.copy(fontFamily = interFontFamily),
    labelSmall = Typography.labelSmall.copy(fontFamily = interFontFamily),
)