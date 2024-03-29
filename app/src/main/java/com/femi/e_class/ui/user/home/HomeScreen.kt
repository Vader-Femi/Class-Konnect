package com.femi.e_class.ui.user.home

import android.app.Activity
import android.content.Context
import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.femi.e_class.R
import com.femi.e_class.ui.user.UserActivity
import com.femi.e_class.viewmodels.UserViewModel
import com.femi.e_class.presentation.user.home.RoomFormEvent
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: UserViewModel) {
    val scrollState = rememberScrollState()
    val state by viewModel.roomFormState.collectAsStateWithLifecycle()
    var userNameText by remember { mutableStateOf("") }
    var greetingText by remember { mutableStateOf("") }
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        userNameText = "Hi, ${viewModel.userFName()}"
        greetingText = "Good Day"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val calendar = Calendar.getInstance()
            greetingText = when (calendar.get(Calendar.HOUR_OF_DAY)) {
                in 6..11 -> "Good Morning"
                in 12..16 -> "Good Afternoon"
                in 17..22 -> "Good Evening"
                else -> "You should be sleeping"
            }
        }


    }

    LaunchedEffect(key1 = context) {
        viewModel.roomEvents.collect { event ->
            when (event) {
                is UserViewModel.RoomEvent.Success -> {
                    context.moveToCall(viewModel)
                }
            }
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .verticalScroll(scrollState)
            .fillMaxSize()
            .padding(0.dp, 0.dp, 0.dp, 30.dp)
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.Start)
                .padding(10.dp, 0.dp, 10.dp, 0.dp),
            text = userNameText,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 20.sp,
            textAlign = TextAlign.Start
        )
        Spacer(modifier = Modifier.height(10.dp))
        TopCard(
            painter = painterResource(id = R.drawable.cover_image),
            contentDescription = "Inspirational Text card",
            text1 = "Let’s Learn\n" +
                    "Now!",
            text2 = "Start your classes on the go.",
            onClick = {

            }
        )
        Text(
            modifier = Modifier
                .align(Alignment.Start)
                .padding(10.dp, 0.dp, 10.dp, 0.dp),
            text = greetingText,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            textAlign = TextAlign.Start
        )
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(
            value = state.courseCode,
            label = { Text(text = "Course Code") },
            onValueChange = { newText ->
                if (newText.length > 6)
                    return@OutlinedTextField

                val input = newText
                    .replace(" ", "")
                    .uppercase()
                viewModel.onEvent(RoomFormEvent.CourseCodeChanged(input))
            },
            isError = state.courseCodeError != null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp, 0.dp, 20.dp, 0.dp),
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
                        text = "LASU"
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
                text = state.courseCodeError!!,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(20.dp, 0.dp, 20.dp, 0.dp),
            )
        }
        Spacer(modifier = Modifier.height(40.dp))
        Button(
            onClick = {
                viewModel.onEvent(RoomFormEvent.Submit)
            },
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
                .padding(20.dp, 0.dp, 20.dp, 0.dp)
        ) {
            Text(text = "Start/Join Class")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopCard(
    painter: Painter,
    contentDescription: String,
    text1: String,
    text2: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 12.dp, end = 12.dp),
        shape = AbsoluteRoundedCornerShape(24.dp),
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 30.dp),
    ) {
        Row(modifier = Modifier
            .height(200.dp)
            .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.53f)
                    .padding(start = 22.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text =
                    buildAnnotatedString {
                        append(text1)
                        withStyle(
                            style = SpanStyle(
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                            )
                        ) {
                            append("\n$text2")
                        }
                    },
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Start
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(1f),
                contentAlignment = Alignment.CenterEnd
            ) {
                Image(
                    painter = painter,
                    contentDescription = contentDescription,
                    contentScale = ContentScale.FillWidth
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(30.dp))
}

private suspend fun Context.moveToCall(viewModel: UserViewModel) {
    val activity = this as Activity
    (activity as UserActivity).startMeeting(
        viewModel = viewModel,
        courseCode = viewModel.roomFormState.value.courseCode.prependIndent("LASU")
    )
    finish()
}
