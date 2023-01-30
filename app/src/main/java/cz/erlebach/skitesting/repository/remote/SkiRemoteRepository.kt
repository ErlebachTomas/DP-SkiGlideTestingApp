package cz.erlebach.skitesting.repository.remote

import android.content.Context
import cz.erlebach.skitesting.common.interfaces.IRemoteServerRepository
import cz.erlebach.skitesting.model.Ski
import cz.erlebach.skitesting.network.RetrofitApiService
import retrofit2.Response

class SkiRemoteRepository(val context: Context) : IRemoteServerRepository<Ski> {
    private val api = RetrofitApiService(this.context).skiTestingServerAPI

    override suspend fun getAllData(userID: String): Response<List<Ski>> {
        return api.getAllData(userID)
    }

    override suspend fun delete(userID: String, obj: Ski) {
        throw NotImplementedError("Not implemented")
    }
    override suspend fun update(userID: String, obj: Ski) {
        throw NotImplementedError("Not implemented")
    }
    override suspend fun insert(userID: String, obj: Ski) {
        throw NotImplementedError("Not implemented")
    }


}