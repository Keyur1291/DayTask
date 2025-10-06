package com.example.daytask.presentation.common.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp

@Composable
fun ReusableButtonText(
    modifier: Modifier = Modifier,
    text: String
) {
    Text(
        fontSize = 18.sp,
        modifier = modifier,
        text = text
    )
}