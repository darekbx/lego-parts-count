package com.darekbx.legopartscount.ui.partslist

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.darekbx.legopartscount.repository.rebrickable.model.LegoPart
import com.darekbx.legopartscount.repository.rebrickable.model.LegoSet
import com.darekbx.legopartscount.repository.rebrickable.model.LegoSetPart
import com.darekbx.legopartscount.ui.LoadingView
import com.darekbx.legopartscount.viewmodel.RebrickableViewModel
import com.google.accompanist.coil.rememberCoilPainter

@Composable
fun PartsListScreen(
    setNumber: String,
    rebrickableViewModel: RebrickableViewModel,
    navigateUp: () -> Unit
) {
    Column(Modifier.padding(8.dp)) {
        SetPreview(setNumber, rebrickableViewModel)
        DisplayParts(setNumber, rebrickableViewModel)
    }

    SideEffect {
        rebrickableViewModel.setPartsSearchResult.value == null
    }
}

@Composable
fun SetPreview(setNumber: String, rebrickableViewModel: RebrickableViewModel) {
    val legoSet = rebrickableViewModel.fetchSet(setNumber).observeAsState()
    legoSet.value?.let { set ->
        DisplaySetPreview(set)
    } ?: run { LoadingView() }
}

@Preview
@Composable
private fun DisplaySetPreview(
    set: LegoSet = LegoSet(
        "42055-1",
        "Wheel Bucket Excavator",
        2016,
        3929,
        ""
    )
) {
    Row(Modifier.fillMaxWidth()) {
        Image(
            modifier = Modifier
                .width(100.dp)
                .height(100.dp)
                .weight(0.2F),
            painter = rememberCoilPainter(set.setImageUrl),
            contentDescription = set.name
        )
        Column(
            Modifier
                .weight(0.8F)
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Text(set.name, fontWeight = FontWeight.Bold)
            Text("${set.partsCount} parts")
            Text("${set.year} year")
        }
    }
}

@Composable
private fun DisplayParts(setNumber: String, rebrickableViewModel: RebrickableViewModel) {
    rebrickableViewModel.fetchSetParts(setNumber)
    rebrickableViewModel.setPartsSearchResult.observeAsState().value?.let { parts ->
        Column(
            Modifier
                .padding(top = 8.dp)
                .verticalScroll(
                    rememberScrollState(0),
                    true
                )
        ) {
            parts.forEach { legoSetPart ->
                DisplayPart(legoSetPart)
            }

            val sum = parts.sumBy { it.quantity }
            Text("SUM: ${sum}")
        }
    } ?: run { LoadingView() }
}

@Preview
@Composable
private fun DisplayPart(
    legoSetPart: LegoSetPart = LegoSetPart(
        LegoPart("3118", "Worm Gear", ""),
        15,
        5,
        "51324"
    )
) {
    val legoPart = legoSetPart.part
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
            Modifier
                .fillMaxWidth()
                .weight(0.8F)) {

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(legoPart.name, Modifier.width(150.dp))
                GrayText("Quantity: ${legoSetPart.quantity}")
            }

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    legoPart.partNumber,
                    color = Color.Gray,
                    fontSize = 11.sp
                )
                GrayText("In sets: ${legoSetPart.numSets}")
            }
        }
    }
}

@Composable
private fun GrayText(text: String) {
    Text(
        text,
        color = Color.Gray,
        fontSize = 11.sp
    )
}
