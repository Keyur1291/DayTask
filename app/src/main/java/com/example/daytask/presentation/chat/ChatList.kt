package com.example.daytask.presentation.chat

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.daytask.R
import com.example.daytask.presentation.common.components.ReusableLargeButton
import com.example.daytask.presentation.ui.theme.bottomNavBarContainerColor
import com.example.daytask.presentation.ui.theme.splashBgColor
import com.example.daytask.presentation.ui.theme.yellowColor
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalSharedTransitionApi::class)
@Composable
fun ChatList(
    modifier: Modifier = Modifier,
    paddings: PaddingValues,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    navigateToPersonalChat: () -> Unit,
    navigateUp: () -> Unit
) {

    val scope = rememberCoroutineScope()
    val tabItems = listOf("Chat", "Groups")
    val pagerState = rememberPagerState { tabItems.size }
    val selectedTabIndex by remember { derivedStateOf { pagerState.currentPage } }

    with(sharedTransitionScope) {
        with(animatedVisibilityScope) {
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier
                    .fillMaxSize()
                    .padding(bottom = 12.dp),
                contentPadding = paddings
            ) {

                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .animateEnterExit(
                                enter = slideInVertically(initialOffsetY = { -(it * 2) }),
                                exit = slideOutVertically(targetOffsetY = { -(it * 2) })
                            )
                            .renderInSharedTransitionScopeOverlay(zIndexInOverlay = 1f)
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                    ) {
                        IconButton(
                            onClick = navigateUp
                        ) {
                            Icon(
                                tint = Color.White,
                                contentDescription = null,
                                painter = painterResource(R.drawable.arrowleft)
                            )
                        }
                        Spacer(Modifier.weight(1f))
                        Text(
                            color = Color.White,
                            style = MaterialTheme.typography.titleLarge,
                            text = "Messages"
                        )
                        Spacer(Modifier.weight(1f))
                        IconButton(
                            onClick = {}
                        ) {
                            Icon(
                                tint = Color.White,
                                contentDescription = null,
                                painter = painterResource(R.drawable.edit)
                            )
                        }
                    }
                }

                stickyHeader {

                    Row(
                        modifier = Modifier
                            .animateEnterExit(
                                enter = slideInVertically(initialOffsetY = { -(it * 2) }),
                                exit = slideOutVertically(targetOffsetY = { -(it * 2) })
                            )
                            .renderInSharedTransitionScopeOverlay(zIndexInOverlay = 1f)
                            .background(splashBgColor)
                            .displayCutoutPadding()
                            .padding(start = 20.dp, end = 20.dp, bottom = 20.dp),
                    ) {
                        ReusableLargeButton(
                            onClick = { scope.launch { pagerState.animateScrollToPage(0) } },
                            containerColor = if (selectedTabIndex == 0) yellowColor else bottomNavBarContainerColor,
                            contentColor = if (selectedTabIndex == 0) Color.Black else Color.White,
                            modifier = Modifier
                                .weight(1f),
                            buttonText = "Chat"
                        )
                        Spacer(Modifier.width(16.dp))
                        ReusableLargeButton(
                            onClick = { scope.launch { pagerState.animateScrollToPage(1) } },
                            containerColor = if (selectedTabIndex == 1) yellowColor else bottomNavBarContainerColor,
                            contentColor = if (selectedTabIndex == 1) Color.Black else Color.White,
                            modifier = Modifier
                                .weight(1f),
                            buttonText = "Groups"
                        )
                    }
                }

                item {
                    HorizontalPager(
                        modifier = Modifier
                            .renderInSharedTransitionScopeOverlay(zIndexInOverlay = 1f),
                        state = pagerState
                    ) { index ->
                        when (index) {
                            0 -> {
                                Column {
                                    repeat(10) { index ->
                                        Card(
                                            modifier = Modifier
                                                .animateEnterExit(
                                                    enter = slideInHorizontally { -(index+1) * it },
                                                    exit = slideOutHorizontally { -(index+1) * it }
                                                )
                                                .clickable { navigateToPersonalChat() },
                                            colors = CardDefaults.cardColors(
                                                containerColor = splashBgColor
                                            )
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                Image(
                                                    contentDescription = null,
                                                    painter = painterResource(R.drawable.profile_image),
                                                    modifier = Modifier
                                                        .padding(20.dp)
                                                        .clip(CircleShape)
                                                        .size(47.dp)
                                                )
                                                Column(
                                                    modifier = Modifier.weight(1f),
                                                    verticalArrangement = Arrangement.Center
                                                ) {
                                                    Text(
                                                        fontSize = 14.sp,
                                                        fontWeight = FontWeight.SemiBold,
                                                        color = Color.White,
                                                        text = "Kobux"
                                                    )
                                                    Text(
                                                        overflow = TextOverflow.Ellipsis,
                                                        maxLines = 1,
                                                        fontSize = 14.sp,
                                                        color = Color.White.copy(0.5f),
                                                        text = "Hey, check out the last task you got assigned to"
                                                    )
                                                }
                                                Text(
                                                    modifier = Modifier.padding(20.dp),
                                                    fontSize = 8.sp,
                                                    color = Color.White.copy(0.5f),
                                                    text = "2 Apr"
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            1 -> {
                                Column {
                                    repeat(10) {
                                        Card(
                                            modifier = Modifier,
                                            colors = CardDefaults.cardColors(
                                                containerColor = splashBgColor
                                            )
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                Image(
                                                    contentDescription = null,
                                                    painter = painterResource(R.drawable.rectangle_6),
                                                    modifier = Modifier
                                                        .padding(20.dp)
                                                        .clip(CircleShape)
                                                        .size(47.dp)
                                                )
                                                Column(
                                                    modifier = Modifier.weight(1f),
                                                    verticalArrangement = Arrangement.Center
                                                ) {
                                                    Text(
                                                        fontSize = 14.sp,
                                                        fontWeight = FontWeight.SemiBold,
                                                        color = Color.White,
                                                        text = "Mike"
                                                    )
                                                    Text(
                                                        overflow = TextOverflow.Ellipsis,
                                                        maxLines = 1,
                                                        fontSize = 14.sp,
                                                        color = Color.White.copy(0.5f),
                                                        text = "Hey, how are we feeling today about the match"
                                                    )
                                                }
                                                Text(
                                                    modifier = Modifier.padding(20.dp),
                                                    fontSize = 8.sp,
                                                    color = Color.White.copy(0.5f),
                                                    text = "2 Apr"
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}