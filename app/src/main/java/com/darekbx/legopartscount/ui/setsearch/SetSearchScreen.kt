package com.darekbx.legopartscount.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import com.darekbx.legopartscount.viewmodel.RebrickableViewModel

@Composable
fun SetSearchScreen(
    rebrickableViewModel: RebrickableViewModel,
    openPartsList: (String) -> Unit,
    openDefinedParts: () -> Unit,
    openViewDefinedParts: () -> Unit,
    openAllTechnicSetsSearch: () -> Unit
) {
    Box {
        SearchView(
            rebrickableViewModel,
            openDefinedParts,
            openViewDefinedParts,
            openAllTechnicSetsSearch
        )
        SearchResultView(rebrickableViewModel, openPartsList)

        LoadingView(rebrickableViewModel)
        ErrorView(rebrickableViewModel)
    }
}

@Composable
fun SearchView(
    rebrickableViewModel: RebrickableViewModel,
    openDefinedParts: () -> Unit,
    openViewDefinedParts: () -> Unit,
    openAllTechnicSetsSearch: () -> Unit
) {
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
            "Count defined parts in Lego sets",
            style = MaterialTheme.typography.h5,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 32.dp)
        )
        Column(Modifier.padding(start = 16.dp, end = 16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .height(56.dp)
                    .fillMaxWidth()
            ) {
                TextField(
                    modifier = Modifier.weight(0.8f),
                    value = searchValue.value,
                    label = { Text(text = "Set number", color = MaterialTheme.colors.onSurface) },
                    onValueChange = { searchValue.value = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Button(
                    modifier = Modifier
                        .weight(0.2f)
                        .padding(start = 4.dp)
                        .fillMaxHeight(),
                    onClick = { rebrickableViewModel.searchForSet(searchValue.value.text) }) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        "Search"
                    )
                }
            }
            Button(modifier = Modifier
                .padding(top = 2.dp)
                .fillMaxWidth(), onClick = { openAllTechnicSetsSearch() }) {
                Text(text = "Search for all Technic sets")
            }
        }

        Row {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.5F, true)
                    .padding(end = 2.dp),
                onClick = { openViewDefinedParts() }) {
                Text("View Defined parts")
            }
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.5F, true)
                    .padding(start = 2.dp),
                onClick = { openDefinedParts() }) {
                Text("Define parts group")
            }
        }
    }
}

@Composable
fun SearchResultView(rebrickableViewModel: RebrickableViewModel, openPartsList: (String) -> Unit) {
    val setSearchResult = rebrickableViewModel.setSearchResult.observeAsState()
    setSearchResult.value?.let { legoSet ->
        rebrickableViewModel.setSearchResult.value = null
        openPartsList(legoSet!!.setNumber)
    }
}
