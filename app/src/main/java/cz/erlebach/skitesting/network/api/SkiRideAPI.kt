package cz.erlebach.skitesting.network.api

import cz.erlebach.skitesting.model.SkiRide
import cz.erlebach.skitesting.model.wrappers.SkiRideWithSki
import cz.erlebach.skitesting.network.model.GeneralDataBody
import retrofit2.Response
import retrofit2.http.*

interface SkiRideAPI {

    @GET("getAllSkiRide")
    suspend fun getAllData( @Query("user") userID: String): Response<List<SkiRide>>

    @POST("deleteSkiRide")
   suspend fun delete(@Body body: GeneralDataBody)

    @POST("updateSkiRide")
    suspend fun update(@Body body: GeneralDataBody)

    @POST("addSkiRide")
    suspend fun insert(@Body body: GeneralDataBody)

    @POST("syncSkiRide")
    suspend fun syncData(@Body body: GeneralDataBody): Response<SkiRide>

    @GET("getAllSkiRideWithSki")
    suspend fun getDataWithSki(@Query("user") userID: String, @Query("testID") testID: String ): Response<List<SkiRideWithSki>>
}