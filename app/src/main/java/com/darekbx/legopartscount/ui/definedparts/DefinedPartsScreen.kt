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
import com.darekbx.legopartscount.model.DefinedPart
import com.darekbx.legopartscount.repository.rebrickable.model.LegoPart
import com.google.accompanist.coil.rememberCoilPainter
import com.darekbx.legopartscount.ui.ErrorView
import com.darekbx.legopartscount.ui.LoadingView
import com.darekbx.legopartscount.viewmodel.DefinedPartsViewModel
import com.darekbx.legopartscount.viewmodel.RebrickableViewModel

@Composable
fun DefinedPartsScreen(
    rebrickableViewModel: RebrickableViewModel,
    definedPartsViewModel: DefinedPartsViewModel,
    navigateUp: () -> Unit
) {
    val selectedItems = mutableListOf<DefinedPart>()

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
                PartSearchView(rebrickableViewModel)
            }
            Box(Modifier.weight(1F, true)) {
                PartsList(rebrickableViewModel) { partNumber, isSelected ->
                    if (isSelected) {
                        selectedItems.add(partNumber)
                    } else {
                        selectedItems.remove(partNumber)
                    }
                }
            }
            Box(Modifier.weight(0.1F, false)) {
                AddButton {
                    definedPartsViewModel.addDefinedParts(selectedItems)
                    navigateUp()
                }
            }
        }

        LoadingView(rebrickableViewModel)
        ErrorView(rebrickableViewModel)
    }

    SideEffect {
        // Reset value
        rebrickableViewModel.partSearchResult.value = null
    }
}

@Composable
fun PartSearchView(rebrickableViewModel: RebrickableViewModel) {
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
            onClick = { rebrickableViewModel.searchForPart(searchValue.value.text) }) {
            Icon(
                imageVector = Icons.Filled.Search,
                "Search"
            )
        }
    }
}

@Composable
fun PartsList(
    rebrickableViewModel: RebrickableViewModel,
    onItemSelect: (DefinedPart, isChecked: Boolean) -> Unit
) {
    val partsList = rebrickableViewModel.partSearchResult.observeAsState()
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
                DisplayItem(legoPart, isChecked, onItemSelect)
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
private fun DisplayItem(
    legoPart: LegoPart,
    isChecked: MutableState<Boolean>,
    onItemSelect: (DefinedPart, isChecked: Boolean) -> Unit
) {
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

        val definedPart = DefinedPart(
            legoPart.partNumber.toInt(),
            legoPart.partImageUrl,
            legoPart.name
        )
        Checkbox(
            modifier = Modifier.weight(0.1f, true),
            checked = isChecked.value,
            onCheckedChange = {
                isChecked.value = it
                onItemSelect(definedPart, it)
            })
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
