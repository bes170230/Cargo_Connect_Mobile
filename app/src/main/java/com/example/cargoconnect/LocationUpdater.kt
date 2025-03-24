package com.example.cargoconnect

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.cargoconnect.RetrofitInstance

fun sendLocationToDali(location: TruckLocation) {
    RetrofitInstance.api.sendTruckLocation(location).enqueue(object : Callback<Void> {
        override fun onResponse(call: Call<Void>, response: Response<Void>) {
            // Handle success
        }

        override fun onFailure(call: Call<Void>, t: Throwable) {
            // Handle failure
        }
    })
}
