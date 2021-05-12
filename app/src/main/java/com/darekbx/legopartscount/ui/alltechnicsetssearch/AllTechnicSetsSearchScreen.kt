package com.darekbx.legopartscount.ui.alltechnicsetssearch

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.darekbx.legopartscount.R
import com.darekbx.legopartscount.viewmodel.AllSetsViewModel
import com.darekbx.legopartscount.viewmodel.LegoSetDefinedParts
import com.google.accompanist.coil.rememberCoilPainter

/**
 * https://brickset.com/api/v3.asmx/getSets
 *
 * login at first!
 *
 * filter by:
 *  year >= 2003
 *  pieces > 500
 *
 */

@Composable
fun AllTechnicSetsSearchScreen(
    allSetsViewModel: AllSetsViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
    ) {
        Row(modifier = Modifier.weight(0.1F)) {
            DisplayProgress(allSetsViewModel)
        }
        Row(modifier = Modifier.weight(0.9F)) {
            DisplaySets(allSetsViewModel)
        }
    }
}

@Composable
fun DisplayProgress(allSetsViewModel: AllSetsViewModel) {
    allSetsViewModel.progress.observeAsState().value?.let {
        val percent = it.progress * 100.0F / it.max
        LinearProgressIndicator(progress = percent)
    }
}

@Composable
fun DisplaySets(allSetsViewModel: AllSetsViewModel) {
    val items = remember { mutableListOf<LegoSetDefinedParts>() }

    allSetsViewModel.loadAllSets(2013, 500).observeAsState().value?.let { data ->
        items.add(data)

        Column(
            Modifier
                .padding(top = 8.dp)
                .verticalScroll(
                    rememberScrollState(0),
                    true
                )
        ) {
        for (item in items) {

                // Draw row
            DisplayRow(item)

            }
        }
    }
}

@Composable
private fun DisplayRow(item: LegoSetDefinedParts) {
    Row {
        Image(
            modifier = Modifier
                .width(30.dp)
                .height(30.dp),
            painter = rememberCoilPainter(item.legoSet.setImageUrl),
            contentDescription = item.legoSet.name
        )
        Column(
            modifier = Modifier
                .padding(start = 8.dp, end = 8.dp)
                .weight(1.0f, true)
        ) {
            Text(item.legoSet.name)
            Text(
                "${item.definedPartsCount} / ${item.legoSet.partsCount}",
                color = Color.Gray,
                fontSize = 11.sp
            )
        }
    }
}
