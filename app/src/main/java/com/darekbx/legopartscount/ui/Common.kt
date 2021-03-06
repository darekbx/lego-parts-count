package com.darekbx.legopartscount.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.darekbx.legopartscount.viewmodel.BaseViewModel
import com.darekbx.legopartscount.viewmodel.RebrickableViewModel

@Composable
fun LoadingView() {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .background(Color(0x99000000)),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun LoadingView(baseViewModel: BaseViewModel) {
    val loadingState = baseViewModel.loadingState.observeAsState(false)
    loadingState.value?.let { loadingVisible ->
        if (loadingVisible) {
            LoadingView()
        }
    }
}

@Composable
fun ErrorView(rebrickableViewModel: RebrickableViewModel) {
    val errorState = rebrickableViewModel.errorState.observeAsState()
    errorState.value?.let {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .clickable {
                    rebrickableViewModel.clearError()
                }
                .background(Color(0x99000000)),
            contentAlignment = Alignment.Center
        ) {
            Text("${it.message}", color = purple700)
        }
    }
}
