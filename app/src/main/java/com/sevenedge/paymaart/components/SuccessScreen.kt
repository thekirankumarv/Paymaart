package com.sevenedge.paymaart.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sevenedge.paymaart.ui.theme.CaribbeanGreen
import com.sevenedge.paymaart.ui.theme.CaribbeanGreen02
import com.sevenedge.paymaart.ui.theme.PaymaartTheme

@Composable
fun SuccessScreen() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(56.dp, 64.dp, 56.dp),
                color = CaribbeanGreen02,
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    text = "Login successful",
                    modifier = Modifier.padding(16.dp),
                    color = CaribbeanGreen,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SuccessScreenPreview() {
    PaymaartTheme {
        SuccessScreen()
    }
}