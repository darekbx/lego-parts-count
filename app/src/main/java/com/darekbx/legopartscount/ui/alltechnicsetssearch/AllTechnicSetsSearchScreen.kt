package com.darekbx.legopartscount.ui.alltechnicsetssearch

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.darekbx.legopartscount.repository.rebrickable.model.LegoSet
import com.darekbx.legopartscount.ui.LoadingView
import com.darekbx.legopartscount.viewmodel.AllSetsViewModel
import com.darekbx.legopartscount.viewmodel.LegoSetDefinedParts
import com.google.accompanist.coil.rememberCoilPainter

@Composable
fun AllTechnicSetsSearchScreen(
    allSetsViewModel: AllSetsViewModel
) {
    Box {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
        ) {
            Row(modifier = Modifier.weight(0.01F)) {
                DisplayProgress(allSetsViewModel)
            }
            Row(modifier = Modifier.weight(0.99F)) {
                DisplaySets(allSetsViewModel)
            }
        }
        LoadingView(allSetsViewModel)
        AddedSets(allSetsViewModel)
    }

    allSetsViewModel.loadAllSets(2021, 400)
}

@Composable
private fun AddedSets(allSetsViewModel: AllSetsViewModel) {
    val setsAdded = allSetsViewModel.setsAdded.observeAsState(false)
    setsAdded.value?.let { addedSets ->
        if (addedSets != null && addedSets != false) {
            Toast.makeText(
                LocalContext.current,
                "Added $addedSets new sets",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}

@Composable
fun DisplayProgress(allSetsViewModel: AllSetsViewModel) {
    allSetsViewModel.progress.observeAsState().value?.let {
        val percent = it.progress / it.max.toFloat()
        LinearProgressIndicator(
            modifier = Modifier.fillMaxWidth(),
            progress = percent,
            backgroundColor = Color.White,
            color = Color.Blue
        )
    }
}

@Composable
fun DisplaySets(allSetsViewModel: AllSetsViewModel) {
    val items = remember { mutableListOf<LegoSetDefinedParts>() }

    val data: LegoSetDefinedParts by allSetsViewModel.result.observeAsState(
        LegoSetDefinedParts(
            LegoSet("", "", 0, 0, ""), 0
        )
    )

    if (data.legoSet.name.isEmpty()) {
        return
    }

    items.add(data)

    Column(
        Modifier
            .padding(8.dp)
            .verticalScroll(
                rememberScrollState(0),
                true
            )
    ) {
        for (item in items.sortedByDescending { it.definedPartsCount }) {
            // Draw row
            DisplayRow(item)
            Divider(color = Color.LightGray, thickness = 1.dp)
        }
    }
}

@Composable
private fun DisplayRow(item: LegoSetDefinedParts) {
    Row {
        Image(
            modifier = Modifier
                .width(60.dp)
                .height(60.dp),
            painter = rememberCoilPainter(item.legoSet.setImageUrl),
            contentDescription = item.legoSet.name
        )
        Column(
            modifier = Modifier
                .padding(start = 8.dp, end = 8.dp)
                .weight(1.0f, true)
        ) {
            Text("${item.legoSet.name} (${item.legoSet.year})")
            Text("Number: ${item.legoSet.setNumber}")
            Text(
                "${item.definedPartsCount} / ${item.legoSet.partsCount} (%.2f)".format(item.ratio),
                color = Color.Gray,
                fontSize = 11.sp
            )
        }
    }
}
