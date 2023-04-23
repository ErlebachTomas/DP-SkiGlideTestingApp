package cz.erlebach.skitesting.repository

import android.content.Context
import cz.erlebach.skitesting.model.BaseModel
import cz.erlebach.skitesting.model.TestSession
import cz.erlebach.skitesting.network.RetrofitApiService
import cz.erlebach.skitesting.network.model.GeneralDataBody
import cz.erlebach.skitesting.repository.local.TestSessionLocalRepository
import kotlinx.coroutines.flow.Flow

class TestSessionRepository(val context: Context) : Repository(context) {

    val api get() = RetrofitApiService(this.context).testSessionAPI
    private val dao = super.db.testSessionDao()
    val localRepository get() = TestSessionLocalRepository(dao)

    /** Smaže vše pouze z room databáze */
    suspend fun deleteLocalCache() = dao.deleteAllTestSessions()

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
        api.insert(GeneralDataBody(userID,obj))
    }

    /**
     * Provede editaci záznamu na serveru
     * @param userID jednoznačné ID uživatele
     * @param obj [BaseModel] změněný záznam
     */
    override suspend fun updateRemote(userID: String, obj: BaseModel) {
        api.update(GeneralDataBody(userID,obj))
    }

    /**
     * Vymaže záznam ze serveru
     * @param userID jednoznačné ID uživatele
     * @param obj [BaseModel] záznam, který bude smazán
     */
    override suspend fun deleteRemote(userID: String, obj: BaseModel) {
        api.delete(GeneralDataBody(userID,obj))
    }

    /**
     * Provede lokální editaci záznamu v databázi
     */
    override suspend fun updateLocalModel(obj: BaseModel) {
        checkTypeAndDoAction(obj) {
            dao.update(it)
        }
    }

    /**
     * Vloží lokálně nový záznam do databáze
     */
    override suspend fun insertLocalModel(obj: BaseModel) {
        dao.insert(obj as TestSession)
    }

    /**
     * Smaže záznam z lokální databáze
     */
    override suspend fun deleteLocalModel(obj: BaseModel) {
        dao.delete(obj as TestSession)
    }

    private suspend fun checkTypeAndDoAction(obj: BaseModel, action: suspend (obj: TestSession) -> Unit ) {
        if (obj is TestSession) {
            action(obj)
        } else {
            throw java.lang.IllegalArgumentException("Invalid argument $obj")
        }
    }
}