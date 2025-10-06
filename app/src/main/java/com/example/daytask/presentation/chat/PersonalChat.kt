package com.example.daytask.presentation.chat

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.daytask.R
import com.example.daytask.presentation.common.components.ReusableTextField
import com.example.daytask.presentation.common.components.chatTextFieldColors
import com.example.daytask.presentation.ui.theme.bottomNavBarContainerColor
import com.example.daytask.presentation.ui.theme.splashBgColor
import com.example.daytask.presentation.ui.theme.yellowColor

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun PersonalChat(
    modifier: Modifier = Modifier,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    navigateBack: () -> Unit
)
{

    with(sharedTransitionScope) {
        with(animatedVisibilityScope) {
            Scaffold(
                modifier = modifier,
                topBar = {
                    TopAppBar(
                        modifier = Modifier
                            .animateEnterExit(
                                enter = slideInVertically(initialOffsetY = { -(it * 2) }),
                                exit = slideOutVertically(targetOffsetY = { -(it * 2) })
                            ).renderInSharedTransitionScopeOverlay(zIndexInOverlay = 1f),
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = splashBgColor,
                            scrolledContainerColor = splashBgColor
                        ),
                        navigationIcon = {
                            IconButton(onClick = navigateBack) {
                                Icon(
                                    tint = Color.White,
                                    contentDescription = null,
                                    painter = painterResource(R.drawable.arrowleft)
                                )
                            }
                        },
                        title = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Image(
                                    modifier = Modifier.size(45.dp),
                                    contentDescription = null,
                                    painter = painterResource(R.drawable.rectangle_6)
                                )
                                Spacer(Modifier.width(8.dp))
                                Column(verticalArrangement = Arrangement.Top) {
                                    Text(fontSize = 22.sp, color = Color.White, text = "Kobux")
                                    Text(
                                        fontSize = 14.sp,
                                        color = Color.White.copy(0.5f),
                                        text = "Online"
                                    )
                                }
                            }
                        },
                        actions = {
                            IconButton(onClick = {}) {
                                Icon(
                                    tint = Color.White,
                                    contentDescription = null,
                                    painter = painterResource(R.drawable.video)
                                )
                            }
                            IconButton(onClick = {}) {
                                Icon(
                                    tint = Color.White,
                                    contentDescription = null,
                                    painter = painterResource(R.drawable.callcalling)
                                )
                            }
                        }
                    )
                },
                bottomBar = {
                    Row(
                        modifier = Modifier
                            .animateEnterExit(
                                enter = slideInVertically(initialOffsetY = { (it) }),
                                exit = slideOutVertically(targetOffsetY = { (it) })
                            ).renderInSharedTransitionScopeOverlay(zIndexInOverlay = 1f)
                            .fillMaxWidth()
                            //.background(splashBgColor)
                            .imePadding()
                            .padding(horizontal = 20.dp)
                    ) {
                        ReusableTextField(
                            modifier = Modifier.weight(1f),
                            colors = chatTextFieldColors(),
                            value = "",
                            onValueChange = {},
                            placeHolder = "Type a message",
                            leadingIcon = R.drawable.elementequal,
                            trailingIcon = R.drawable.send,
                            trailingTint = yellowColor
                        )
                        Spacer(Modifier.width(8.dp))
                        Icon(
                            tint = yellowColor,
                            modifier = Modifier
                                .background(bottomNavBarContainerColor)
                                .padding(16.dp),
                            contentDescription = null,
                            painter = painterResource(R.drawable.microphone2)
                        )
                    }
                }
            ) { paddings ->
                Surface(
                    color = splashBgColor
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 10.dp),
                        contentPadding = paddings,
                        reverseLayout = true
                    ) {

                        item {
                            Column(
                                modifier = Modifier
                                    .animateEnterExit(
                                        enter = slideInHorizontally(initialOffsetX = { (it) }),
                                        exit = slideOutHorizontally(targetOffsetX = { (it) })
                                    ).renderInSharedTransitionScopeOverlay(zIndexInOverlay = 1f)
                                    .fillMaxSize()
                            ) {
                                var seen by remember { mutableStateOf(true) }
                                Text(
                                    fontSize = 14.sp,
                                    color = Color.Black,
                                    modifier = Modifier
                                        .align(Alignment.End)
                                        .padding(horizontal = 20.dp, vertical = 5.dp)
                                        .background(yellowColor)
                                        .clickable { seen = !seen }
                                        .padding(12.dp),
                                    text = "Got it. Will check it soon."
                                )
                                AnimatedVisibility(
                                    enter = fadeIn() + expandVertically(animationSpec = tween(100)),
                                    exit = fadeOut() + shrinkVertically(animationSpec = tween(100)),
                                    modifier = Modifier.padding(horizontal = 20.dp)
                                        .align(Alignment.End),
                                    visible = seen
                                ) {
                                    Text(
                                        fontSize = 12.sp,
                                        color = yellowColor,
                                        text = "Seen"
                                    )
                                }
                            }
                        }

                        item {
                            Column(
                                modifier = Modifier
                                    .animateEnterExit(
                                        enter = slideInHorizontally(initialOffsetX = { -(it) }),
                                        exit = slideOutHorizontally(targetOffsetX = { -(it) })
                                    ).renderInSharedTransitionScopeOverlay(zIndexInOverlay = 1f)
                                    .fillMaxSize()
                            ) {

                                Text(
                                    color = Color.White,
                                    modifier = Modifier
                                        .align(Alignment.Start)
                                        .padding(horizontal = 20.dp, vertical = 5.dp)
                                        .background(bottomNavBarContainerColor)
                                        .padding(12.dp),
                                    text = "Hi, please check the new task."
                                )
                            }
                        }

                        item {
                            Column(
                                modifier = Modifier
                                    .animateEnterExit(
                                        enter = slideInHorizontally(initialOffsetX = { (it) }),
                                        exit = slideOutHorizontally(targetOffsetX = { (it) })
                                    ).renderInSharedTransitionScopeOverlay(zIndexInOverlay = 1f)
                                    .fillMaxSize()
                            ) {
                                var seen by remember { mutableStateOf(true) }
                                Text(
                                    fontSize = 14.sp,
                                    color = Color.Black,
                                    modifier = Modifier
                                        .align(Alignment.End)
                                        .padding(horizontal = 20.dp, vertical = 5.dp)
                                        .background(yellowColor)
                                        .clickable { seen = !seen }
                                        .padding(12.dp),
                                    text = "Got it. Will check it soon."
                                )
                                AnimatedVisibility(
                                    enter = fadeIn() + expandVertically(animationSpec = tween(100)),
                                    exit = fadeOut() + shrinkVertically(animationSpec = tween(100)),
                                    modifier = Modifier.padding(horizontal = 20.dp)
                                        .align(Alignment.End),
                                    visible = seen
                                ) {
                                    Text(
                                        fontSize = 12.sp,
                                        color = yellowColor,
                                        text = "Seen"
                                    )
                                }
                            }
                        }

                        item {
                            Column(
                                modifier = Modifier
                                    .animateEnterExit(
                                        enter = slideInHorizontally(initialOffsetX = { -(it) }),
                                        exit = slideOutHorizontally(targetOffsetX = { -(it) })
                                    ).renderInSharedTransitionScopeOverlay(zIndexInOverlay = 1f)
                                    .fillMaxSize()
                            ) {

                                Text(
                                    color = Color.White,
                                    modifier = Modifier
                                        .align(Alignment.Start)
                                        .padding(horizontal = 20.dp, vertical = 5.dp)
                                        .background(bottomNavBarContainerColor)
                                        .padding(12.dp),
                                    text = "Hi, please check the new task."
                                )
                            }
                        }

                        item {
                            Column(
                                modifier = Modifier
                                    .animateEnterExit(
                                        enter = slideInHorizontally(initialOffsetX = { (it) }),
                                        exit = slideOutHorizontally(targetOffsetX = { (it) })
                                    ).renderInSharedTransitionScopeOverlay(zIndexInOverlay = 1f)
                                    .fillMaxSize()
                            ) {
                                var seen by remember { mutableStateOf(true) }
                                Text(
                                    fontSize = 14.sp,
                                    color = Color.Black,
                                    modifier = Modifier
                                        .align(Alignment.End)
                                        .padding(horizontal = 20.dp, vertical = 5.dp)
                                        .background(yellowColor)
                                        .clickable { seen = !seen }
                                        .padding(12.dp),
                                    text = "Got it. Will check it soon."
                                )
                                AnimatedVisibility(
                                    enter = fadeIn() + expandVertically(animationSpec = tween(100)),
                                    exit = fadeOut() + shrinkVertically(animationSpec = tween(100)),
                                    modifier = Modifier.padding(horizontal = 20.dp)
                                        .align(Alignment.End),
                                    visible = seen
                                ) {
                                    Text(
                                        fontSize = 12.sp,
                                        color = yellowColor,
                                        text = "Seen"
                                    )
                                }
                            }
                        }

                        item {
                            Column(
                                modifier = Modifier
                                    .animateEnterExit(
                                        enter = slideInHorizontally(initialOffsetX = { -(it) }),
                                        exit = slideOutHorizontally(targetOffsetX = { -(it) })
                                    ).renderInSharedTransitionScopeOverlay(zIndexInOverlay = 1f)
                                    .fillMaxSize()
                            ) {

                                Text(
                                    color = Color.White,
                                    modifier = Modifier
                                        .align(Alignment.Start)
                                        .padding(horizontal = 20.dp, vertical = 5.dp)
                                        .background(bottomNavBarContainerColor)
                                        .padding(12.dp),
                                    text = "Hi, please check the new task."
                                )
                            }
                        }

                        item {
                            Column(
                                modifier = Modifier
                                    .animateEnterExit(
                                        enter = slideInHorizontally(initialOffsetX = { (it) }),
                                        exit = slideOutHorizontally(targetOffsetX = { (it) })
                                    ).renderInSharedTransitionScopeOverlay(zIndexInOverlay = 1f)
                                    .fillMaxSize()
                            ) {
                                var seen by remember { mutableStateOf(true) }
                                Text(
                                    fontSize = 14.sp,
                                    color = Color.Black,
                                    modifier = Modifier
                                        .align(Alignment.End)
                                        .padding(horizontal = 20.dp, vertical = 5.dp)
                                        .background(yellowColor)
                                        .clickable { seen = !seen }
                                        .padding(12.dp),
                                    text = "Got it. Will check it soon."
                                )
                                AnimatedVisibility(
                                    enter = fadeIn() + expandVertically(animationSpec = tween(100)),
                                    exit = fadeOut() + shrinkVertically(animationSpec = tween(100)),
                                    modifier = Modifier.padding(horizontal = 20.dp)
                                        .align(Alignment.End),
                                    visible = seen
                                ) {
                                    Text(
                                        fontSize = 12.sp,
                                        color = yellowColor,
                                        text = "Seen"
                                    )
                                }
                            }
                        }

                        item {
                            Column(
                                modifier = Modifier
                                    .animateEnterExit(
                                        enter = slideInHorizontally(initialOffsetX = { -(it) }),
                                        exit = slideOutHorizontally(targetOffsetX = { -(it) })
                                    ).renderInSharedTransitionScopeOverlay(zIndexInOverlay = 1f)
                                    .fillMaxSize()
                            ) {

                                Text(
                                    color = Color.White,
                                    modifier = Modifier
                                        .align(Alignment.Start)
                                        .padding(horizontal = 20.dp, vertical = 5.dp)
                                        .background(bottomNavBarContainerColor)
                                        .padding(12.dp),
                                    text = "Hi, please check the new task."
                                )
                            }
                        }

                        item {
                            Column(
                                modifier = Modifier
                                    .animateEnterExit(
                                        enter = slideInHorizontally(initialOffsetX = { (it) }),
                                        exit = slideOutHorizontally(targetOffsetX = { (it) })
                                    ).renderInSharedTransitionScopeOverlay(zIndexInOverlay = 1f)
                                    .fillMaxSize()
                            ) {
                                var seen by remember { mutableStateOf(true) }
                                Text(
                                    fontSize = 14.sp,
                                    color = Color.Black,
                                    modifier = Modifier
                                        .align(Alignment.End)
                                        .padding(horizontal = 20.dp, vertical = 5.dp)
                                        .background(yellowColor)
                                        .clickable { seen = !seen }
                                        .padding(12.dp),
                                    text = "Got it. Will check it soon."
                                )
                                AnimatedVisibility(
                                    enter = fadeIn() + expandVertically(animationSpec = tween(100)),
                                    exit = fadeOut() + shrinkVertically(animationSpec = tween(100)),
                                    modifier = Modifier.padding(horizontal = 20.dp)
                                        .align(Alignment.End),
                                    visible = seen
                                ) {
                                    Text(
                                        fontSize = 12.sp,
                                        color = yellowColor,
                                        text = "Seen"
                                    )
                                }
                            }
                        }

                        item {
                            Column(
                                modifier = Modifier
                                    .animateEnterExit(
                                        enter = slideInHorizontally(initialOffsetX = { -(it) }),
                                        exit = slideOutHorizontally(targetOffsetX = { -(it) })
                                    ).renderInSharedTransitionScopeOverlay(zIndexInOverlay = 1f)
                                    .fillMaxSize()
                            ) {

                                Text(
                                    color = Color.White,
                                    modifier = Modifier
                                        .align(Alignment.Start)
                                        .padding(horizontal = 20.dp, vertical = 5.dp)
                                        .background(bottomNavBarContainerColor)
                                        .padding(12.dp),
                                    text = "Hi, please check the new task."
                                )
                            }
                        }

                        item {
                            Column(
                                modifier = Modifier
                                    .animateEnterExit(
                                        enter = slideInHorizontally(initialOffsetX = { (it) }),
                                        exit = slideOutHorizontally(targetOffsetX = { (it) })
                                    ).renderInSharedTransitionScopeOverlay(zIndexInOverlay = 1f)
                                    .fillMaxSize()
                            ) {
                                var seen by remember { mutableStateOf(true) }
                                Text(
                                    fontSize = 14.sp,
                                    color = Color.Black,
                                    modifier = Modifier
                                        .align(Alignment.End)
                                        .padding(horizontal = 20.dp, vertical = 5.dp)
                                        .background(yellowColor)
                                        .clickable { seen = !seen }
                                        .padding(12.dp),
                                    text = "Got it. Will check it soon."
                                )
                                AnimatedVisibility(
                                    enter = fadeIn() + expandVertically(animationSpec = tween(100)),
                                    exit = fadeOut() + shrinkVertically(animationSpec = tween(100)),
                                    modifier = Modifier.padding(horizontal = 20.dp)
                                        .align(Alignment.End),
                                    visible = seen
                                ) {
                                    Text(
                                        fontSize = 12.sp,
                                        color = yellowColor,
                                        text = "Seen"
                                    )
                                }
                            }
                        }

                        item {
                            Column(
                                modifier = Modifier
                                    .animateEnterExit(
                                        enter = slideInHorizontally(initialOffsetX = { -(it) }),
                                        exit = slideOutHorizontally(targetOffsetX = { -(it) })
                                    ).renderInSharedTransitionScopeOverlay(zIndexInOverlay = 1f)
                                    .fillMaxSize()
                            ) {

                                Text(
                                    color = Color.White,
                                    modifier = Modifier
                                        .align(Alignment.Start)
                                        .padding(horizontal = 20.dp, vertical = 5.dp)
                                        .background(bottomNavBarContainerColor)
                                        .padding(12.dp),
                                    text = "Hi, please check the new task."
                                )
                            }
                        }

                        item {
                            Column(
                                modifier = Modifier
                                    .animateEnterExit(
                                        enter = slideInHorizontally(initialOffsetX = { (it) }),
                                        exit = slideOutHorizontally(targetOffsetX = { (it) })
                                    ).renderInSharedTransitionScopeOverlay(zIndexInOverlay = 1f)
                                    .fillMaxSize()
                            ) {
                                var seen by remember { mutableStateOf(true) }
                                Text(
                                    fontSize = 14.sp,
                                    color = Color.Black,
                                    modifier = Modifier
                                        .align(Alignment.End)
                                        .padding(horizontal = 20.dp, vertical = 5.dp)
                                        .background(yellowColor)
                                        .clickable { seen = !seen }
                                        .padding(12.dp),
                                    text = "Got it. Will check it soon."
                                )
                                AnimatedVisibility(
                                    enter = fadeIn() + expandVertically(animationSpec = tween(100)),
                                    exit = fadeOut() + shrinkVertically(animationSpec = tween(100)),
                                    modifier = Modifier.padding(horizontal = 20.dp)
                                        .align(Alignment.End),
                                    visible = seen
                                ) {
                                    Text(
                                        fontSize = 12.sp,
                                        color = yellowColor,
                                        text = "Seen"
                                    )
                                }
                            }
                        }

                        item {
                            Column(
                                modifier = Modifier
                                    .animateEnterExit(
                                        enter = slideInHorizontally(initialOffsetX = { -(it) }),
                                        exit = slideOutHorizontally(targetOffsetX = { -(it) })
                                    ).renderInSharedTransitionScopeOverlay(zIndexInOverlay = 1f)
                                    .fillMaxSize()
                            ) {

                                Text(
                                    color = Color.White,
                                    modifier = Modifier
                                        .align(Alignment.Start)
                                        .padding(horizontal = 20.dp, vertical = 5.dp)
                                        .background(bottomNavBarContainerColor)
                                        .padding(12.dp),
                                    text = "Hi, please check the new task."
                                )
                            }
                        }

                        item {
                            Column(
                                modifier = Modifier
                                    .animateEnterExit(
                                        enter = slideInHorizontally(initialOffsetX = { (it) }),
                                        exit = slideOutHorizontally(targetOffsetX = { (it) })
                                    ).renderInSharedTransitionScopeOverlay(zIndexInOverlay = 1f)
                                    .fillMaxSize()
                            ) {
                                var seen by remember { mutableStateOf(true) }
                                Text(
                                    fontSize = 14.sp,
                                    color = Color.Black,
                                    modifier = Modifier
                                        .align(Alignment.End)
                                        .padding(horizontal = 20.dp, vertical = 5.dp)
                                        .background(yellowColor)
                                        .clickable { seen = !seen }
                                        .padding(12.dp),
                                    text = "Got it. Will check it soon."
                                )
                                AnimatedVisibility(
                                    enter = fadeIn() + expandVertically(animationSpec = tween(100)),
                                    exit = fadeOut() + shrinkVertically(animationSpec = tween(100)),
                                    modifier = Modifier.padding(horizontal = 20.dp)
                                        .align(Alignment.End),
                                    visible = seen
                                ) {
                                    Text(
                                        fontSize = 12.sp,
                                        color = yellowColor,
                                        text = "Seen"
                                    )
                                }
                            }
                        }

                        item {
                            Column(
                                modifier = Modifier
                                    .animateEnterExit(
                                        enter = slideInHorizontally(initialOffsetX = { -(it) }),
                                        exit = slideOutHorizontally(targetOffsetX = { -(it) })
                                    ).renderInSharedTransitionScopeOverlay(zIndexInOverlay = 1f)
                                    .fillMaxSize()
                            ) {

                                Text(
                                    color = Color.White,
                                    modifier = Modifier
                                        .align(Alignment.Start)
                                        .padding(horizontal = 20.dp, vertical = 5.dp)
                                        .background(bottomNavBarContainerColor)
                                        .padding(12.dp),
                                    text = "Hi, please check the new task."
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}