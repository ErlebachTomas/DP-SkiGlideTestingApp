package cz.erlebach.skitesting.network.api

import cz.erlebach.skitesting.model.Ski
import cz.erlebach.skitesting.model.SkiRide
import cz.erlebach.skitesting.model.TestSession
import cz.erlebach.skitesting.network.model.DataBody
import cz.erlebach.skitesting.network.model.SkiDataBody
import retrofit2.Response
import retrofit2.http.*

interface SkiRideAPI {

    @GET("getAllSkiRide")
    suspend fun getAllData( @Query("user") userID: String): Response<List<SkiRide>>

    @POST("deleteSkiRide")
   suspend fun delete(@Body body: DataBody)

    @POST("updateSkiRide")
    suspend fun update(@Body body: DataBody)

    @POST("addSkiRide")
    suspend fun insert(@Body body: DataBody)

    @POST("syncSkiRide")
    suspend fun syncData(@Body body: DataBody): Response<SkiRide>
}