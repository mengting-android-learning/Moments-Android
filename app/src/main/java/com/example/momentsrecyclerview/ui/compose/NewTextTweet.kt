package com.example.momentsrecyclerview.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NewTextTweet(
    onCancelClick: () -> Unit,
    onSendClick: (text: String) -> Unit,
    onSendClickNavigate: () -> Unit
) {
    var text by remember {
        mutableStateOf("")
    }
    var enabled by remember {
        mutableStateOf(false)
    }
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Cancel",
                modifier = Modifier
                    .wrapContentSize()
                    .clickable {
                        keyboardController?.hide()
                        onCancelClick()
                    }
            )
            Text(
                text = "Send Text",
            )
            Button(
                onClick = {
                    onSendClick(text)
                    onSendClickNavigate()
                },
                colors = buttonColors(
                    backgroundColor = if (enabled) Color.Green else Color.Gray,
                    contentColor = if (enabled) Color.White else Color.DarkGray
                ),
                enabled = enabled,
                modifier = Modifier
                    .wrapContentSize()
            ) {
                Text(text = "Send")
            }
        }
        TextField(
            value = text,
            onValueChange = {
                text = it
                enabled = it.isNotBlank()
            },
            Modifier
                .background(color = Color.White)
                .fillMaxSize()
                .padding(start = 15.dp, end = 15.dp)
                .focusRequester(focusRequester),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.White,
                textColor = Color.Black,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
    }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}
