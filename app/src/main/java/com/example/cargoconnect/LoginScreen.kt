package com.example.cargoconnect

// LoginScreen.kt
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.navigation.NavController

@Composable
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val authManager = AuthManager(LocalContext.current)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
        TextField(value = password, onValueChange = { password = it }, label = { Text("Password") })

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            // Login API call
            val response = RetrofitInstance.api.login(LoginRequest(email, password))
            if (response.isSuccessful) {
                authManager.saveAuthToken(response.body()?.token ?: "")
                navController.navigate("truckSchedule")
            }
        }) {
            Text("Login")
        }

        Button(onClick = { navController.navigate("signup") }) {
            Text("Sign Up")
        }
    }
}
