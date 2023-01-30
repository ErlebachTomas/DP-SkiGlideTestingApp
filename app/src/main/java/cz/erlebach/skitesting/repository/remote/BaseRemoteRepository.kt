package cz.erlebach.skitesting.repository.remote

import cz.erlebach.skitesting.common.interfaces.IRemoteServerRepository
import cz.erlebach.skitesting.network.api.BaseServerAPI
import retrofit2.Response

abstract class BaseRemoteRepository<T>(private val api: BaseServerAPI<T>) :
    IRemoteServerRepository<T> {

    override suspend fun getAllData(userID: String): Response<List<T>> {
        return api.getAllData(userID)
    }
    override suspend fun delete(userID: String, obj: T) {
        return api.delete(userID, obj)
    }
    override suspend fun update(userID: String, obj: T) {
        return api.update(userID, obj)
    }
    override suspend fun insert(userID: String, obj: T) {
        return api.insert(userID, obj)
    }


}