package cz.erlebach.skitesting.repository

import android.content.Context
import androidx.lifecycle.LiveData
import cz.erlebach.skitesting.model.BaseModel
import cz.erlebach.skitesting.model.Ski
import cz.erlebach.skitesting.model.SkiRide
import cz.erlebach.skitesting.network.RetrofitApiService
import cz.erlebach.skitesting.network.model.DataBody
import cz.erlebach.skitesting.repository.local.SkiRideLocalRepository
import kotlinx.coroutines.flow.Flow

class SkiRideRepository (val context: Context) : Repository(context) {

    val api get() = RetrofitApiService(this.context).skiRideAPI
    private val dao = super.db.skiRideDao()
    val localRepository get() = SkiRideLocalRepository(dao)

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
        api.insert(DataBody(userID, obj))
    }

    /**
     * Provede editaci záznamu na serveru
     * @param userID jednoznačné ID uživatele
     * @param obj [BaseModel] změněný záznam
     */
    override suspend fun updateRemote(userID: String, obj: BaseModel) {
        api.update(DataBody(userID, obj))
    }

    /**
     * Vymaže záznam ze serveru
     * @param userID jednoznačné ID uživatele
     * @param obj [BaseModel] záznam, který bude smazán
     */
    override suspend fun deleteRemote(userID: String, obj: BaseModel) {
        api.delete(DataBody(userID, obj))
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