package com.femi.e_class.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.femi.e_class.R
import com.femi.e_class.handleNetworkExceptions
import com.femi.e_class.presentation.UpdateProfileFormEvent
import com.femi.e_class.viewmodels.HomeActivityViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(viewModel: HomeActivityViewModel) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val state = viewModel.updateProfileValidationFormState
    var loading by remember { mutableStateOf(false) }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .verticalScroll(scrollState)
            .fillMaxSize()
            .padding(0.dp, 10.dp, 0.dp, 30.dp),
    ) {
        LaunchedEffect(key1 = context) {
            viewModel.updateProfileValidationEvents.collect { event ->
                when (event) {
                    is HomeActivityViewModel.UpdateProfileValidationEvent.Success -> {
                        viewModel.updateUser()
                    }
                }
            }
        }
        LaunchedEffect(key1 = viewModel.updateProfileEvents) {
            viewModel.updateProfileEvents.collect { event ->
                loading = (event is HomeActivityViewModel.UpdateProfileEvent.Loading)
                when (event) {
                    is HomeActivityViewModel.UpdateProfileEvent.Success -> {
                        Toast.makeText(context,
                            "Profile updated successfully",
                            Toast.LENGTH_LONG).show()
                    }
                    is HomeActivityViewModel.UpdateProfileEvent.Error -> {
                        context.handleNetworkExceptions(event.exception,
                            retry = {
                                viewModel.onEvent(UpdateProfileFormEvent.Submit)
                            })
                    }
                    is HomeActivityViewModel.UpdateProfileEvent.Loading -> {}
                }
            }
        }
        Text(
            modifier = Modifier
                .align(Alignment.Start)
                .padding(20.dp, 0.dp, 20.dp, 0.dp),
            text = "Enter your details to update",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            textAlign = TextAlign.Start,
            style = TextStyle(
                color = MaterialTheme.colorScheme.primary
            )
        )
        Spacer(modifier = Modifier.height(30.dp))
        OutlinedTextField(
            value = state.firstName,
            label = { Text(text = "First Name") },
            onValueChange = {
                viewModel.onEvent(UpdateProfileFormEvent.FirstNameChanged(it))
            },
            isError = state.firstNameError != null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(30.dp, 0.dp, 30.dp, 0.dp),
            maxLines = 2,
            leadingIcon = {
                Icon(Icons.Filled.Person, "First Name Icon")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Words,
                autoCorrect = false,
                imeAction = ImeAction.Next
            )
        )
        if (state.firstNameError != null) {
            Text(
                text = state.firstNameError,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(30.dp, 0.dp, 30.dp, 0.dp)
            )
        }
        Spacer(modifier = Modifier.height(40.dp))
        OutlinedTextField(
            value = state.lastName,
            label = { Text(text = "Last Name") },
            onValueChange = {
                viewModel.onEvent(UpdateProfileFormEvent.LastNameChanged(it))
            },
            isError = state.lastNameError != null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(30.dp, 0.dp, 30.dp, 0.dp),
            maxLines = 2,
            leadingIcon = {
                Icon(Icons.Filled.Person, "Last Name Icon")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Words,
                autoCorrect = false,
                imeAction = ImeAction.Next
            )
        )
        if (state.lastNameError != null) {
            Text(
                text = state.lastNameError,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(30.dp, 0.dp, 30.dp, 0.dp)
            )
        }
        Spacer(modifier = Modifier.height(40.dp))
        OutlinedTextField(
            value = state.matric,
            label = { Text(text = "Matric") },
            onValueChange = {
                viewModel.onEvent(UpdateProfileFormEvent.MatricChanged(it))
            },
            isError = state.matricError != null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(30.dp, 0.dp, 30.dp, 0.dp),
            maxLines = 2,
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_school),
                    contentDescription = "Matric Number Icon")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.NumberPassword,
                capitalization = KeyboardCapitalization.None,
                autoCorrect = false,
                imeAction = ImeAction.Next
            )
        )
        if (state.matricError != null) {
            Text(
                text = state.matricError,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(30.dp, 0.dp, 30.dp, 0.dp)
            )
        }
        Spacer(modifier = Modifier.height(40.dp))
        OutlinedTextField(
            value = state.email,
            label = { Text(text = "Email") },
            onValueChange = {
                viewModel.onEvent(UpdateProfileFormEvent.EmailChanged(it))
            },
            isError = state.emailError != null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(30.dp, 0.dp, 30.dp, 0.dp),
            maxLines = 2,
            leadingIcon = {
                Icon(Icons.Filled.Email, "Email Icon")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                capitalization = KeyboardCapitalization.None,
                autoCorrect = false,
                imeAction = ImeAction.Done
            )
        )
        if (state.emailError != null) {
            Text(
                text = state.emailError,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(30.dp, 0.dp, 30.dp, 0.dp)
            )
        }
        Spacer(modifier = Modifier.height(40.dp))
        if (loading) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(40.dp))
        }
        Button(
            onClick = {
                viewModel.onEvent(UpdateProfileFormEvent.Submit)
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
                .padding(30.dp, 0.dp, 30.dp, 0.dp),
            enabled = !loading
        ) {
            Text(text = "Update Profile")
        }

    }
}