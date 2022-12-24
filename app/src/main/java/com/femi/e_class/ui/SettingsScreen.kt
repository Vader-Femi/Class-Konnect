package com.femi.e_class.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.femi.e_class.R
import com.femi.e_class.presentation.VerifyIdentityFormEvent
import com.femi.e_class.viewmodels.HomeActivityViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(viewModel: HomeActivityViewModel) {
    var userResolution by remember { mutableStateOf(720) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    var showLogOutDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showVerifyIdentity by remember { mutableStateOf(false) }
    var verifyingIdentity by remember { mutableStateOf(false) }
    var deletingAccount by remember { mutableStateOf(false) }
    var is720Checked by remember { mutableStateOf(false) }
    var is1080Checked by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        userResolution = viewModel.videoResolution()
    }

    LaunchedEffect(key1 = viewModel.deleteAccountEvents) {
        viewModel.deleteAccountEvents.collect { event ->
            deletingAccount =
                (event is HomeActivityViewModel.DeleteAccountEvent.Loading)
            when (event) {
                is HomeActivityViewModel.DeleteAccountEvent.Success -> {
                    context.logOut(viewModel)
                }
                is HomeActivityViewModel.DeleteAccountEvent.Error -> {
                    Toast.makeText(context,
                        event.exception?.message,
                        Toast.LENGTH_SHORT)
                        .show()
                }
                else -> {

                }
            }
        }
    }

    LaunchedEffect(key1 = viewModel.verifyIdentityEvents) {
        coroutineScope.launch {
//            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.verifyIdentityEvents.collect { event ->
                    verifyingIdentity =
                        (event is HomeActivityViewModel.VerifyIdentityEvent.Loading)
                    when (event) {
                        is HomeActivityViewModel.VerifyIdentityEvent.Success -> {
                            viewModel.deleteAccount()
                        }
                        is HomeActivityViewModel.VerifyIdentityEvent.Error -> {
                            Toast.makeText(context,
                                event.exception?.message,
                                Toast.LENGTH_SHORT)
                                .show()
                        }
                        is HomeActivityViewModel.VerifyIdentityEvent.Loading -> {
                        }
                    }
                }
//            }
        }
    }
    LaunchedEffect(key1 = userResolution) {
        if (userResolution == 720) {
            is720Checked = true
            is1080Checked = false
        } else {
            is1080Checked = true
            is720Checked = false
        }
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .verticalScroll(scrollState)
            .fillMaxSize()
            .padding(0.dp, 10.dp, 0.dp, 30.dp)
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.Start)
                .padding(20.dp, 0.dp, 20.dp, 0.dp),
            text = "Set video resolution",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            textAlign = TextAlign.Start,
            style = TextStyle(
                color = MaterialTheme.colorScheme.primary
            )
        )
        Spacer(modifier = Modifier.height(10.dp))
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp, 0.dp, 20.dp, 0.dp),
            shape = RoundedCornerShape(15.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Lower Video Quality (720P)",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
                Switch(
                    checked = is720Checked,
                    modifier = Modifier.align(Alignment.CenterVertically),
                    onCheckedChange = { isChecked ->
                        if (isChecked) {
                            is720Checked = isChecked
                            is1080Checked = !isChecked
                            viewModel.videoResolution(720)
                        } else {
                            is720Checked = !isChecked
                            is1080Checked = isChecked
                            viewModel.videoResolution(1080)
                        }
                    })
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp, 0.dp, 20.dp, 0.dp),
            shape = RoundedCornerShape(15.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "High Video Quality (1080P)",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
                Switch(
                    checked = is1080Checked,
                    modifier = Modifier.align(Alignment.CenterVertically),
                    onCheckedChange = { isChecked ->
                        if (isChecked) {
                            is1080Checked = isChecked
                            is720Checked = !isChecked
                            viewModel.videoResolution(1080)
                        } else {
                            is1080Checked = !isChecked
                            is720Checked = isChecked
                            viewModel.videoResolution(720)
                        }
                    })
            }
        }
        Spacer(modifier = Modifier.height(30.dp))
        Text(
            modifier = Modifier
                .align(Alignment.Start)
                .padding(20.dp, 0.dp, 20.dp, 0.dp),
            text = "Account settings",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            textAlign = TextAlign.Start,
            style = TextStyle(
                color = MaterialTheme.colorScheme.primary
            )
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp, 0.dp, 20.dp, 0.dp)
        ) {
            Button(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .padding(end = 5.dp),
                onClick = {
                    showLogOutDialog = true
                }) {
                Text(text = "Log Out")
            }
            OutlinedButton(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(start = 5.dp),
                onClick = {
                    showDeleteDialog = true
                }) {
                Text(text = "Delete Account")
            }
        }
        if (showLogOutDialog) {
            AlertDialog(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
//                                        .padding(0.dp, 30.dp, 0.dp, 0.dp)
                    .align(Alignment.CenterHorizontally),
                title = {
                    Text(text = "Hold up")
                },
                text = {
                    Text(
                        text = "Are you sure you want to log out",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = { context.logOut(viewModel) },
                        content = {
                            Text(text = "Yes I am")
                        },
                    )
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showLogOutDialog = false
                        },
                        content = {
                            Text(text = "No")
                        },
                    )
                },
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 20.dp,
                onDismissRequest = {
                    showLogOutDialog = false
                }
            )
        }
        if (showDeleteDialog) {
            AlertDialog(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
//                                        .padding(0.dp, 30.dp, 0.dp, 0.dp)
                    .align(Alignment.CenterHorizontally),
                title = {
                    Text(text = "Hold up")
                },
                text = {
                    Text(
                        text = "Your account will be PERMANENTLY deleted\nDo you want to continue?",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showDeleteDialog = false
                            showVerifyIdentity = true
                        },
                        content = {
                            Text(text = "Continue")
                        },
                    )
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showDeleteDialog = false
                        },
                        content = {
                            Text(text = "No")
                        },
                    )
                },
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 20.dp,
                onDismissRequest = {
                    showDeleteDialog = false
                }
            )
        }

        if (showVerifyIdentity) {
            val state = viewModel.verifyIdentityFormState
            var showPassword by remember { mutableStateOf(false) }
            AlertDialog(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(0.dp, 0.dp, 0.dp, 20.dp)
                    .align(Alignment.CenterHorizontally),
                title = {
                    Text(text = "Verify your Identify")
                },
                text = {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            modifier = Modifier
                                .align(Alignment.Start),
                            text = "Enter your password to verify",
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Start
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        OutlinedTextField(
                            value = state.password,
                            label = { Text(text = "Password") },
                            onValueChange = {
                                viewModel.onEvent(VerifyIdentityFormEvent.PasswordChanged(
                                    it))
                            },
                            isError = state.passwordError != null,
                            modifier = Modifier.fillMaxWidth(),
                            maxLines = 2,
                            visualTransformation = if (showPassword) {
                                VisualTransformation.None
                            } else {
                                PasswordVisualTransformation()
                            },
                            trailingIcon = {
                                if (showPassword) {
                                    IconButton(onClick = {
                                        showPassword = false
                                    }) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_visibility),
                                            contentDescription = "Show Password"
                                        )
                                    }
                                } else {
                                    IconButton(onClick = {
                                        showPassword = true
                                    }) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_visibility_off),
                                            contentDescription = "Hide Password"
                                        )
                                    }
                                }
                            },
                            leadingIcon = {
                                Icon(Icons.Filled.Lock, "Password Icon")
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password,
                                capitalization = KeyboardCapitalization.None,
                                autoCorrect = false,
                                imeAction = ImeAction.Done
                            )
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        if (state.passwordError != null) {
                            Text(
                                text = state.passwordError,
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.align(Alignment.End)
                            )
                        }
                        if (verifyingIdentity) {
                            CircularProgressIndicator()
                        }
                        if (deletingAccount) {
                            CircularProgressIndicator()
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.onEvent(VerifyIdentityFormEvent.Submit)
                        },
                        content = {
                            Text(text = "Verify")
                        },
                    )
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showVerifyIdentity = false
                        },
                        content = {
                            Text(text = "Cancel")
                        },
                    )
                },
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 20.dp,
                onDismissRequest = {
                    showVerifyIdentity = false
                }
            )
        }
    }
}

fun Context.logOut(viewModel: HomeActivityViewModel) {
    val activity = this as Activity
    viewModel.logOut()
    Intent(activity, MainActivity::class.java).also {
        it.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(it)
        activity.finish()
    }
}