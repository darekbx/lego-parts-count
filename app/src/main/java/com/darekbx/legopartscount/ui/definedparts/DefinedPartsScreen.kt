package com.darekbx.legopartscount.ui.definedparts

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.darekbx.legopartscount.ui.ErrorView
import com.darekbx.legopartscount.ui.LoadingView
import com.darekbx.legopartscount.viewmodel.MainViewModel

@Composable
fun DefinedPartsScreen(
    mainViewModel: MainViewModel,
    navigateUp: () -> Unit
) {
    Box(Modifier.padding(top = 8.dp, bottom = 8.dp)) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            PartSearchView(mainViewModel)
            PartsList(mainViewModel)
        }

        LoadingView(mainViewModel)
        ErrorView(mainViewModel)
    }
}

@Composable
fun PartSearchView(mainViewModel: MainViewModel) {
    val searchValue = remember { mutableStateOf(TextFieldValue()) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.height(56.dp)
    ) {
        TextField(
            value = searchValue.value,
            label = { Text(text = "Part number or name", color = MaterialTheme.colors.onSurface) },
            onValueChange = { searchValue.value = it }
        )
        Button(
            modifier = Modifier
                .padding(start = 4.dp)
                .fillMaxHeight(),
            onClick = { mainViewModel.searchForPart(searchValue.value.text) }) {
            Icon(
                imageVector = Icons.Filled.Search,
                "Search",
                tint = Color.White
            )
        }
    }
}

@Composable
fun PartsList(mainViewModel: MainViewModel) {
    val partsList = mainViewModel.partSearchResult.observeAsState()
    partsList.value?.let { partsList ->
        Column {
            partsList.forEach {
                Row(modifier = Modifier.padding(8.dp)) {
                    Text(it.name, modifier = Modifier.padding(end = 8.dp))
                    Text(it.partNumber)
                }
            }
        }
    }
}
