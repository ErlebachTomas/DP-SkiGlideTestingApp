package cz.erlebach.skitesting.network.api

import cz.erlebach.skitesting.model.TestSession
import cz.erlebach.skitesting.network.model.GeneralDataBody
import retrofit2.Response
import retrofit2.http.*

interface TestSessionAPI {

    @GET("getAllUserTests")
    suspend fun getAllData( @Query("userID") userID: String): Response<List<TestSession>>

    @POST("deleteTestSession")
   suspend fun delete(@Body body: GeneralDataBody)

    @POST("updateTestSession")
    suspend fun update(@Body body: GeneralDataBody)

    @POST("addTestSession")
    suspend fun insert(@Body body: GeneralDataBody)

    @POST("syncTestSession")
    suspend fun syncData(@Body body: GeneralDataBody): Response<TestSession>
}