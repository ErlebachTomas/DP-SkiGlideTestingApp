package cz.erlebach.skitesting.network.api

import cz.erlebach.skitesting.model.Ski
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ISkiAPI: BaseServerAPI<Ski> {

    @GET("getAllUsersSki")
    override suspend fun getAllData( @Query("user") userID: String): Response<List<Ski>> //todo query ?user=auth0|62c3317067fdea356d289028

   override suspend fun delete(userID: String, obj: Ski)

   override suspend fun update(userID: String, obj: Ski)

   override suspend fun insert(userID: String, obj: Ski)

}