package com.darekbx.legopartscount.ui.partslist

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
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
import com.darekbx.legopartscount.ui.ErrorView
import com.darekbx.legopartscount.ui.LoadingView
import com.darekbx.legopartscount.viewmodel.RebrickableViewModel
import com.google.accompanist.coil.rememberCoilPainter

@Composable
fun PartsListScreen(
    setNumber: String,
    rebrickableViewModel: RebrickableViewModel
) {
    Box {
        Column {
            SetPreview(setNumber, rebrickableViewModel) { legoSet ->
                Divider(color = Color.LightGray, thickness = 1.dp)
                DisplayParts(setNumber, legoSet.partsCount, rebrickableViewModel)
            }
        }
        LoadingView(rebrickableViewModel)
        ErrorView(rebrickableViewModel)
    }

    rebrickableViewModel.fetchSetParts(setNumber)
}

@Composable
fun SetPreview(
    setNumber: String,
    rebrickableViewModel: RebrickableViewModel,
    loadedCallback: @Composable (LegoSet) -> Unit
) {
    val legoSet = rebrickableViewModel.fetchSet(setNumber).observeAsState()
    legoSet.value?.let { set ->
        DisplaySetPreview(set)
        loadedCallback(set)
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
    Row(
        Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Image(
            modifier = Modifier
                .width(100.dp)
                .height(100.dp)
                .weight(0.25F),
            painter = rememberCoilPainter(set.setImageUrl),
            contentDescription = set.name
        )
        Column(
            Modifier
                .weight(0.75F)
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
private fun DisplayParts(
    setNumber: String,
    partsCount: Int,
    rebrickableViewModel: RebrickableViewModel
) {
    val legoSetParts = rebrickableViewModel.fetchSetParts(setNumber).observeAsState()

    Column(
        Modifier
            .padding(8.dp)
            .verticalScroll(
                rememberScrollState(0),
                true
            )
    ) {

        legoSetParts.value?.let { parts ->
            parts.forEach { legoSetPart ->
                DisplayPart(legoSetPart)
                Divider(color = Color.LightGray, thickness = 1.dp)
            }

            val definedSum = parts.sumBy { it.quantity }
            Text(
                "Items count: $definedSum",
                Modifier.padding(top = 8.dp, start = 8.dp, end = 8.dp),
                fontWeight = FontWeight.Bold
            )
            Text(
                "Defined parts to parts count: %.2f".format(definedSum / partsCount.toFloat()),
                Modifier.padding(bottom = 8.dp, start = 8.dp, end = 8.dp),
                fontWeight = FontWeight.Bold
            )
        }
    }
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
                .padding(start = 8.dp)
                .weight(0.8F)
        ) {

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    legoPart.name,
                    Modifier.weight(0.8F),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Row(
                    Modifier.weight(0.2F),
                    horizontalArrangement = Arrangement.End
                ) {
                    GrayText("Quantity: ")
                    Text(
                        "${legoSetPart.quantity}",
                        color = Color.Gray,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    legoPart.partNumber,
                    color = Color.Gray,
                    fontSize = 11.sp,
                    modifier = Modifier.weight(0.8F)
                )
                Row(
                    Modifier.weight(0.2F),
                    horizontalArrangement = Arrangement.End
                ) {
                    GrayText("In sets: ")
                    Text(
                        "${legoSetPart.numSets}",
                        color = Color.Gray,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
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
