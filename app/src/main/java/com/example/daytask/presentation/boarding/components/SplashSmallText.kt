package com.example.daytask.presentation.boarding.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import com.example.daytask.presentation.ui.theme.splashFontFamily
import com.example.daytask.presentation.ui.theme.yellowColor

@Composable
fun SplashSmallText(
    modifier: Modifier = Modifier,
    textSize: Int = 16
) {

    Text(
        modifier = modifier,
        text = buildAnnotatedString {
            withStyle(style = SpanStyle(fontSize = textSize.sp, fontFamily = splashFontFamily, color = Color.White)) {
                append("Day")
            }
            withStyle(style = SpanStyle(fontSize = textSize.sp, fontFamily = splashFontFamily, color = yellowColor)) {
                append("Task")
            }
        }
    )
}