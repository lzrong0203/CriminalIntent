package network

import retrofit2.http.GET
import retrofit2.http.HEAD
import retrofit2.http.Headers


interface TaipeiTourApi {
    @Headers(
        "accept: application/json"
    )
    @GET(
            "open-api/zh-tw/Attractions/All"
    )
    suspend fun fetchContents(): String
}