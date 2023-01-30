package cz.erlebach.skitesting.common.interfaces

import retrofit2.Response

/** šablona pro vzdálený repozitář */
interface IRemoteServerRepository<T> {

    /**
     * @return [T] data object from remote server
     */
    suspend fun getAllData(userID: String) : Response<List<T>>

}