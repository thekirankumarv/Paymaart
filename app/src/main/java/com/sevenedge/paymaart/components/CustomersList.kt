package com.sevenedge.paymaart.components

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.sevenedge.paymaart.api.CustomerViewModel
import com.sevenedge.paymaart.api.User
import com.sevenedge.paymaart.ui.theme.DeepKoamaru
import com.sevenedge.paymaart.ui.theme.Independence
import com.sevenedge.paymaart.ui.theme.MetallicSilver

@Composable
fun CustomerList(
    customers: List<User>,
    searchQuery: String,
    onLoadMore: () -> Unit,
    navController: NavController,
    viewModel: CustomerViewModel
) {
    val listState = rememberLazyListState()

    // Scroll progress tracking
    val isScrolling = listState.isScrollInProgress
    val progress = remember {
        derivedStateOf {
            if (listState.layoutInfo.totalItemsCount > 0) {
                listState.firstVisibleItemIndex.toFloat() / (listState.layoutInfo.totalItemsCount - 1)
            } else {
                0f
            }
        }
    }

    Column {
        // Header Text
        Text(
            text = "Members onboarded by email address",
            modifier = Modifier.padding(start = 16.dp, top = 18.dp),
            color = MetallicSilver,
            fontSize = 12.sp,
            fontWeight = FontWeight(400)
        )

        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 28.dp, top = 18.dp, end = 28.dp, bottom = 18.dp)
                    .background(Color.White)
            ) {
                items(customers) { customer ->
                    CustomerRow(
                        customer = customer,
                        onClick = {
                            viewModel.setSelectedCustomer(customer)
                            navController.navigate(
                                "kycDetails/${Uri.encode(customer.avatar ?: "")}/${Uri.encode(customer.firstName ?: "")}/${Uri.encode(customer.lastName ?: "")}/${Uri.encode(customer.email ?: "")}"
                            )
                        }
                    )
                }
                // Trigger load more when reaching the bottom
                if (listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index == customers.size - 1) {
                    onLoadMore()
                }
            }

            // Scroll indicator
            if (isScrolling) {
                Box(
                    modifier = Modifier
                        .width(5.dp)
                        .height(56.dp)
                        .align(Alignment.CenterEnd)
                        .offset(y = progress.value * 274.dp)
                        .background(Color(0xFFA79FBE))
                        .clip(RoundedCornerShape(topStart = 10.dp))
                )
            }
        }
    }
}

@Composable
fun CustomerContent(
    customers: List<User>,
    searchQuery: String,
    onLoadMore: () -> Unit,
    navController: NavController,
    viewModel: CustomerViewModel
) {
    val listState = rememberLazyListState()

    val filteredCustomers = if (searchQuery.isBlank()) {
        customers
    } else {
        customers.filter { customer ->
            customer.firstName.contains(searchQuery, ignoreCase = true) ||
                    customer.lastName.contains(searchQuery, ignoreCase = true) ||
                    customer.email.contains(searchQuery, ignoreCase = true)
        }
    }

    Column {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize()
        ) {
            items(filteredCustomers.size) { index ->
                if (index >= filteredCustomers.size - 1) {
                    onLoadMore()
                }
                CustomerRow(
                    customer = filteredCustomers[index],
                    onClick = {
                        viewModel.setSelectedCustomer(filteredCustomers[index])
                        navController.navigate(
                            "kycDetails/${Uri.encode(filteredCustomers[index].avatar)}/${Uri.encode(filteredCustomers[index].firstName)}/${Uri.encode(filteredCustomers[index].lastName)}/${Uri.encode(filteredCustomers[index].email)}"
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun CustomerRow(customer: User, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(45.5.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberImagePainter(data = customer.avatar),
            contentDescription = null,
            modifier = Modifier
                .size(45.dp)
                .clip(RoundedCornerShape(8.dp))
                .border(
                    width = 1.dp,
                    color = DeepKoamaru,
                    shape = RoundedCornerShape(8.dp)
                ),
            contentScale = ContentScale.Crop
        )

        Column(modifier = Modifier.padding(start = 14.dp)) {
            Text(
                text = "${customer.firstName} ${customer.lastName}",
                fontWeight = FontWeight(500),
                fontSize = 16.sp,
                color = Independence
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Email,
                    contentDescription = "Email Icon",
                    tint = MetallicSilver,
                    modifier = Modifier.size(19.dp)
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = customer.email,
                    fontSize = 14.sp,
                    fontWeight = FontWeight(400),
                    color = MetallicSilver
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(29.5.dp))
}

@Preview(showBackground = true)
@Composable
fun CustomerListPreview() {
    val customers = listOf(
        User(
            id = 1,
            firstName = "John",
            lastName = "Doe",
            email = "john.doe@example.com",
            avatar = "https://www.example.com/avatar1.jpg"
        ),
        User(
            id = 2,
            firstName = "Jane",
            lastName = "Smith",
            email = "jane.smith@example.com",
            avatar = "https://www.example.com/avatar2.jpg"
        )
    )

    CustomerList(
        customers = customers,
        searchQuery = "",
        onLoadMore = {},
        navController = rememberNavController(),
        viewModel = viewModel()
    )
}

@Preview(showBackground = true)
@Composable
fun CustomerRowPreview() {
    val customer = User(
        id = 1,
        firstName = "John",
        lastName = "Doe",
        email = "john.doe@example.com",
        avatar = "https://www.example.com/avatar1.jpg"
    )

    CustomerRow(customer = customer, onClick = {})
}

