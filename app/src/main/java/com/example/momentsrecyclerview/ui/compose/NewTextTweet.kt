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
import androidx.compose.ui.res.dimensionResource
import com.example.momentsrecyclerview.R
import com.example.momentsrecyclerview.util.CANCEL_TEXT
import com.example.momentsrecyclerview.util.POST_TEXT

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NewTextTweet(
    content: String,
    setLocalContent: (String) -> Unit,
    saveNewTweet: () -> Unit,
    navigateBack: () -> Unit,
) {
    var enabled by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = dimensionResource(id = R.dimen.small_margin_end),
                end = dimensionResource(id = R.dimen.small_margin_end)
            ),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = dimensionResource(id = R.dimen.small_margin_end),
                    end = dimensionResource(id = R.dimen.small_margin_end)
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = CANCEL_TEXT,
                modifier = Modifier
                    .wrapContentSize()
                    .clickable {
                        setLocalContent("")
                        keyboardController?.hide()
                        navigateBack()
                    }
            )
            Text(
                text = "Text",
            )
            Button(
                onClick = {
                    saveNewTweet()
                    navigateBack()
                },
                colors = buttonColors(
                    backgroundColor = if (enabled) Color.Green else Color.Gray,
                    contentColor = if (enabled) Color.White else Color.DarkGray
                ),
                enabled = enabled,
                modifier = Modifier
                    .wrapContentSize()
            ) {
                Text(text = POST_TEXT)
            }
        }
        TextField(
            value = content,
            onValueChange = {
                setLocalContent(it)
                enabled = it.isNotBlank()
            },
            Modifier
                .background(color = Color.White)
                .fillMaxSize()
                .padding(
                    start = dimensionResource(id = R.dimen.new_tweet_padding),
                    end = dimensionResource(id = R.dimen.new_tweet_padding)
                )
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
