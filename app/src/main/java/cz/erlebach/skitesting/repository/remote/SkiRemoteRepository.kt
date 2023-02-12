package cz.erlebach.skitesting.repository.remote

import android.content.Context
import cz.erlebach.skitesting.common.interfaces.IRemoteServerRepository
import cz.erlebach.skitesting.model.Ski
import cz.erlebach.skitesting.network.RetrofitApiService
import cz.erlebach.skitesting.network.model.SkiDataBody
import retrofit2.Response

class SkiRemoteRepository(val context: Context) : IRemoteServerRepository<Ski> {
    private val api = RetrofitApiService(this.context).skiAPI

    override suspend fun getAllData(userID: String): Response<List<Ski>> {
        return api.getAllData(userID)
    }

    suspend fun getList(userID: String): List<Ski> {
        val response = api.getAllData(userID)
        if (!response.isSuccessful) {
            throw Exception(response.message())
        } else {
            return response.body()!!
        }
    }

    override suspend fun delete(userID: String, obj: Ski) {
        return api.delete(SkiDataBody(userID,obj))
    }
    override suspend fun update(userID: String, obj: Ski) {
        return api.update(SkiDataBody(userID,obj))
    }
    override suspend fun insert(userID: String, obj: Ski) {
        return api.insert(SkiDataBody(userID, obj))
    }

    suspend fun deleteAll(userID: String) {
        return api.deleteAll(userID)
    }

}