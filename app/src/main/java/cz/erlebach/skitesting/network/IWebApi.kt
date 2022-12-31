package cz.erlebach.skitesting.network

import retrofit2.Call
import retrofit2.http.GET

/**
 * DP-SkiGlideTestingServer provides web API for mobile application
 * @author Tomas Erlebach
 */
interface IWebApi {

    @GET("/api/data")
    fun getTestData(): Call<TestData>


}