package com.example.daytask.presentation.authentication.presentation

import android.widget.Toast
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
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imeNestedScroll
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.daytask.R
import com.example.daytask.presentation.authentication.presentation.events.RegisterEvent
import com.example.daytask.presentation.authentication.domain.model.RegisterState
import com.example.daytask.presentation.boarding.components.SplashSmallText
import com.example.daytask.presentation.common.components.ReusableLargeButton
import com.example.daytask.presentation.common.components.ReusableOutlinedButton
import com.example.daytask.presentation.common.components.ReusableTextField
import com.example.daytask.presentation.common.components.SquareCheckBox
import com.example.daytask.presentation.ui.theme.splashBgColor
import com.example.daytask.presentation.ui.theme.yellowColor
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalLayoutApi::class, ExperimentalSharedTransitionApi::class)
@Composable
fun Register(
    modifier: Modifier = Modifier,
    navigateToLogin: () -> Unit,
    registerSuccess: () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {

    val authenticationViewModel: AuthenticationViewModel = viewModel()
    val state by authenticationViewModel.registerState.collectAsState()
    val onRegisterEvent = authenticationViewModel::onRegisterEvent

    val context = LocalContext.current
    LaunchedEffect(context) {
        authenticationViewModel.registerValidationEvent.collect { event ->
            when (event) {
                is AuthenticationViewModel.ValidationEvent.Success -> {
                    Toast.makeText(context, "Registered Successfully", Toast.LENGTH_SHORT).show()
                    registerSuccess()
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
                                sharedContentState = rememberSharedContentState("registerLogo"),
                                animatedVisibilityScope = animatedVisibilityScope
                            )
                            .fillMaxWidth()
                            .statusBarsPadding()
                            .padding(horizontal = 20.dp)
                    ) {
                        Image(
                            modifier = Modifier
                                .animateEnterExit(
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
                            text = "Create your account"
                        )
                        Spacer(Modifier.height(16.dp))
                        Text(
                            modifier = Modifier
                                .animateEnterExit(
                                    enter = slideInHorizontally { it },
                                    exit = slideOutHorizontally { -it }
                                ),
                            color = Color.White.copy(0.5f),
                            text = "Full Name"
                        )
                        Spacer(Modifier.height(8.dp))
                        ReusableTextField(
                            modifier = Modifier
                                .animateEnterExit(
                                    enter = slideInHorizontally { it },
                                    exit = slideOutHorizontally { -it }
                                )
                                .fillMaxWidth(),
                            value = state.fullName,
                            onValueChange = { onRegisterEvent(RegisterEvent.OnFullNameChange(it)) },
                            placeHolder = "Full Name",
                            leadingIcon = R.drawable.usertag,
                            trailingIcon = 0,
                            isError = state.fullNameError != null,
                            supportingText = state.fullNameError,
                            visualTransformation = VisualTransformation.None,
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
                                .fillMaxWidth(),
                            value = state.email,
                            onValueChange = { onRegisterEvent(RegisterEvent.OnEmailChange(it)) },
                            placeHolder = "Email",
                            leadingIcon = R.drawable.usertag,
                            trailingIcon = 0,
                            isError = state.emailError != null,
                            supportingText = state.emailError,
                            visualTransformation = VisualTransformation.None,
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
                                .fillMaxWidth(),
                            value = state.password,
                            onValueChange = { onRegisterEvent(RegisterEvent.OnPasswordChange(it)) },
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
                                    onRegisterEvent(RegisterEvent.Register)
                                }
                            )
                        )
                        Spacer(Modifier.height(16.dp))
                        Row(
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.Top,
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {

                            SquareCheckBox(
                                enabled= true,
                                modifier = Modifier
                                    .animateEnterExit(
                                        enter = slideInHorizontally { it },
                                        exit = slideOutHorizontally { -(2*it) }
                                    ),
                                value = state.terms,
                                onTickMarkChange = {
                                    if (state.terms) {
                                        onRegisterEvent(RegisterEvent.OnTermsChange(false))
                                    } else {
                                        onRegisterEvent(RegisterEvent.OnTermsChange(true))
                                    }
                                }
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                color = Color.White.copy(0.5f),
                                fontSize = 14.sp,
                                modifier = Modifier
                                    .animateEnterExit(
                                        enter = slideInHorizontally { it },
                                        exit = slideOutHorizontally { -it }
                                    )
                                    .fillMaxWidth(),
                                text = buildAnnotatedString {
                                    val text =
                                        "I have read & agreed to DayTask  Privacy Policy, Terms & Condition"
                                    val start = text.indexOf("Privacy")
                                    val end = text.length
                                    append(text)
                                    addStyle(
                                        SpanStyle(color = yellowColor),
                                        start, end
                                    )
                                }
                            )
                        }
                        state.termsError?.let { error ->
                            Text(
                                modifier = Modifier
                                    .animateEnterExit(
                                        enter = slideInHorizontally { it },
                                        exit = slideOutHorizontally { -it }
                                    ),
                                color = MaterialTheme.colorScheme.error,
                                text = error
                            )
                        }
                        Spacer(Modifier.height(24.dp))
                        ReusableLargeButton(
                            modifier = Modifier
                                .animateEnterExit(
                                    enter = slideInHorizontally { it },
                                    exit = slideOutHorizontally { -it }
                                )
                                .sharedElement(
                                    sharedContentState = rememberSharedContentState("registerButton"),
                                    animatedVisibilityScope = animatedVisibilityScope
                                )
                                .fillMaxWidth(),
                            buttonText = "Sign Up",
                            onClick = {
                                onRegisterEvent(RegisterEvent.Register)
                            }
                        )
                    }
                }

                item {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
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
                            onClick = { onRegisterEvent(RegisterEvent.RegisterWithGoogle) }
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
                            text = "Already have an account?"
                        )
                        Text(
                            modifier = Modifier
                                .clip(CircleShape)
                                .clickable { navigateToLogin() }
                                .padding(horizontal = 8.dp),
                            color = yellowColor,
                            text = "Log In"
                        )
                    }
                }
            }
        }
    }
}