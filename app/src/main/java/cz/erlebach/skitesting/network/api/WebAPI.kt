package cz.erlebach.skitesting.network.api

import cz.erlebach.skitesting.network.model.TestDataBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * DP-SkiGlideTestingServer provides web API for mobile application
 * @author Tomas Erlebach
 */
interface WebAPI {

    @GET("data")
    fun getTestData(): Call<TestDataBody>

    @GET("data")
    suspend fun getTestData2(): Response<TestDataBody>

    @POST("post-test")
    suspend fun testPost(
        @Body post: TestDataBody
    ): Response<TestDataBody>

}