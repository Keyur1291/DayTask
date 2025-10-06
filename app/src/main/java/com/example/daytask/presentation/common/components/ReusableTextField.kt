package com.example.daytask.presentation.common.components

import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import com.example.daytask.presentation.ui.theme.bottomNavBarContainerColor
import com.example.daytask.presentation.ui.theme.contentDarkGrayBg
import com.example.daytask.presentation.ui.theme.yellowColor

@Composable
fun ReusableTextField(
    modifier: Modifier = Modifier,
    colors: TextFieldColors = textFieldColors(),
    enabled: Boolean = true,
    value: String = "",
    onValueChange: (String) -> Unit,
    placeHolder: String = "",
    minLines: Int = 1,
    singleLine: Boolean = true,
    leadingIcon: Int = 0,
    trailingIcon: Int = 0,
    trailingTint: Color = Color.White,
    onTrailingClick: () -> Unit = {},
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    supportingText: String? = null,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {

    TextField(
        enabled = enabled,
        singleLine = singleLine,
        minLines = minLines,
        modifier = modifier,
        colors = colors,
        shape = RectangleShape,
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(text = placeHolder) },
        leadingIcon = {
            if(leadingIcon > 0){
                Icon(
                    contentDescription = null,
                    painter = painterResource(leadingIcon)
                )
            }
        },
        trailingIcon = {
            if(trailingIcon > 0){
                IconButton(
                    onClick = onTrailingClick
                ){
                    Icon(
                        tint = trailingTint,
                        contentDescription = null,
                        painter = painterResource(trailingIcon)
                    )
                }
            }
        },
        isError = isError,
        supportingText = {
            if(supportingText != null) {
                Text(
                    text = supportingText
                )
            }
        },
        keyboardActions = keyboardActions,
        keyboardOptions = keyboardOptions,
        visualTransformation = visualTransformation
    )
}

@Composable
fun textFieldColors(): TextFieldColors {
    return TextFieldDefaults.colors(
        disabledIndicatorColor = Color.Transparent,
        disabledLeadingIconColor = Color.White,
        disabledTextColor = Color.White,
        disabledPlaceholderColor = Color.White.copy(0.5f),
        disabledContainerColor = contentDarkGrayBg,
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White,
        focusedLeadingIconColor = Color.White,
        unfocusedLeadingIconColor = Color.White,
        focusedTrailingIconColor = Color.White,
        unfocusedTrailingIconColor = Color.White,
        focusedContainerColor = contentDarkGrayBg,
        unfocusedContainerColor = contentDarkGrayBg,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        cursorColor = Color.White,
        focusedPlaceholderColor = Color.White.copy(0.5f),
        unfocusedPlaceholderColor = Color.White.copy(0.5f),
        errorContainerColor = Color.Red.copy(0.2f),
        errorTextColor = Color.White,
        errorCursorColor = Color.Red,
        errorLeadingIconColor = Color.White,
        errorIndicatorColor = Color.Transparent,
        errorPlaceholderColor = Color.White.copy(0.5f),
    )
}

@Composable
fun chatTextFieldColors(): TextFieldColors {
    return TextFieldDefaults.colors(
        disabledTrailingIconColor = yellowColor,
        disabledIndicatorColor = Color.Transparent,
        disabledLeadingIconColor = yellowColor,
        disabledTextColor = Color.White,
        disabledPlaceholderColor = Color.White.copy(0.5f),
        disabledContainerColor = Color.Gray,
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White,
        focusedLeadingIconColor = yellowColor,
        unfocusedLeadingIconColor = yellowColor,
        focusedTrailingIconColor = yellowColor,
        unfocusedTrailingIconColor = yellowColor,
        focusedContainerColor = bottomNavBarContainerColor,
        unfocusedContainerColor = bottomNavBarContainerColor,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        cursorColor = yellowColor,
        focusedPlaceholderColor = Color.White.copy(0.5f),
        unfocusedPlaceholderColor = Color.White.copy(0.5f),
        errorContainerColor = Color.Red.copy(0.2f),
        errorTextColor = Color.White,
        errorCursorColor = Color.Red,
        errorLeadingIconColor = Color.White,
        errorIndicatorColor = Color.Transparent,
        errorPlaceholderColor = Color.White.copy(0.5f),
    )
}