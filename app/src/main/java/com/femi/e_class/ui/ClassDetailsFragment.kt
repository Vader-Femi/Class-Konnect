package com.femi.e_class.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.femi.e_class.R
import com.femi.e_class.theme.E_ClassTheme
import com.femi.e_class.databinding.FragmentClassDetailsBinding
import com.femi.e_class.presentation.RoomFormEvent
import com.femi.e_class.viewmodels.HomeActivityViewModel

class ClassDetailsFragment : Fragment() {

    private var _binding: FragmentClassDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<HomeActivityViewModel>()
    private val preIndentedString = "LASU"

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentClassDetailsBinding.inflate(inflater, container, false)

        binding.composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                E_ClassTheme(dynamicColor = viewModel.useDynamicTheme) {
                    Surface {
                        val scrollState = rememberScrollState()
                        val context = LocalContext.current
                        val state = viewModel.roomFormState
                        var showPassword by remember { mutableStateOf(false) }
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .verticalScroll(scrollState)
                                .fillMaxSize()
                                .padding(30.dp, 0.dp, 30.dp, 30.dp),
                        ) {
                            LaunchedEffect(key1 = context) {
                                viewModel.roomEvents.collect { event ->
                                    when (event) {
                                        is HomeActivityViewModel.RoomEvent.Success -> {
                                            moveToCall()
                                        }
                                    }
                                }
                            }
//                            Text(
//                                modifier = Modifier
//                                    .align(Alignment.Start),
//                                text = "Start or join a class",
//                                fontWeight = FontWeight.ExtraBold,
//                                fontSize = 24.sp
//                            )
//                            Spacer(modifier = Modifier.height(1.dp))
                            Text(
                                modifier = Modifier
                                    .align(Alignment.Start),
                                text = "Enter the class details",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                style = TextStyle(
                                    color = MaterialTheme.colorScheme.primary
                                )
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                            OutlinedTextField(
                                value = state.courseCode,
                                label = { Text(text = "Course Code (No spaces)") },
                                onValueChange = { newText ->
                                    val input = newText
                                        .replace(" ","")
                                        .uppercase()
                                    viewModel.onEvent(RoomFormEvent.CourseCodeChanged(input))
                                },
                                isError = state.courseCodeError != null,
                                modifier = Modifier.fillMaxWidth(),
                                maxLines = 2,
                                leadingIcon = {
                                    Row(
                                        horizontalArrangement = Arrangement.SpaceAround
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_class),
                                            contentDescription = "Course Code Icon",
                                            modifier = Modifier.padding(start = 14.dp, end = 14.dp)
                                        )
                                        Text(
                                            text = preIndentedString
                                        )
                                    }
                                },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Text,
                                    capitalization = KeyboardCapitalization.Characters,
                                    autoCorrect = false,
                                    imeAction = ImeAction.Done
                                )
                            )
                            if (state.courseCodeError != null) {
                                Text(
                                    text = state.courseCodeError,
                                    color = MaterialTheme.colorScheme.error,
                                    modifier = Modifier
                                        .align(Alignment.End),
                                )
                            }
                            Spacer(modifier = Modifier.height(40.dp))
                            /*
                            OutlinedTextField(
                                value = state.roomPassword,
                                label = { Text(text = "Password") },
                                onValueChange = {
                                    viewModel.onEvent(RoomFormEvent.RoomPasswordChanged(it))
                                },
                                isError = state.roomPasswordError != null,
                                modifier = Modifier.fillMaxWidth(),
                                maxLines = 2,
                                visualTransformation = if (showPassword) {
                                    VisualTransformation.None
                                } else {
                                    PasswordVisualTransformation()
                                },
                                trailingIcon = {
                                    if (showPassword) {
                                        IconButton(onClick = { showPassword = false }) {
                                            Icon(
                                                painter = painterResource(id = R.drawable.ic_visibility),
                                                contentDescription = "Show Password"
                                            )
                                        }
                                    } else {
                                        IconButton(onClick = { showPassword = true }) {
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
                            if (state.roomPasswordError != null) {
                                Text(
                                    text = state.roomPasswordError,
                                    color = MaterialTheme.colorScheme.error,
                                    modifier = Modifier
                                        .align(Alignment.End),
                                )
                            }
                            Spacer(modifier = Modifier.height(40.dp))
                             */
                            Button(
                                onClick = {
                                    viewModel.onEvent(RoomFormEvent.Submit)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.CenterHorizontally)
                            ) {
                                Text(text = "Start/Join Class")
                            }
                        }
                    }
                }
            }
        }
        return binding.root
    }

    private suspend fun moveToCall() {
        findNavController().popBackStack()
        (activity as HomeActivity).startMeeting(
            courseCode = viewModel.roomFormState.courseCode.prependIndent(preIndentedString)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}