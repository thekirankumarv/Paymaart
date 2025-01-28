package com.sevenedge.paymaart

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseInQuart
import androidx.compose.animation.core.EaseOutQuart
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.sevenedge.paymaart.api.CustomerDetails
import com.sevenedge.paymaart.api.CustomerService
import com.sevenedge.paymaart.api.RetrofitInstance
import com.sevenedge.paymaart.ui.theme.Border
import com.sevenedge.paymaart.ui.theme.Cultured
import com.sevenedge.paymaart.ui.theme.DeepKoamaru
import com.sevenedge.paymaart.ui.theme.DeepKoamaru02
import com.sevenedge.paymaart.ui.theme.EerieBlack
import com.sevenedge.paymaart.ui.theme.Gainsboro
import com.sevenedge.paymaart.ui.theme.Independence
import com.sevenedge.paymaart.ui.theme.Lotion
import com.sevenedge.paymaart.ui.theme.Magnolia
import com.sevenedge.paymaart.ui.theme.PaymaartTheme
import com.sevenedge.paymaart.ui.theme.SpanishGray
import com.sevenedge.paymaart.ui.theme.TealGreen
import com.sevenedge.paymaart.ui.theme.VividTangelo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import retrofit2.Response
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PaymaartTheme {
                val navController = rememberNavController()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = "login"
                    ) {
                        composable("login") {
                            LoginScreen(navController)
                        }
                        composable("success") {
                            MainScreen(navController)
                        }
                        composable(
                            "kycDetails/{avatar}/{firstName}/{lastName}/{email}",
                            arguments = listOf(
                                navArgument("avatar") { type = NavType.StringType },
                                navArgument("firstName") { type = NavType.StringType },
                                navArgument("lastName") { type = NavType.StringType },
                                navArgument("email") { type = NavType.StringType }
                            )
                        ) { backStackEntry ->
                            val avatar = backStackEntry.arguments?.getString("avatar") ?: ""
                            val firstName = backStackEntry.arguments?.getString("firstName") ?: ""
                            val lastName = backStackEntry.arguments?.getString("lastName") ?: ""
                            val email = backStackEntry.arguments?.getString("email") ?: ""

                            KycDetails(
                                avatar = avatar,
                                firstName = firstName,
                                lastName = lastName,
                                email = email,
                                navController = navController
                            )
                        }
                    }
                }
            }
        }

        // Retrofit service initialization
        val retrofitService = RetrofitInstance.getRetrofitInstance().create(CustomerService::class.java)

        // Making the API call and observing the response
        val responseLiveData: LiveData<Response<CustomerDetails>> = liveData {
            val response = retrofitService.getUsers(2) // API call for page 2
            emit(response) // Emit the API response
        }

        // Observing the LiveData to process the response
        responseLiveData.observe(this) { response ->
            if (response.isSuccessful) {
                val customerDetails = response.body() // Get the response body
                val userList = customerDetails?.data // Extract the list of users

                userList?.forEach { user ->
                    Log.i(
                        "User Info",
                        "ID: ${user.id}, Name: ${user.firstName} ${user.lastName}"
                    )
                }
            } else {
                Log.e("API Error", "Error: ${response.code()} - ${response.message()}")
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun LoginScreen(navController: NavHostController) {
    var selectedTab by remember { mutableStateOf("Password") }
    var selectedLoginMethod by remember { mutableStateOf("Phone") }
    var phoneNumber by remember { mutableStateOf("") }
    var isPhoneError by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("") }
    var isEmailError by remember { mutableStateOf(false) }
    var paymartId by remember { mutableStateOf("") }
    var isPaymartIdError by remember { mutableStateOf(false) }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf("") }
    var isPopupVisible by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .imePadding()
            .systemBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .verticalScroll(scrollState)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Login Text
            Text(
                "Login",
                fontSize = 30.sp,
                fontWeight = FontWeight(700),
                color = Independence,
                modifier = Modifier
                    .padding(start = 7.dp, top = 137.dp)
                    .align(Alignment.Start)
                    .width(82.dp)
                    .height(40.dp)
            )

            // Logo
            Image(
                painter = painterResource(id = R.drawable.paymaart_logo),
                contentDescription = stringResource(id = R.string.paymaart_logo),
                modifier = Modifier
                    .padding(top= 42.dp)
                    .width(172.dp)
                    .height(43.2.dp)
            )

            // Animation
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(9.dp, 26.8.dp, 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                val tabWidth = 172.dp
                val indicatorOffset by animateDpAsState(
                    targetValue = if (selectedTab == "PIN") 0.dp else tabWidth,
                    animationSpec = tween(durationMillis = 200)
                )

                // Stack for animated indicator and buttons
                Box(modifier = Modifier.fillMaxWidth()) {
                    // Sliding Indicator
                    Box(
                        modifier = Modifier
                            .offset(x = indicatorOffset)
                            .width(tabWidth)
                            .height(45.dp)
                            .background(color = Magnolia, shape = RoundedCornerShape(4.dp))
                            .padding(horizontal = 4.dp)
                    )

                    // Tab Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        TabButton(
                            shape = RoundedCornerShape(
                                topStart = 4.dp,
                                topEnd = 0.dp,
                                bottomStart = 4.dp,
                                bottomEnd = 0.dp
                            ),
                            title = "PIN",
                            isSelected = selectedTab == "PIN",
                            onClick = {
                                selectedTab = "PIN"
                                focusManager.clearFocus()
                                keyboardController?.hide()
                            }
                        )
                        TabButton(
                            shape = RoundedCornerShape(
                                topStart = 0.dp,
                                topEnd = 4.dp,
                                bottomStart = 0.dp,
                                bottomEnd = 4.dp
                            ),
                            title = "Password",
                            isSelected = selectedTab == "Password",
                            onClick = {
                                selectedTab = "Password"
                            }
                        )
                    }
                }
            }


            // Only show the rest of the content when Password is selected
            if (selectedTab == "Password") {

                // Section 1: Login Method Selection
                Text(
                    text = "Login By",
                    fontSize = 14.sp,
                    lineHeight = 16.sp,
                    fontWeight = FontWeight(500),
                    color = Independence,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 42.dp)
                )

                // Read-only selection field
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                        .heightIn(min = 56.dp)
                ) {
                    TextField(
                        value = when (selectedLoginMethod) {
                            "Phone" -> "Phone Number"
                            "Email" -> "Email"
                            else -> "Paymaart ID"
                        },
                        onValueChange = { },
                        enabled = false,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 2.dp),
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Cultured,
                            disabledTextColor = DeepKoamaru02,
                            disabledIndicatorColor = Color.Transparent,
                        ),
                        trailingIcon = {
                            Text(
                                text = "Change",
                                fontSize = 14.sp,
                                fontWeight = FontWeight(400),
                                lineHeight = 22.sp,
                                color = DeepKoamaru02,
                                modifier = Modifier
                                    .clickable {
                                        isPopupVisible = true
                                        focusManager.clearFocus()
                                        keyboardController?.hide()
                                    }
                                    .padding(end = 16.dp)
                            )
                        }
                    )
                    // Bottom border with a Box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .align(Alignment.BottomCenter)
                            .background(Gainsboro)
                            .offset(y = -10.dp)
                    )
                }


                // Section 2: Dynamic Input Field with Label
                Text(
                    text = when (selectedLoginMethod) {
                        "Phone" -> "Phone Number"
                        "Email" -> "Email"
                        else -> "Paymaart ID"
                    },
                    fontSize = 14.sp,
                    lineHeight = 16.sp,
                    fontWeight = FontWeight(500),
                    color = Independence,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                )

                TextField(
                    textStyle = TextStyle(fontSize = 14.sp),
                    singleLine = true,
                    value = when (selectedLoginMethod) {
                        "Phone" -> phoneNumber
                        "Email" -> email
                        else -> paymartId
                    },
                    onValueChange = { value ->
                        when (selectedLoginMethod) {
                            "Phone" -> {
                                phoneNumber = value.take(9)
                                isPhoneError = false
                            }
                            "Email" -> {
                                email = value.lowercase()
                                isEmailError = false
                            }
                            else -> {
                                paymartId = value
                                isPaymartIdError = false
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 4.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Cultured,
                        cursorColor = Color.Black,
                        focusedIndicatorColor = when {
                            (selectedLoginMethod == "Phone" && isPhoneError) ||
                                    (selectedLoginMethod == "Email" && isEmailError) ||
                                    (selectedLoginMethod == "Paymaart" && isPaymartIdError) -> Color.Red
                            else ->  Gainsboro
                        },
                        unfocusedIndicatorColor = when {
                            (selectedLoginMethod == "Phone" && isPhoneError) ||
                                    (selectedLoginMethod == "Email" && isEmailError) ||
                                    (selectedLoginMethod == "Paymaart" && isPaymartIdError) -> Color.Red
                            else ->  Gainsboro
                        },
                        errorIndicatorColor = Color.Red
                    ),
                    keyboardOptions = when (selectedLoginMethod) {
                        "Phone" -> KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Next)
                        "Email" -> KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next)
                        else -> KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next)
                    },
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(FocusDirection.Next) // Move to the next input field
                        }
                    ),
                    placeholder = {
                        Text(
                            text = when (selectedLoginMethod) {
                                "Phone" -> "Enter phone number"
                                "Email" -> "Enter email"
                                else -> "Enter Paymaart ID"
                            },
                            color = SpanishGray,
                            fontSize = 14.sp,
                            fontWeight = FontWeight(400),
                            lineHeight = 22.sp
                        )
                    },
                    leadingIcon = when (selectedLoginMethod) {
                        "Phone" -> {
                            { Text(
                                "+265",
                                fontSize = 14.sp,
                                fontWeight = FontWeight(400),
                                lineHeight = 22.sp,
                                color = DeepKoamaru,
                                modifier = Modifier.padding(start = 10.dp)
                            ) }
                        }
                        "Paymaart" -> {
                            { Text(
                                "AGT",
                                fontSize = 14.sp,
                                fontWeight = FontWeight(400),
                                lineHeight = 22.sp,
                                color = DeepKoamaru,
                                modifier = Modifier.padding(start = 10.dp)
                            ) }
                        }
                        else -> null
                    }
                )

                // Error Messages
                if (selectedLoginMethod == "Phone" && isPhoneError) {
                    Text(
                        text = "Invalid phone number",
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    )
                } else if (selectedLoginMethod == "Email" && isEmailError) {
                    Text(
                        text = "Invalid email",
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    )
                } else if (selectedLoginMethod == "Paymaart" && isPaymartIdError) {
                    Text(
                        text = "Invalid Paymaart ID",
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    )
                }

                // Section 3: Password
                Text(
                    text = "Password",
                    fontSize = 14.sp,
                    lineHeight = 16.sp,
                    fontWeight = FontWeight(500),
                    color = Independence,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )

                TextField(
                    textStyle = TextStyle(fontSize = 14.sp),
                    singleLine = true,
                    value = password,
                    onValueChange = {
                        password = it
                        passwordError = "" // Clear error when typing
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Cultured,
                        cursorColor = Color.Black,
                        focusedIndicatorColor = if (passwordError.isNotEmpty()) Color.Red else Gainsboro,
                        unfocusedIndicatorColor = if (passwordError.isNotEmpty()) Color.Red else Gainsboro
                    ),
                    placeholder = {
                        Text(
                            text = "Enter password",
                            fontSize = 14.sp,
                            fontWeight = FontWeight(400),
                            lineHeight = 22.sp,
                            color = SpanishGray
                        )
                    },
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = if (password.isNotEmpty()) {
                        {
                            Text(
                                text = if (showPassword) "HIDE" else "SHOW",
                                fontSize = 10.sp,
                                color = TealGreen,
                                modifier = Modifier.clickable { showPassword = !showPassword }
                            )
                        }
                    } else null
                )

                if (passwordError.isNotEmpty()) {
                    Text(
                        text = passwordError,
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .align(Alignment.Start)
                    )
                }

                // Forgot Password
                Text(
                    text = "Forgot Password?",
                    fontSize = 12.sp,
                    fontWeight = FontWeight(400),
                    lineHeight = 20.sp,
                    color = Independence,
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp, bottom = 24.dp)
                        .clickable {
                            focusManager.clearFocus()
                            keyboardController?.hide()
                            Toast
                                .makeText(context, "Forgot Password clicked", Toast.LENGTH_SHORT)
                                .show()
                        }
                )

                Button(
                    onClick = {
                        focusManager.clearFocus()
                        keyboardController?.hide()
                        when (selectedLoginMethod) {
                            "Phone" -> {
                                isPhoneError = phoneNumber.isEmpty() || !phoneNumber.matches(Regex("^[0-9]{9}$"))
                            }
                            "Email" -> {
                                isEmailError = email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
                            }
                            "Paymaart" -> {
                                isPaymartIdError = paymartId.isEmpty() || paymartId != "4321"
                            }
                        }
                        if (password.isEmpty()) {
                            passwordError = "Password cannot be blank"
                        } else if (password != "kiran") {
                            passwordError = "Invalid credentials"
                        } else if (!isPhoneError && !isEmailError && !isPaymartIdError && passwordError.isEmpty()) {
                            passwordError = "" // Clear error on success
                            navController.navigate("success")
                        }
                    },
                    shape = RoundedCornerShape(8),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(58.dp)
                        .padding(bottom = 16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = DeepKoamaru)
                ) {
                    Text(
                        text = "Login",
                        fontSize = 18.sp,
                        fontWeight = FontWeight(500),
                        lineHeight = 26.sp,
                        color = Lotion
                    )
                }


                // New Agent Registration
                Row(
                    modifier = Modifier
                        .fillMaxHeight()
                        .align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        text = "New Agent? ",
                        fontSize = 14.sp,
                        fontWeight = FontWeight(400),
                        color = EerieBlack,
                    )
                    Text(
                        text = "Register Now",
                        fontSize = 14.sp,
                        fontWeight = FontWeight(400),
                        color = VividTangelo,
                        modifier = Modifier.clickable {
                            focusManager.clearFocus()
                            keyboardController?.hide()
                            Toast.makeText(context, "Register Now clicked", Toast.LENGTH_SHORT).show()

                        }
                    )
                }
            }
          //  Spacer(modifier = Modifier.weight(1f))
        }

    }

    @Composable
    fun SelectionOption(
        text: String,
        icon: Int,
        onClick: () -> Unit
    ) {
        // Your existing SelectionOption code remains the same
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(vertical = 12.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier
                    .size(25.dp)
                    .padding(end = 8.dp)
            )
            Text(
                text = text,
                fontSize = 14.sp,
                lineHeight = 22.sp,
                color = EerieBlack
            )
        }
    }

    @Composable
    fun AnimatedAlertDialog(
        isVisible: Boolean,
        onDismissRequest: () -> Unit,
        content: @Composable () -> Unit
    ) {
        AnimatedVisibility(
            visible = isVisible,
            enter = slideInVertically(
                initialOffsetY = { fullHeight -> fullHeight },
                animationSpec = tween(durationMillis = 100, easing = EaseOutQuart)
            ),
            exit = slideOutVertically(
                targetOffsetY = { fullHeight -> fullHeight },
                animationSpec = tween(durationMillis = 100, easing = EaseInQuart)
            )
        ) {
            AlertDialog(
                onDismissRequest = onDismissRequest,
                text = { content() },
                confirmButton = {},
                shape = RoundedCornerShape(4.dp),
                properties = DialogProperties(
                    dismissOnBackPress = true,
                    dismissOnClickOutside = true
                ),
                modifier = Modifier
                    .size(280.dp, 212.dp)
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(4.dp)
                    ),
                containerColor = Color.White
            )
        }
    }

