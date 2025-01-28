package com.sevenedge.paymaart.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.sevenedge.paymaart.R

@Composable
fun BackgroundDesign(modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Image(
            painter = painterResource(id = R.drawable.one),
            contentDescription = "Ellipse One",
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.TopCenter)
                .zIndex(0.5f)
                .offset(y = (-305).dp)
        )
        Image(
            painter = painterResource(id = R.drawable.two),
            contentDescription = "Ellipse Two",
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.TopCenter)
                .zIndex(0.4f)
                .offset(y = (-305).dp)
        )
        Image(
            painter = painterResource(id = R.drawable.three),
            contentDescription = "Ellipse Three",
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.TopCenter)
                .zIndex(0.3f)
                .offset(y = (-300).dp)
        )
        Image(
            painter = painterResource(id = R.drawable.four),
            contentDescription = "Ellipse Four",
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.TopCenter)
                .zIndex(0.2f)
                .offset(y = (-300).dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BackgroundDesignPreview() {
    BackgroundDesign()
}
