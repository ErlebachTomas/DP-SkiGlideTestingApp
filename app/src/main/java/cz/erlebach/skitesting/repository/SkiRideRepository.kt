package cz.erlebach.skitesting.repository

import android.content.Context
import androidx.lifecycle.LiveData
import cz.erlebach.skitesting.common.utils.info
import cz.erlebach.skitesting.model.BaseModel
import cz.erlebach.skitesting.model.Ski
import cz.erlebach.skitesting.model.SkiRide
import cz.erlebach.skitesting.model.wrappers.SkiRideWithSki
import cz.erlebach.skitesting.network.RetrofitApiService
import cz.erlebach.skitesting.network.model.GeneralDataBody
import cz.erlebach.skitesting.repository.local.SkiRideLocalRepository
import cz.erlebach.skitesting.repository.resource.networkBoundResource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow

open class SkiRideRepository (val context: Context) : Repository(context) {

    val api get() = RetrofitApiService(this.context).skiRideAPI
    private val dao = super.db.skiRideDao()
    private val localRepository get() = SkiRideLocalRepository(dao)

    fun readDataWithSkisForTest(testID: String) = networkBoundResource(
        localFlow = {
            localRepository.getJoinedSkiRidesForTest(testID)
        },
        fetchFromRemote = {
            delay(4000)
            this.getSkiRideRemoteDataWithSkis(testID)
        },
        sync = { listResponse ->
            for (onlineData: SkiRideWithSki in listResponse) {

                val offlineData = getLocalModelByID(onlineData.skiRide.id)

                if (offlineData != null) {

                    if (onlineData.skiRide.isNewer(offlineData)) {
                        info("aktualizuji ${onlineData.skiRide.toString()}")
                        updateLocalModel(onlineData.skiRide)
                    }
                } else {
                    info("stahuji a vkládám ${onlineData.skiRide.toString()}")
                    insertLocalModel(onlineData.skiRide)
                }
            }
            info("sync ${listResponse.count()}")
        }

    )
    private suspend fun getSkiRideRemoteDataWithSkis(testID: String): List<SkiRideWithSki> {
        val response = api.getDataWithSki(super.account.getUserID(), testID)
        return super.getListFromResponse(response)
    }

    suspend fun deleteLocalCache()  = dao.deleteAllSkiRides()

    suspend fun getSki(skiRide: SkiRide): Ski? {
        return db.skiDao().getSki(skiRide.skiID)
    }

    /**
     * Načte všechny provedené jízdy z testu
     * @param testID [String] id testu
     * @return [LiveData] jízdy testu
     */
    fun loadTestSessionRideByID(testID: String): LiveData<List<SkiRide>> {
        return dao.loadTestSessionRideByID(testID)
    }

    /**
     * Načtení dat z lokální ROOM databáze pomocí Dao
     */
    override fun getLocalDataFlow(): Flow<List<BaseModel>> {
        return dao.getFlow()
    }

    /**
     * Načtení dat pomocí [RetrofitApiService]
     */
    override suspend fun getRemoteData(): List<BaseModel> {
        val response = api.getAllData(super.account.getUserID())
        return super.getListFromResponse(response)
    }

    /**
     * Načtení [BaseModel] záznam
     */
    override suspend fun getLocalModelByID(primaryKey: String): BaseModel? {
        return dao.getTest(primaryKey)
    }

    /**
     * Zajišťuje nahrání nového záznamu na server
     * @param userID jednoznačné ID uživatele
     * @param obj [BaseModel] nový záznam
     */
    override suspend fun insertRemote(userID: String, obj: BaseModel) {
        api.insert(GeneralDataBody(userID, obj))
    }

    /**
     * Provede editaci záznamu na serveru
     * @param userID jednoznačné ID uživatele
     * @param obj [BaseModel] změněný záznam
     */
    override suspend fun updateRemote(userID: String, obj: BaseModel) {
        api.update(GeneralDataBody(userID, obj))
    }

    /**
     * Vymaže záznam ze serveru
     * @param userID jednoznačné ID uživatele
     * @param obj [BaseModel] záznam, který bude smazán
     */
    override suspend fun deleteRemote(userID: String, obj: BaseModel) {
        api.delete(GeneralDataBody(userID, obj))
    }

    /**
     * Provede lokální editaci záznamu v databázi
     */
    override suspend fun updateLocalModel(obj: BaseModel) {
        dao.update(obj as SkiRide)
    }

    /**
     * Vloží lokálně nový záznam do databáze
     */
    override suspend fun insertLocalModel(obj: BaseModel) {
        dao.insert(obj as SkiRide)
    }

    /**
     * Smaže záznam z lokální databáze
     */
    override suspend fun deleteLocalModel(obj: BaseModel) {
        dao.delete(obj as SkiRide)
    }
}