package com.example.cargoconnect

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @GET("getTruckDetails")
    fun getTruckDetails(
        @Query("truckId") truckId: String
    ): Call<TruckDetails>

    @GET("getRouteGuidance")
    fun getRouteGuidance(
        @Query("truckId") truckId: String
    ): Call<RouteGuidance>

    @POST("sendTruckLocation")
    fun sendTruckLocation(
        @Body truckLocation: TruckLocation
    ): Call<Void>
}
