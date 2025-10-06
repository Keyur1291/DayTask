package com.example.daytask.presentation.home.components

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.daytask.R
import com.example.daytask.data.local.model.ProjectWithTasks
import com.example.daytask.presentation.ui.theme.contentDarkGrayBg
import com.example.daytask.presentation.ui.theme.splashBgColor
import com.example.daytask.presentation.ui.theme.splashFontFamily
import com.example.daytask.presentation.ui.theme.yellowColor

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun CompletedTaskCard(
    modifier: Modifier = Modifier,
    teamModifier: Modifier = Modifier,
    progressModifier: Modifier = Modifier,
    projectWithTasks: ProjectWithTasks,
    index: Int,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {

    var animationPlayed by remember { mutableStateOf(false) }
    val currentPercentage by animateFloatAsState(
        targetValue = if (animationPlayed) projectWithTasks.project.progress else 0f,
        animationSpec = tween(
            durationMillis = 1500,
        )
    )

    LaunchedEffect(true) {
        animationPlayed = true
    }

    with(sharedTransitionScope) {
        Card(
            modifier = modifier,
            colors = CardDefaults.cardColors(
                contentColor = if (index == 0) splashBgColor else Color.White,
                containerColor = if (index == 0) yellowColor else contentDarkGrayBg
            ),
            shape = RectangleShape
        ) {
            Column(
                modifier = Modifier
                    .padding(8.dp)
            ) {
                CompletedTaskCardTitle(
                    index = index,
                    modifier = Modifier
                        .sharedElement(
                            sharedContentState = rememberSharedContentState("Shared${projectWithTasks.project.title}"),
                            animatedVisibilityScope = animatedVisibilityScope
                        ),
                    text = projectWithTasks.project.title
                )
                Spacer(Modifier.height(8.dp))
                Spacer(Modifier.weight(1f))
                Row(
                    modifier = teamModifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    CompletedTaskCardSmallText(
                        text = "Team members"
                    )
                    OverlappingImagesRow(
                        modifier = Modifier,
                        overlappingPercentage = 0.3f
                    ) {
                        repeat(4) {
                            CardImageRowItem(
                                modifier = Modifier
                            )
                        }
                    }
                }
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = progressModifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    CompletedTaskCardSmallText(
                        text = "Completed"
                    )
                    CompletedTaskCardSmallText(
                        text = "${(currentPercentage * 100).toInt()}%"
                    )
                }
                Spacer(Modifier.height(8.dp))
                CompletedTaskCardLinearProgress(
                    modifier = Modifier.fillMaxWidth(),
                    index = index,
                    progress = currentPercentage,
                )
                Spacer(Modifier.height(12.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun CompletedTaskCardLinearProgress(
    modifier: Modifier = Modifier,
    index: Int,
    progress: Float
) {
    LinearProgressIndicator(
        modifier = modifier
            .height(6.dp),
        progress = { progress },
        strokeCap = StrokeCap.Round,
        trackColor = Color.White.copy(0.5f),
        color = if (index == 0) splashBgColor else Color.White,
    )
}

@Composable
fun CardImageRowItem(
    modifier: Modifier = Modifier,
) {
    Image(
        modifier = modifier
            .border(2.dp, yellowColor, CircleShape)
            .size(20.dp),
        contentDescription = null,
        painter = painterResource(R.drawable.rectangle_6)
    )
}

@Composable
fun CompletedTaskCardSmallText(
    text: String
) {
    Text(
        fontSize = 12.sp,
        text = text
    )
}

@Composable
fun CompletedTaskCardTitle(
    modifier: Modifier = Modifier,
    text: String,
    index: Int
) {
    Text(
        modifier = modifier,
        color = if (index == 0) Color.Black else Color.White,
        fontWeight = FontWeight.Bold,
        fontSize = 21.sp,
        fontFamily = splashFontFamily,
        text = text
    )
}