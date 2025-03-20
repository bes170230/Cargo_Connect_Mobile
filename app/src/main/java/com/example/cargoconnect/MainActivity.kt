package com.example.cargoconnect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.cargoconnect.ui.theme.CargoConnectTheme
import androidx.compose.material3.*
import androidx.navigation.compose.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CargoConnectTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CargoConnectTheme {
        Greeting("Android")
    }
}

interface ApiService {
    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST("auth/signup")
    suspend fun signup(@Body signupRequest: SignupRequest): Response<SignupResponse>

    @POST("schedule/truck")
    suspend fun scheduleTruck(@Body truckScheduleRequest: TruckScheduleRequest, @Header("Authorization") token: String): Response<TruckScheduleResponse>
}

data class LoginRequest(val email: String, val password: String)
data class LoginResponse(val token: String)

data class SignupRequest(val email: String, val password: String, val name: String)
data class SignupResponse(val message: String)

data class TruckScheduleRequest(val truckId: String, val cargoInfo: String)
data class TruckScheduleResponse(val dock: String, val departureTime: String)

object RetrofitInstance {
    private const val BASE_URL = "https://yourapi.com/api/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: ApiService = retrofit.create(ApiService::class.java)
}

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "login") {
        composable("login") { LoginScreen(navController) }
        composable("signup") { SignupScreen(navController) }
        composable("schedule_form") { TruckScheduleFormScreen(navController) }
        composable("schedule_confirmation/{dock}/{departureTime}") { backStackEntry ->
            val dock = backStackEntry.arguments?.getString("dock") ?: ""
            val departureTime = backStackEntry.arguments?.getString("departureTime") ?: ""
            ScheduleConfirmationScreen(dock, departureTime)
        }
    }
}

@Composable
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Login", fontSize = 24.sp)

        // Email and Password TextFields
        TextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
        TextField(value = password, onValueChange = { password = it }, label = { Text("Password") })

        Button(onClick = {
            isLoading = true
            // Call login API here and handle JWT token
            val loginRequest = LoginRequest(email, password)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitInstance.api.login(loginRequest)
                    if (response.isSuccessful) {
                        // Save the JWT Token and navigate to schedule screen
                        val token = response.body()?.token ?: ""
                        navController.navigate("schedule_form")
                    } else {
                        // Handle errors
                    }
                } catch (e: Exception) {
                    // Handle exceptions
                } finally {
                    isLoading = false
                }
            }
        }) {
            Text("Login")
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = { navController.navigate("signup") }) {
            Text("Don't have an account? Sign up")
        }
    }
}

@Composable
fun TruckScheduleFormScreen(navController: NavController) {
    var truckId by remember { mutableStateOf("") }
    var cargoInfo by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val token = "Bearer YOUR_JWT_TOKEN"

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Truck Scheduling", fontSize = 24.sp)

        // Truck ID and Cargo Info TextFields
        TextField(value = truckId, onValueChange = { truckId = it }, label = { Text("Truck ID") })
        TextField(value = cargoInfo, onValueChange = { cargoInfo = it }, label = { Text("Cargo Info") })

        Button(onClick = {
            isLoading = true
            val truckScheduleRequest = TruckScheduleRequest(truckId, cargoInfo)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitInstance.api.scheduleTruck(truckScheduleRequest, token)
                    if (response.isSuccessful) {
                        val schedule = response.body()
                        // Navigate to schedule confirmation with dock and departure time
                        navController.navigate("schedule_confirmation/${schedule?.dock}/${schedule?.departureTime}")
                    } else {
                        // Handle error
                    }
                } catch (e: Exception) {
                    // Handle exception
                } finally {
                    isLoading = false
                }
            }
        }) {
            Text("Submit")
        }
    }
}

@Composable
fun ScheduleConfirmationScreen(dock: String, departureTime: String) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Schedule Confirmed", fontSize = 24.sp)
        Text("Dock: $dock")
        Text("Departure Time: $departureTime")
    }
}
