package cz.erlebach.skitesting.common.interfaces

import retrofit2.Response

/** šablona pro vzdálený repozitář */
interface IRemoteServerRepository<T> {

    /**
     * @param [userID] jednoznačné id uživatele
     * @return [T] data object from remote server
     */
    suspend fun getAllData(userID: String) : Response<List<T>>

    /** vytvoří nový záznam na serveru
     *  @param [userID] jednoznačné id uživatele
     *
     *  */
    suspend fun insert(userID: String, obj: T)

    /** umožňuje editovat záznam */
    suspend fun update(userID: String, obj: T)

    /** smaže záznam se serveru */
    suspend fun delete(userID: String, obj: T)

}