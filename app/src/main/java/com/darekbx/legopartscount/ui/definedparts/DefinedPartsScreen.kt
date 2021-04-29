package com.darekbx.legopartscount.ui.definedparts

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.coil.rememberCoilPainter
import com.darekbx.legopartscount.ui.ErrorView
import com.darekbx.legopartscount.ui.LoadingView
import com.darekbx.legopartscount.viewmodel.MainViewModel

@Composable
fun DefinedPartsScreen(
    mainViewModel: MainViewModel,
    navigateUp: () -> Unit
) {
    val selectedItems = mutableListOf<String>()

    Box {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(Modifier.weight(0.1F, false)) {
                PartSearchView(mainViewModel)
            }
            Box(Modifier.weight(1F, true)) {
                PartsList(mainViewModel) { partNumber, isSelected ->
                    if (isSelected) {
                        selectedItems.add(partNumber)
                    } else {
                        selectedItems.remove(partNumber)
                    }
                }
            }
            Box(Modifier.weight(0.1F, false)) {
                AddButton {
                    mainViewModel.addDefinedParts(selectedItems)
                    navigateUp()
                }
            }
        }

        LoadingView(mainViewModel)
        ErrorView(mainViewModel)
    }

    SideEffect {
        // Reset value
        mainViewModel.partSearchResult.value = null
    }
}

@Composable
fun PartSearchView(mainViewModel: MainViewModel) {
    val searchValue = remember { mutableStateOf(TextFieldValue()) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .height(56.dp)
            .fillMaxWidth()
    ) {
        TextField(
            modifier = Modifier.weight(1.0F, true),
            value = searchValue.value,
            label = { Text("Part number or name") },
            onValueChange = { searchValue.value = it }
        )
        Button(
            modifier = Modifier
                .padding(start = 4.dp)
                .fillMaxHeight(),
            onClick = { mainViewModel.searchForPart(searchValue.value.text) }) {
            Icon(
                imageVector = Icons.Filled.Search,
                "Search"
            )
        }
    }
}

@Composable
fun PartsList(
    mainViewModel: MainViewModel,
    onItemSelect: (partNum: String, isChecked: Boolean) -> Unit
) {
    val partsList = mainViewModel.partSearchResult.observeAsState()
    partsList.value?.let {
        Column(
            Modifier
                .padding(top = 8.dp)
                .verticalScroll(
                    rememberScrollState(0),
                    true
                )
        ) {
            it.forEach { legoPart ->
                val isChecked = remember { mutableStateOf(false) }
                Row(
                    modifier = Modifier.padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Image(
                        modifier = Modifier
                            .width(30.dp)
                            .height(30.dp),
                        painter = rememberCoilPainter(legoPart.partImageUrl),
                        contentDescription = legoPart.name
                    )
                    Column(
                        modifier = Modifier
                            .padding(start = 8.dp, end = 8.dp)
                            .weight(1.0f, true)
                    ) {
                        Text(legoPart.name)
                        Text(
                            legoPart.partNumber,
                            color = Color.Gray,
                            fontSize = 11.sp
                        )
                    }
                    Checkbox(
                        modifier = Modifier.weight(0.1f, true),
                        checked = isChecked.value,
                        onCheckedChange = {
                            isChecked.value = it
                            onItemSelect(legoPart.partNumber, it)
                        })
                }
                Divider(color = Color.LightGray, thickness = 1.dp)
            }
        }
    } ?: run {
        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Empty list")
        }
    }
}

@Composable
fun AddButton(onClick: () -> Unit) {
    Button(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Text("Add selected")
    }
}
