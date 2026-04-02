package com.technet.olttv.data.network

import com.technet.olttv.data.model.DashboardResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("olt-monitor/api/dashboard-tv.php")
    suspend fun getDashboard(
        @Query("token") token: String
    ): DashboardResponse

    companion object {
        const val BASE_URL = "http://200.106.207.64:5009/"
        const val API_TOKEN = "TECHNET_TV_V1_2026"
    }
}
