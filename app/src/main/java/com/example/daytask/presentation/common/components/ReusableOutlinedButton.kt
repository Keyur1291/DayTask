package com.example.daytask.presentation.common.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun ReusableOutlinedButton(
    modifier: Modifier = Modifier,
    textContentModifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    buttonText: String = "",
    buttonIcon: Int = 0,
    contentColor: Color = Color.White
) {

    OutlinedButton(
        border = BorderStroke(2.dp, Color.White),
        shape = RectangleShape,
        modifier = modifier,
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = contentColor
        ),
        onClick = onClick
    ) {
        if(buttonIcon > 0) {
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