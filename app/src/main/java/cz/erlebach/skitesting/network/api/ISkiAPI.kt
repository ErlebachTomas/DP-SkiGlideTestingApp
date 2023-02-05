package cz.erlebach.skitesting.network.api

import cz.erlebach.skitesting.model.Ski
import cz.erlebach.skitesting.network.model.SkiDataBody
import retrofit2.Response
import retrofit2.http.*

interface ISkiAPI {

    @GET("getAllUsersSki")
    suspend fun getAllData( @Query("user") userID: String): Response<List<Ski>>

    @POST("deleteSki")
   suspend fun delete(@Body body: SkiDataBody)

    @POST("updateSki")
   suspend fun update(@Body body: SkiDataBody)

    @POST("addSki")
    suspend fun insert(@Body body: SkiDataBody)
    @GET("deleteAllUsersSkis")
    suspend fun deleteAll(@Query("user") userID: String)

}