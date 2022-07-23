package cz.erlebach.skitesting.network

import retrofit2.Call
import retrofit2.http.GET

interface IWebApi {

    @GET("/api/data")
    fun getTestData(): Call<TestData>

}