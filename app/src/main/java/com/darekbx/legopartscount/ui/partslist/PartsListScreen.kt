package com.darekbx.legopartscount.ui

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.darekbx.legopartscount.viewmodel.MainViewModel

@Composable
fun PartsListScreen(
    setId: String,
    mainViewModel: MainViewModel,
    navigateUp: () -> Unit
) {
    Button(onClick = { navigateUp() }) {
        Text(text = "In Set id: $setId, press to go back")
    }
}