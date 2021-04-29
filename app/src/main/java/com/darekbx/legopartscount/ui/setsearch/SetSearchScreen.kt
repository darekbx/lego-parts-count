package com.darekbx.legopartscount.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.text.style.TextAlign
import com.darekbx.legopartscount.viewmodel.MainViewModel

@Composable
fun SetSearchScreen(
    mainViewModel: MainViewModel,
    openPartsList: (String) -> Unit,
    openDefinedParts: () -> Unit
) {
    Box {
        SearchView(mainViewModel, openDefinedParts)
        SearchResultView(mainViewModel, openPartsList)

        LoadingView(mainViewModel)
        ErrorView(mainViewModel)
    }
}

@Composable
fun SearchView(mainViewModel: MainViewModel, openDefinedParts: () -> Unit) {
    val searchValue = remember { mutableStateOf(TextFieldValue()) }

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(8.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            "Lego parts count in sets",
            style = MaterialTheme.typography.h5,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 32.dp)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.height(56.dp)
        ) {
            TextField(
                value = searchValue.value,
                label = { Text(text = "Set number", color = MaterialTheme.colors.onSurface) },
                onValueChange = { searchValue.value = it },
            )
            Button(
                modifier = Modifier
                    .padding(start = 4.dp)
                    .fillMaxHeight(),
                onClick = { mainViewModel.searchForSet(searchValue.value.text) }) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    "Search"
                )
            }
        }
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { openDefinedParts() }) {
            Text("Define parts group")
        }
    }
}

@Composable
fun SearchResultView(mainViewModel: MainViewModel, openPartsList: (String) -> Unit) {
    val setSearchResult = mainViewModel.setSearchResult.observeAsState()
    setSearchResult.value?.let { legoSet ->
        openPartsList(legoSet!!.setNumber)
    }
}
