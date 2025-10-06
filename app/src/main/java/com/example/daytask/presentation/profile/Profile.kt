package com.example.daytask.presentation.profile

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil3.compose.AsyncImage
import com.example.daytask.R
import com.example.daytask.data.local.model.UserState
import com.example.daytask.domain.UserEvent
import com.example.daytask.presentation.common.components.ReusableLargeButton
import com.example.daytask.presentation.ui.theme.contentDarkGrayBg
import com.example.daytask.presentation.ui.theme.splashBgColor
import com.example.daytask.presentation.ui.theme.yellowColor
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.storage.storage

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun Profile(
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit,
    onUserEvent: (UserEvent) -> Unit,
    userState: UserState,
    navigateToLogin: () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {

    val context = LocalContext.current
    var currentProfileImage by remember { mutableStateOf(userState.firebaseUser?.photoUrl) }
    val storage = Firebase.storage
    val storageRef = storage.reference
    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                userState.firebaseUser?.let { user ->
                    val imageRef = storageRef.child("images/${user.uid}.jpg")
                    val uploadTask = imageRef.putFile(uri)
                    uploadTask.addOnSuccessListener { task ->
                        imageRef.downloadUrl.addOnSuccessListener { url ->
                            val profileUpdates = UserProfileChangeRequest.Builder()
                                .setPhotoUri(url)
                                .build()
                            user.updateProfile(profileUpdates)
                        }
                        Toast.makeText(context, "Profile picture updated", Toast.LENGTH_SHORT)
                            .show()
                    }.addOnFailureListener {
                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    )

    val profileScreenItems = listOf(
        ProfileScreenItems(
            leadingIcon = R.drawable.useradd,
            title = userState.firebaseUser?.displayName ?: "Anonymous",
            trailingIcon = R.drawable.edit,
            onClick = { }
        ),
        ProfileScreenItems(
            leadingIcon = R.drawable.usertag,
            title = userState.firebaseUser?.email ?: "anonymous",
            trailingIcon = R.drawable.edit,
            onClick = { }
        ),
        ProfileScreenItems(
            leadingIcon = R.drawable.lock,
            title = "Password",
            trailingIcon = R.drawable.edit,
            onClick = { }
        ),
        ProfileScreenItems(
            leadingIcon = R.drawable.task,
            title = "My Tasks",
            trailingIcon = R.drawable.arrowdown2,
            onClick = { }
        ),
        ProfileScreenItems(
            leadingIcon = R.drawable.securitycard,
            title = "Privacy",
            trailingIcon = R.drawable.arrowdown2,
            onClick = { }
        ),
        ProfileScreenItems(
            leadingIcon = R.drawable.setting2,
            title = "Settings",
            trailingIcon = R.drawable.arrowdown2,
            onClick = { }
        ),
    )

    with(sharedTransitionScope) {
        with(animatedVisibilityScope) {
            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        modifier = Modifier
                            .animateEnterExit(
                                enter = slideInVertically(initialOffsetY = { -(it * 2) }),
                                exit = slideOutVertically(targetOffsetY = { -(it * 2) })
                            ),
                        title = { Text(color = Color.White, text = "Profile") },
                        navigationIcon = {
                            IconButton(
                                onClick = navigateUp
                            ) {
                                Icon(
                                    tint = Color.White,
                                    contentDescription = null,
                                    painter = painterResource(R.drawable.arrowleft)
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = splashBgColor
                        )
                    )
                }
            ) { paddings ->
                Surface(
                    color = splashBgColor
                ) {
                    with(sharedTransitionScope) {
                        LazyColumn(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            contentPadding = paddings,
                            modifier = modifier
                                .fillMaxSize(),
                        ) {

                            item {
                                Spacer(Modifier.height(32.dp))
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .sharedElement(
                                            sharedContentState = rememberSharedContentState("userProfile"),
                                            animatedVisibilityScope = animatedVisibilityScope
                                        )
                                        .size(150.dp)
                                ) {
                                    AsyncImage(
                                        modifier = Modifier
                                            .size(150.dp)
                                            .clip(CircleShape)
                                            .clickable {
                                                photoPicker.launch(
                                                    PickVisualMediaRequest(
                                                        ActivityResultContracts.PickVisualMedia.ImageOnly
                                                    )
                                                )
                                            }
                                            .border(2.dp, yellowColor, CircleShape),
                                        clipToBounds = true,
                                        contentScale = ContentScale.Crop,
                                        contentDescription = null,
                                        error = painterResource(R.drawable.profile_image),
                                        model = userState.firebaseUser?.photoUrl ?: ""
                                    )
                                    IconButton(
                                        modifier = Modifier
                                            .zIndex(1f)
                                            .align(Alignment.BottomEnd),
                                        colors = IconButtonDefaults.iconButtonColors(
                                            containerColor = splashBgColor
                                        ),
                                        onClick = {}
                                    ) {
                                        Icon(
                                            tint = Color.White,
                                            contentDescription = null,
                                            painter = painterResource(R.drawable.addsquare)
                                        )
                                    }
                                }
                                Spacer(Modifier.height(32.dp))
                            }

                            itemsIndexed(
                                key = { index, item -> "${index}${item.title}" },
                                items = profileScreenItems
                            ) { index, item ->
                                Card(
                                    modifier = Modifier
                                        .animateEnterExit(
                                            enter = slideInHorizontally { (index + 1) * it },
                                            exit = slideOutHorizontally { -((index + 1) * it) }
                                        )
                                        .padding(vertical = 8.dp, horizontal = 32.dp),
                                    shape = RectangleShape,
                                    colors = CardDefaults.cardColors(
                                        containerColor = contentDarkGrayBg,
                                        contentColor = Color.White
                                    )
                                ) {
                                    Row(
                                        modifier = Modifier.padding(16.dp)
                                    ) {
                                        Icon(
                                            modifier = Modifier.padding(start = 4.dp),
                                            tint = Color.White.copy(0.7f),
                                            contentDescription = null,
                                            painter = painterResource(item.leadingIcon)
                                        )
                                        Spacer(Modifier.width(16.dp))
                                        Text(
                                            fontSize = 18.sp,
                                            text = item.title
                                        )
                                        Spacer(Modifier.weight(1f))
                                        Icon(
                                            tint = Color.White.copy(0.7f),
                                            contentDescription = null,
                                            painter = painterResource(item.trailingIcon)
                                        )
                                    }
                                }
                            }

                            item {
                                Spacer(Modifier.height(24.dp))
                                ReusableLargeButton(
                                    modifier = Modifier
                                        .animateEnterExit(
                                            enter = slideInHorizontally { it },
                                            exit = slideOutHorizontally { -it }
                                        )
                                        .fillMaxWidth(0.85f),
                                    buttonText = "Logout",
                                    buttonIcon = R.drawable.logoutcurve,
                                    onClick = {
                                        FirebaseAuth.getInstance().signOut()
                                        onUserEvent(UserEvent.UpdateUser(null))
                                        navigateToLogin()
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

data class ProfileScreenItems(
    val leadingIcon: Int,
    val title: String,
    val trailingIcon: Int,
    val onClick: () -> Unit
)