package com.example.daytask.presentation.common.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.daytask.R
import com.example.daytask.presentation.ui.theme.yellowColor

@Composable
fun SquareCheckBox(
    enabled: Boolean,
    value: Boolean,
    modifier: Modifier = Modifier,
    onTickMarkChange: () -> Unit
) {


    IconButton(
        enabled = enabled,
        modifier = modifier,
        onClick = onTickMarkChange
    ) {
        Icon(
            modifier = Modifier.size(28.dp),
            tint = if (value) yellowColor else Color.White,
            contentDescription = null,
            painter = painterResource(R.drawable.ticksquare)
        )
    }
}