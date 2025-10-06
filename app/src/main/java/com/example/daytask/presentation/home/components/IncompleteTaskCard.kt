package com.example.daytask.presentation.home.components

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Text
import androidx.compose.material3.WavyProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.daytask.data.local.model.ProjectWithTasks
import com.example.daytask.presentation.ui.theme.circularProgressStrokeColor
import com.example.daytask.presentation.ui.theme.contentDarkGrayBg
import com.example.daytask.presentation.ui.theme.splashFontFamily
import com.example.daytask.presentation.ui.theme.yellowColor

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun IncompleteTaskCard(
    modifier: Modifier = Modifier,
    projectWithTasks: ProjectWithTasks,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    with(sharedTransitionScope) {
        Card(
            modifier = modifier,
            colors = CardDefaults.cardColors(
                contentColor = Color.White,
                containerColor = contentDarkGrayBg
            ),
            shape = RectangleShape
        ) {
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                IncompleteTaskCardTitle(
                    modifier = Modifier
                        .sharedElement(
                            sharedContentState = rememberSharedContentState("Shared${projectWithTasks.project.title}"),
                            animatedVisibilityScope = animatedVisibilityScope
                        ),
                    text = projectWithTasks.project.title
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                    ) {
                        IncompleteTaskCardSmallText(
                            modifier = Modifier,
                            text = "Team members"
                        )
                        Spacer(Modifier.height(4.dp))
                        OverlappingImagesRow(
                            modifier = Modifier,
                            overlappingPercentage = 0.3f
                        ) {
                            repeat(3) {
                                CardImageRowItem()
                            }
                        }
                        Spacer(Modifier.height(4.dp))
                        IncompleteTaskCardSmallText(
                            modifier = Modifier
                                .sharedElement(
                                    sharedContentState = rememberSharedContentState("shared${projectWithTasks.project.title}DueDate"),
                                    animatedVisibilityScope = animatedVisibilityScope
                                ),
                            text = projectWithTasks.project.dueDate
                        )
                    }
                    IncompleteTaskCardProgressBox(
                        modifier = Modifier
                            .sharedElement(
                                sharedContentState = rememberSharedContentState("shared${projectWithTasks.project.title}Progress"),
                                animatedVisibilityScope = animatedVisibilityScope
                            )
                            .align(Alignment.Bottom),
                        progress = projectWithTasks.project.progress,
                        unit = "%"
                    )
                }
            }
        }
    }
}

@Composable
fun IncompleteTaskCardTitle(
    modifier: Modifier = Modifier,
    text: String
) {
    Text(
        color =Color.White,
        overflow = TextOverflow.Ellipsis,
        maxLines = 1,
        modifier = modifier,
        fontFamily = splashFontFamily,
        fontSize = 21.sp,
        fontWeight = FontWeight.Bold,
        text = text
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun IncompleteTaskCardProgressBox(
    modifier: Modifier = Modifier,
    progress: Float,
    unit: String
) {

    var animationPlayed by remember { mutableStateOf(false) }
    val currentPercentage by animateFloatAsState(
        targetValue = if (animationPlayed) progress else 0f,
        animationSpec = tween(
            durationMillis = 1500,
        )
    )
    LaunchedEffect(true) {
        animationPlayed = true
    }
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .rotate(160f)
                .size(65.dp),
            progress = { currentPercentage },
            color = yellowColor,
            trackColor = circularProgressStrokeColor,
            strokeCap = StrokeCap.Round,
        )
        Text(
            color = Color.White,
            fontSize = 12.sp,
            text = "${(currentPercentage*100f).toInt()}$unit"
        )
    }
}

@Composable
fun IncompleteTaskCardSmallText(
    modifier: Modifier = Modifier,
    text: String
) {
    Text(
        modifier = modifier,
        fontSize = 14.sp,
        text = text
    )
}