package com.example.cargoconnect
// APIService.kt
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import retrofit2.http.POST
import retrofit2.http.Body

interface APIService {

    @POST("login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST("signup")
    suspend fun signup(@Body signupRequest: SignupRequest): Response<SignupResponse>

    @GET("schedule")
    suspend fun getTruckSchedule(
        @Header("Authorization") token: String,
        @Query("truck_id") truckId: String
    ): Response<TruckSchedule>
}

data class LoginRequest(val email: String, val password: String)
data class SignupRequest(val username: String, val email: String, val password: String)

data class LoginResponse(val token: String)
data class SignupResponse(val message: String)

data class TruckSchedule(
    val truckId: String,
    val estimatedArrivalTime: String,
    val routeSteps: List<RouteStep>,
    val dockId: String,
    val dockLocation: String,
    val sourceLongitude: Double,
    val sourceLatitude: Double,
    val destinationLongitude: Double,
    val destinationLatitude: Double
)

data class RouteStep(val step: String, val distance: String)

