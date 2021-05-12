package com.darekbx.legopartscount.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val purple200 = Color(0xFF222222)
val purple700 = Color(0xFF000000)
val teal200 = Color(0xFF03DAC5)
val white = Color(0xFFFFFFFF)

val shapes = Shapes(
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(4.dp),
    large = RoundedCornerShape(0.dp)
)

val typography = Typography(
    body1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )
)

@Composable
fun LegoPartsCountTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    MaterialTheme(
        colors = lightColors(
            surface = Color.White,
            background = Color.White,
            primary = purple200,
            primaryVariant = purple700,
            secondary = teal200,
        ),
        typography = typography,
        shapes = shapes,
        content = content
    )
}

