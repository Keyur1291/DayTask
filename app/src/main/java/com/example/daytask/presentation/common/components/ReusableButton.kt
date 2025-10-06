package com.example.daytask.presentation.common.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.daytask.presentation.ui.theme.yellowColor

@Composable
fun ReusableLargeButton(
    modifier: Modifier = Modifier,
    textContentModifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit = {},
    buttonText: String = "",
    buttonIcon: Int = 0,
    containerColor: Color = yellowColor,
    contentColor: Color = Color.Black
) {

    Button(
        enabled = enabled,
        shape = RectangleShape,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        onClick = onClick
    ) {
        if (buttonIcon > 0) {
            Icon(
                contentDescription = null,
                painter = painterResource(buttonIcon)
            )
            Spacer(Modifier.width(8.dp))
        }
        ReusableButtonText(
            modifier = textContentModifier.padding(vertical = 10.dp),
            text = buttonText
        )
    }
}