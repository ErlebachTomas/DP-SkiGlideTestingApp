package cz.erlebach.skitesting.network

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * DP-SkiGlideTestingServer provides web API for mobile application
 * @author Tomas Erlebach
 */
interface IWebApi {

    @GET("/api/data")
    fun getTestData(): Call<TestData>

    @GET("/api/data")
    suspend fun getTestData2(): Response<TestData>

    @POST("/api/post-test")
    suspend fun testPost(
        @Body post: TestData
    ): Response<TestData>
}