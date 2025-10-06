package com.example.daytask.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.daytask.R
import com.example.daytask.presentation.common.components.ReusableTextField
import com.example.daytask.presentation.ui.theme.splashBgColor
import com.example.daytask.presentation.ui.theme.yellowColor

@Composable
fun TopBarSearchRow(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    onBackspaceClick: () -> Unit,
) {

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Top
    ) {
        ReusableTextField(
            modifier = Modifier.weight(1f),
            value = value,
            onValueChange = onValueChange,
            placeHolder = "Search tasks",
            leadingIcon = R.drawable.searchnormal1,
            trailingIcon = if(value != "") R.drawable.ic_backspace else 0,
            onTrailingClick = onBackspaceClick,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            )
        )
        Spacer(Modifier.width(16.dp))
        Icon(
            tint = splashBgColor,
            contentDescription = null,
            painter = painterResource(R.drawable.setting4),
            modifier = Modifier
                .background(yellowColor)
                .padding(16.dp)
        )
    }
}