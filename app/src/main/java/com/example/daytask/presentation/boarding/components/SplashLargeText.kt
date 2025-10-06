package com.example.daytask.presentation.boarding.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.example.daytask.presentation.ui.theme.yellowColor
import com.example.daytask.presentation.ui.theme.splashFontFamily

@Composable
fun SplashLargeText(
    modifier: Modifier = Modifier,
) {

    Text(
        fontWeight = FontWeight.SemiBold,
        modifier = modifier,
        style = MaterialTheme.typography.displayMedium,
        fontFamily = splashFontFamily,
        text = buildAnnotatedString {
            withStyle(style = SpanStyle(fontFamily = splashFontFamily, color = Color.White)) {
                append("Manage\nYour\nTask with\n")
            }
            withStyle(style = SpanStyle(color = yellowColor)) {
                append("DayTask")
            }
        }
    )
}