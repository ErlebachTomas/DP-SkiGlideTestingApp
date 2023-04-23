package cz.erlebach.skitesting.network.api

import cz.erlebach.skitesting.network.model.GeneralDataBody
import cz.erlebach.skitesting.network.model.DemoDataBody
import cz.erlebach.skitesting.network.model.recomendation.RecommendationDataBody
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

     @POST("recomendacion")
    //@POST("recomendacionDemoData")
    suspend fun recomendacion(
        @Body post: GeneralDataBody
    ): Response<List<RecommendationDataBody>>


    /* pro testování */
    @GET("data")
    fun getTestData(): Call<DemoDataBody>

    @GET("data")
    suspend fun getTestData2(): Response<DemoDataBody>

    @POST("post-test")
    suspend fun testPost(
        @Body post: DemoDataBody
    ): Response<DemoDataBody>

}