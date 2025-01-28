package com.sevenedge.paymaart

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.sevenedge.paymaart.components.BackgroundDesign
import com.sevenedge.paymaart.ui.theme.Border
import com.sevenedge.paymaart.ui.theme.DeepKoamaru
import com.sevenedge.paymaart.ui.theme.Independence
import com.sevenedge.paymaart.ui.theme.Magnolia
import com.sevenedge.paymaart.ui.theme.MetallicSilver
import com.sevenedge.paymaart.ui.theme.PaymaartTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KycDetails(
    avatar: String,
    firstName: String,
    lastName: String,
    email: String,
    navController: NavController
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // BackgroundDesign component
        BackgroundDesign()

        // TopAppBar and Content
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TopAppBar(
                title = {
                    Text(
                        text = "KYC Details",
                        fontSize = 16.sp,
                        fontWeight = FontWeight(700),
                        modifier = Modifier
                            .padding(start = 16.dp, top = 17.dp)
                            .fillMaxWidth()
                    )
                },
                navigationIcon = {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(start = 16.dp, top = 16.dp, bottom = 16.dp)
                    ) {
                        IconButton(
                            onClick = {
                                navController.navigateUp()
                            },
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowLeft,
                                contentDescription = "Back",
                                tint = Independence,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Independence
                ),
                modifier = Modifier.height(56.dp)
            )

            // User Profile Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Profile Image
                Image(
                    painter = rememberImagePainter(data = avatar),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(25.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Name
                Text(
                    text = "$firstName $lastName",
                    fontSize = 14.sp,
                    fontWeight = FontWeight(500),
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Email,
                        contentDescription = "Email Icon",
                        tint = Independence,
                        modifier = Modifier.size(16.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = email,
                        fontSize = 12.sp,
                        fontWeight = FontWeight(400),
                        color = Color.Black
                    )
                }
            }

            // KYC Status Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 82.dp, start = 16.dp, end = 16.dp),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .border(
                            width = 1.dp,
                            shape = RoundedCornerShape(8.dp),
                            color = Magnolia
                        )
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "KYC Registration",
                        fontSize = 16.sp,
                        fontWeight = FontWeight(600),
                        color = Independence
                    )
                    Text(
                        text = "Not Started",
                        fontSize = 13.sp,
                        fontWeight = FontWeight(600),
                        color = Independence,
                        modifier = Modifier
                            .background(
                                color = Border,
                                shape = RoundedCornerShape(4.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }
            }


            // Online KYC Registration Section
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 13.dp, start = 16.dp, end = 16.dp, bottom = 13.dp)
                        .border(
                            width = 1.dp,
                            shape = RoundedCornerShape(8.dp),
                            color = Magnolia
                        ),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ){
                    Text(
                        text = "Online KYC Registration",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Independence,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(top = 130.dp)
                            .align(Alignment.CenterHorizontally),

                    )
                    Text(
                        text = "Complete KYC registration now for full access to Paymaart services",
                        fontSize = 14.sp,
                        color = MetallicSilver,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 6.dp, bottom = 130.dp, start = 31.dp, end =  31.dp)
                    )
                }


                Button(
                    onClick = { /*KYC*/ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(58.dp)
                        .padding(start = 16.dp, end = 16.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = DeepKoamaru)
                ) {
                    Text(
                        text = "Complete KYC Registration",
                        fontSize = 14.sp,
                        fontWeight = FontWeight(400),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun KycDetailsPreview() {
    PaymaartTheme {
        val mockNavController = rememberNavController()
        KycDetails(
            avatar = "https://via.placeholder.com/150",
            firstName = "John",
            lastName = "Doe",
            email = "johndoe@example.com",
            navController = mockNavController
        )
    }
}
