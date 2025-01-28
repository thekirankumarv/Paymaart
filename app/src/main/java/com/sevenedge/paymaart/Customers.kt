package com.sevenedge.paymaart

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.sevenedge.paymaart.api.CustomerViewModel
import com.sevenedge.paymaart.components.CustomerList
import com.sevenedge.paymaart.ui.theme.DeepKoamaru
import com.sevenedge.paymaart.ui.theme.Independence
import com.sevenedge.paymaart.ui.theme.MetallicSilver
import com.sevenedge.paymaart.ui.theme.PastelPurple
import com.sevenedge.paymaart.ui.theme.PaymaartTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sevenedge.paymaart.api.User
import com.sevenedge.paymaart.components.CustomerContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navController: NavHostController,
    viewModel: CustomerViewModel = viewModel()
) {
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    val customers by viewModel.filteredCustomers.collectAsState()

    // Tabs for switching between agents, merchants, and customers
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Agents", "Merchants", "Customers")

    Column {
        // Top Bar with Search
        TopAppBar(
            title = {
                Text(
                    text = "Search",
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
                            navController.navigate("login") {
                                popUpTo("success") { inclusive = true }
                            }
                        },
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowLeft,
                            contentDescription = "Back",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = DeepKoamaru,
                titleContentColor = Color.White
            ),
            modifier = Modifier.height(56.dp)
        )

        // Search Box Section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(DeepKoamaru)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(start = 16.dp, top = 10.dp, end = 16.dp, bottom = 10.dp)
                    .clip(RoundedCornerShape(4.dp))
            ) {
                TextField(
                    textStyle = TextStyle(
                        fontSize = 12.sp,
                        color = Color.Black
                    ),
                    shape = RoundedCornerShape(4.dp),
                    value = searchQuery,
                    onValueChange = { newValue ->
                        searchQuery = newValue
                        viewModel.onSearchQueryChanged(newValue.text)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .heightIn(44.dp)
                        .border(
                            width = 1.dp,
                            color = MetallicSilver,
                            shape = RoundedCornerShape(4.dp)
                        ),
                    placeholder = {
                        Text(
                            text = "Search by Paymaart ID, name or phone number",
                            color = MetallicSilver,
                            fontSize = 12.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(20.dp)
                                .padding(top = 2.dp),
                        )
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { /* Close keyboard or handle actions */ }
                    ),
                    trailingIcon = {
                        if (searchQuery.text.isNotEmpty()) {
                            IconButton(
                                onClick = {
                                    searchQuery = TextFieldValue("")
                                    viewModel.onSearchQueryChanged("")
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Clear text",
                                    tint = MetallicSilver,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        } else {
                            Icon(
                                painter = painterResource(id = R.drawable.search),
                                contentDescription = "Search Icon",
                                tint = MetallicSilver,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        containerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )
            }
        }

        // TabRow for selecting tabs
        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = DeepKoamaru,
            contentColor = Color.White,
            indicator = { tabPositions ->
                Box(
                    Modifier
                        .tabIndicatorOffset(tabPositions[selectedTabIndex])
                        .height(2.dp)
                        .fillMaxWidth()
                        .background(PastelPurple)
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = {
                        Text(
                            text = title,
                            color = if (selectedTabIndex == index) Color.White else Color.Gray
                        )
                    }
                )
            }
        }

        // Display content based on selected tab
        when (selectedTabIndex) {
            0 -> NoAgents()
            1 -> NoMerchants()
            2 -> {
                if (customers.isEmpty()) {
                    if (searchQuery.text.isNotEmpty()) {
                        NoDataFound()
                    } else {
                        NoCustomers()
                    }
                } else {
                    CustomerList(
                        customers = customers,
                        searchQuery = searchQuery.text,
                        onLoadMore = { viewModel.loadMoreCustomers() },
                        navController = navController,
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

// Empty State component
@Composable
fun EmptyState(
    imageResource: Int,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(start = 28.dp, end = 28.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = imageResource),
                contentDescription = title,
                modifier = Modifier
                    .padding(top = 147.dp)
            )

            Spacer(modifier = Modifier.height(34.58.dp))

            Text(
                text = title,
                color = Independence,
                fontSize = 20.sp,
                fontWeight = FontWeight(400),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = subtitle,
                fontSize = 14.sp,
                fontWeight = FontWeight(400),
                color = MetallicSilver,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun NoAgents() {
    EmptyState(
        imageResource = R.drawable.nocustomer,
        title = "No Agents Onboarded for Past 60 days",
        subtitle = "Please check back later"
    )
}

@Composable
fun NoMerchants() {
    EmptyState(
        imageResource = R.drawable.nocustomer,
        title = "No Merchants Onboarded for Past 60 days",
        subtitle = "Please check back later"
    )
}

@Composable
fun NoCustomers() {
    EmptyState(
        imageResource = R.drawable.nocustomer,
        title = "No Customers Onboarded for Past 60 days",
        subtitle = "Please check back later"
    )
}

@Composable
fun NoDataFound() {
    EmptyState(
        imageResource = R.drawable.nodatafound,
        title = "No Data Found",
        subtitle = "Try adjusting your search to find what you're looking for"
    )
}

@Composable
fun MainScreen(navController: NavHostController) {
    val viewModel: CustomerViewModel = viewModel() // Get the ViewModel
    val customers = listOf<User>(/* Sample data or fetched data */)
    val searchQuery = remember { mutableStateOf("") }

    val loadMore = {
        // Add the logic to load more customers
    }

    Column(modifier = Modifier.fillMaxSize()) {
        SearchScreen(navController = navController, viewModel = viewModel) // Pass ViewModel

        CustomerContent(
            customers = customers,
            searchQuery = searchQuery.value,
            onLoadMore = loadMore,
            navController = navController, // Pass navController
            viewModel = viewModel // Pass viewModel
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    PaymaartTheme {
        val navController = rememberNavController()
        MainScreen(navController)
    }
}
