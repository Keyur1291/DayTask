package com.example.daytask.presentation.authentication.presentation

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentType
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.daytask.R
import com.example.daytask.presentation.authentication.domain.GoogleAuth
import com.example.daytask.presentation.authentication.presentation.events.LoginEvent
import com.example.daytask.presentation.boarding.components.SplashSmallText
import com.example.daytask.presentation.common.components.ReusableLargeButton
import com.example.daytask.presentation.common.components.ReusableOutlinedButton
import com.example.daytask.presentation.common.components.ReusableTextField
import com.example.daytask.presentation.ui.theme.splashBgColor
import com.example.daytask.presentation.ui.theme.yellowColor

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun Login(
    modifier: Modifier = Modifier,
    navigateToRegister: () -> Unit,
    loginSuccess: () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {

    val authenticationViewModel: AuthenticationViewModel = viewModel()
    val state by authenticationViewModel.loginState.collectAsState()
    val onLoginEvent = authenticationViewModel::onLoginEvent

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        GoogleAuth.googleSignIn(
            context = context,
            scope = scope,
            login = {
                onLoginEvent(LoginEvent.LoginWithGoogle(it))
            }
        )
    }

    LaunchedEffect(context) {
        authenticationViewModel.loginValidationEvent.collect { event ->
            when (event) {
                is AuthenticationViewModel.ValidationEvent.Success -> {
                    Toast.makeText(context, "Login Success", Toast.LENGTH_SHORT).show()
                    loginSuccess()
                }
            }
        }
    }
    var isTrailingActive by remember { mutableStateOf(true) }

    with(sharedTransitionScope) {
        with(animatedVisibilityScope) {
            LazyColumn(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier
                    .fillMaxSize()
                    .padding(bottom = 12.dp)
                    .imePadding()
            ) {

                item {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .sharedElement(
                                sharedContentState = rememberSharedContentState("loginLogo"),
                                animatedVisibilityScope = animatedVisibilityScope
                            )
                            .sharedElement(
                                sharedContentState = rememberSharedContentState("registerLogo"),
                                animatedVisibilityScope = animatedVisibilityScope
                            )
                            .fillMaxWidth()
                            .statusBarsPadding()
                            .padding(horizontal = 20.dp)
                    ) {
                        Image(
                            modifier = Modifier.animateEnterExit(
                                enter = slideInHorizontally { it },
                                exit = slideOutHorizontally { -(2*it) }
                            ),
                            contentDescription = null,
                            painter = painterResource(R.drawable.ic_logo)
                        )
                        SplashSmallText(
                            modifier = Modifier
                                .animateEnterExit(
                                    enter = slideInHorizontally { it },
                                    exit = slideOutHorizontally { -(2*it) }
                                ),
                            textSize = 24
                        )
                    }
                }

                item {
                    Spacer(Modifier.height(24.dp))
                    Column(
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                    ) {

                        Text(
                            modifier = Modifier
                                .animateEnterExit(
                                    enter = slideInHorizontally { it },
                                    exit = slideOutHorizontally { -it }
                                )
                                .sharedElement(
                                    sharedContentState = rememberSharedContentState("WelcomeText"),
                                    animatedVisibilityScope = animatedVisibilityScope,
                                ),
                            color = Color.White,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.SemiBold,
                            text = "Welcome Back"
                        )
                        Spacer(Modifier.height(16.dp))
                        Text(
                            modifier = Modifier
                                .animateEnterExit(
                                    enter = slideInHorizontally { it },
                                    exit = slideOutHorizontally { -it }
                                )
                                .sharedElement(
                                    sharedContentState = rememberSharedContentState("registerEmailTitle"),
                                    animatedVisibilityScope = animatedVisibilityScope
                                ),
                            color = Color.White.copy(0.5f),
                            text = "Email Address"
                        )
                        Spacer(Modifier.height(8.dp))
                        ReusableTextField(
                            modifier = Modifier
                                .animateEnterExit(
                                    enter = slideInHorizontally { it },
                                    exit = slideOutHorizontally { -it }
                                )
                                .sharedElement(
                                    sharedContentState = rememberSharedContentState("registerEmail"),
                                    animatedVisibilityScope = animatedVisibilityScope
                                )
                                .semantics { contentType = ContentType.EmailAddress }
                                .fillMaxWidth(),
                            value = state.email,
                            onValueChange = { onLoginEvent(LoginEvent.OnEmailChange(it)) },
                            placeHolder = "Email",
                            leadingIcon = R.drawable.usertag,
                            trailingIcon = 0,
                            isError = state.emailError != null,
                            supportingText = state.emailError,
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Next
                            )
                        )
                        Text(
                            modifier = Modifier
                                .animateEnterExit(
                                    enter = slideInHorizontally { it },
                                    exit = slideOutHorizontally { -it }
                                )
                                .sharedElement(
                                    sharedContentState = rememberSharedContentState("registerPasswordTitle"),
                                    animatedVisibilityScope = animatedVisibilityScope
                                ),
                            color = Color.White.copy(0.5f),
                            text = "Password"
                        )
                        Spacer(Modifier.height(8.dp))
                        ReusableTextField(
                            modifier = Modifier
                                .animateEnterExit(
                                    enter = slideInHorizontally { it },
                                    exit = slideOutHorizontally { -it }
                                )
                                .sharedElement(
                                    sharedContentState = rememberSharedContentState("registerPassword"),
                                    animatedVisibilityScope = animatedVisibilityScope
                                )
                                .semantics { contentType = ContentType.Password }
                                .fillMaxWidth(),
                            value = state.password,
                            onValueChange = { onLoginEvent(LoginEvent.OnPasswordChange(it)) },
                            placeHolder = "Password",
                            leadingIcon = R.drawable.ic_lock,
                            trailingIcon = R.drawable.eyeslash,
                            trailingTint = if (isTrailingActive) yellowColor else Color.White,
                            onTrailingClick = { isTrailingActive = !isTrailingActive },
                            isError = state.passwordError != null,
                            supportingText = state.passwordError,
                            visualTransformation = if (!isTrailingActive) VisualTransformation.None else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Go
                            ),
                            keyboardActions = KeyboardActions(
                                onGo = {
                                    onLoginEvent(LoginEvent.Login)
                                }
                            )
                        )
                        Text(
                            modifier = Modifier
                                .animateEnterExit(
                                    enter = slideInHorizontally { it },
                                    exit = slideOutHorizontally { -(2*it) }
                                )
                                .align(Alignment.End)
                                .clip(CircleShape)
                                .clickable {}
                                .padding(horizontal = 8.dp),
                            color = Color.White.copy(0.5f),
                            text = "Forgot Password?"
                        )
                        Spacer(Modifier.height(24.dp))
                        ReusableLargeButton(
                            modifier = Modifier
                                .animateEnterExit(
                                    enter = slideInHorizontally { it },
                                    exit = slideOutHorizontally { -it }
                                )
                                .sharedElement(
                                    sharedContentState = rememberSharedContentState("loginButton"),
                                    animatedVisibilityScope = animatedVisibilityScope
                                )
                                .sharedElement(
                                    sharedContentState = rememberSharedContentState("registerButton"),
                                    animatedVisibilityScope = animatedVisibilityScope
                                )
                                .fillMaxWidth(),
                            buttonText = "Log In",
                            onClick = {
                                onLoginEvent(LoginEvent.Login)
                            }
                        )
                    }
                }

                item {
                    Column(
                        modifier = Modifier.padding(horizontal = 20.dp)
                    ) {
                        Spacer(Modifier.height(24.dp))
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .animateEnterExit(
                                    enter = slideInHorizontally { it },
                                    exit = slideOutHorizontally { -it }
                                )
                                .sharedElement(
                                    sharedContentState = rememberSharedContentState("continue"),
                                    animatedVisibilityScope = animatedVisibilityScope
                                )
                                .fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(1.dp)
                                    .background(Color.White.copy(0.5f))
                            )
                            Text(
                                softWrap = false,
                                maxLines = 1,
                                modifier = Modifier
                                    .background(splashBgColor)
                                    .padding(horizontal = 8.dp),
                                color = Color.White.copy(0.5f),
                                text = "Or continue with"
                            )
                        }
                        Spacer(Modifier.height(24.dp))
                        ReusableOutlinedButton(
                            modifier = Modifier
                                .animateEnterExit(
                                    enter = slideInHorizontally { it },
                                    exit = slideOutHorizontally { -it }
                                )
                                .sharedElement(
                                    sharedContentState = rememberSharedContentState("GoogleButton"),
                                    animatedVisibilityScope = animatedVisibilityScope
                                )
                                .fillMaxWidth(),
                            buttonText = "Google",
                            buttonIcon = R.drawable.google,
                            onClick = {
                                GoogleAuth.googleSignIn(
                                    context = context,
                                    scope = scope,
                                    login = {
                                        onLoginEvent(LoginEvent.LoginWithGoogle(it))
                                        loginSuccess()
                                    }
                                )
                            }
                        )
                        Spacer(Modifier.height(24.dp))
                    }
                }

                item {
                    Row(
                        modifier = Modifier
                            .animateEnterExit(
                                enter = slideInHorizontally { it },
                                exit = slideOutHorizontally { -it }
                            )
                            .sharedElement(
                                sharedContentState = rememberSharedContentState("SignUp"),
                                animatedVisibilityScope = animatedVisibilityScope
                            )
                            .navigationBarsPadding()
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            color = Color.White.copy(0.5f),
                            modifier = Modifier,
                            text = "Don't have an account?"
                        )
                        Text(
                            modifier = Modifier
                                .clip(CircleShape)
                                .clickable { navigateToRegister() }
                                .padding(horizontal = 8.dp),
                            color = yellowColor,
                            text = "Sign Up"
                        )
                    }
                }
            }
        }
    }
}