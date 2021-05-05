package com.darekbx.legopartscount.ui.viewdefinedparts

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.darekbx.legopartscount.model.DefinedPart
import com.darekbx.legopartscount.viewmodel.DefinedPartsViewModel
import com.google.accompanist.coil.rememberCoilPainter

@Composable
fun ViewDefinedPartsScreen(
    definedPartsViewModel: DefinedPartsViewModel,
) {
    PartsList(definedPartsViewModel)
}

@Composable
fun PartsList(
    definedPartsViewModel: DefinedPartsViewModel
) {
    val deleteDialog = remember { mutableStateOf(false) }
    val definedPartToDelete = remember { mutableStateOf<DefinedPart?>(null) }
    val partsList = definedPartsViewModel.listDefinedParts().observeAsState()
    partsList.value?.let {
        Column(
            Modifier
                .padding(top = 8.dp)
                .verticalScroll(
                    rememberScrollState(0),
                    true
                )
        ) {
            it.forEach { definedPart ->
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            definedPartToDelete.value = definedPart
                            deleteDialog.value = true
                        },
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Image(
                        modifier = Modifier
                            .width(30.dp)
                            .height(30.dp),
                        painter = rememberCoilPainter(definedPart.imageUrl),
                        contentDescription = definedPart.name
                    )
                    Column(
                        modifier = Modifier
                            .padding(start = 8.dp, end = 8.dp)
                            .weight(1.0f, true)
                    ) {
                        Text(definedPart.name)
                        Text(
                            "${definedPart.number}",
                            color = Color.Gray,
                            fontSize = 11.sp
                        )
                    }
                }
                Divider(color = Color.LightGray, thickness = 1.dp)
            }
        }

        if (deleteDialog.value && definedPartToDelete.value != null) {
            showDeleteDialog(deleteDialog, definedPartToDelete.value!!, definedPartsViewModel)
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
fun showDeleteDialog(
    deleteDialog: MutableState<Boolean>,
    definedPart: DefinedPart,
    definedPartsViewModel: DefinedPartsViewModel
) {
    AlertDialog(
        onDismissRequest = { deleteDialog.value = false },
        title = { Text("Delete defined part?") },
        text = { Text("Part to delete: ${definedPart.name}") },
        confirmButton = {
            Button(onClick = {
                definedPartsViewModel.deletePart(definedPart)
                deleteDialog.value = false
            }) {
                Text("Delete")
            }
        },
        dismissButton = {
            Button(onClick = { deleteDialog.value = false }) {
                Text("Cancel")
            }
        }
    )
}
