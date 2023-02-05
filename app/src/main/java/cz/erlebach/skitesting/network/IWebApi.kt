package cz.erlebach.skitesting.network

import cz.erlebach.skitesting.model.Ski
import cz.erlebach.skitesting.network.model.TestDataBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * DP-SkiGlideTestingServer provides web API for mobile application
 * @author Tomas Erlebach
 */
interface IWebApi {

    @GET("data")
    fun getTestData(): Call<TestDataBody>

    @GET("data")
    suspend fun getTestData2(): Response<TestDataBody>

    @POST("post-test")
    suspend fun testPost(
        @Body post: TestDataBody
    ): Response<TestDataBody>

    @GET("getAllUsersSki")
    suspend fun getAllData( @Query("user") userID: String): Response<List<Ski>> //todo query ?user=auth0|62c3317067fdea356d289028


}