// Pop up:
    if (isPopupVisible) {
        AnimatedAlertDialog(
            isVisible = isPopupVisible,
            onDismissRequest = { isPopupVisible = false }
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                SelectionOption(
                    text = "Phone Number",
                    icon = R.drawable.phone
                ) {
                    selectedLoginMethod = "Phone"
                    isPopupVisible = false
                }
                SelectionOption(
                    text = "Email",
                    icon = R.drawable.mail
                ) {
                    selectedLoginMethod = "Email"
                    isPopupVisible = false
                }
                SelectionOption(
                    text = "Paymaart ID",
                    icon = R.drawable.paymaarticon
                ) {
                    selectedLoginMethod = "Paymaart"
                    isPopupVisible = false
                }
            }
        }
    }

}

@Composable
fun TabButton(
    title: String,
    isSelected: Boolean,
    shape: Shape,
    onClick: () -> Unit
) {
    val noRippleInteractionSource = remember { NoRippleInteractionSource() }

    TextButton(
        onClick = onClick,
        shape = shape,
        border = BorderStroke(1.dp, color = Border),
        modifier = Modifier
            .height(45.dp)
            .width(172.dp),
        interactionSource = noRippleInteractionSource,
        colors = ButtonDefaults.textButtonColors(
            containerColor = Color.Transparent,
            contentColor = if (isSelected) DeepKoamaru else Independence
        )
    ) {
        Text(text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight(400),
            lineHeight = 28.sp
        )
    }
}

class NoRippleInteractionSource : MutableInteractionSource {
    override val interactions: Flow<Interaction> = emptyFlow()

    override suspend fun emit(interaction: Interaction) {
    }

    override fun tryEmit(interaction: Interaction): Boolean {
        return false
    }
}



@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    val previewNavController = rememberNavController()
    PaymaartTheme {
        LoginScreen(navController = previewNavController)
    }
}

