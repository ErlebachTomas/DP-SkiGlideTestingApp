package cz.erlebach.skitesting.repository
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.withTransaction
import cz.erlebach.skitesting.common.utils.debug
import cz.erlebach.skitesting.model.BaseModel
import cz.erlebach.skitesting.model.Ski
import cz.erlebach.skitesting.network.RetrofitApiService
import cz.erlebach.skitesting.network.api.SkiAPI
import cz.erlebach.skitesting.network.model.SkiDataBody
import cz.erlebach.skitesting.repository.local.SkiLocalRepository
import kotlinx.coroutines.flow.Flow

/**
 * Obsluha [Ski]
 * Kombinuje přístup k lokálním datům pomocí [SkiLocalRepository] a vzdálená data pomocí [SkiAPI]
 */

class SkiRepository(val context: Context): Repository(context) {

      val api get() = RetrofitApiService(this.context).skiAPI
      private val localRepository get() = SkiLocalRepository(super.db.skiDao())

    /** lokální cache lyží */
    val userSki : LiveData<List<Ski>> get() = localRepository.readAllData

    /**
     * Načtení dat z lokální ROOM databáze pomocí Dao
     *
     */
    override fun getLocalDataFlow(): Flow<List<BaseModel>> {

        return super.db.skiDao().getFlow()
    }
    /**
     * Načtení dat pomocí [RetrofitApiService]
     */
    override suspend fun getRemoteData(): List<BaseModel> {
        val response = api.getAllData(super.account.getUserID())

        debug("Repository getList response")
        debug(response.code().toString() + response.message().toString())
        debug(response.raw().toString())

        return super.getListFromResponse(response)


    }
    override suspend fun getLocalModelByID(primaryKey: String): BaseModel? {
        return super.db.skiDao().getSki(primaryKey)
    }

    override suspend fun insertRemote(userID: String, obj: BaseModel) {
        return api.insert(SkiDataBody(userID, obj as Ski))
    }

    override suspend fun updateRemote(userID: String, obj: BaseModel) {
        return api.update(SkiDataBody(userID, obj as Ski))
    }

    override suspend fun deleteRemote(userID: String, obj: BaseModel) {
        return api.delete(SkiDataBody(userID,obj as Ski))
    }

    /**
     * Provede lokální editaci záznamu v databázi
     */
    override suspend fun updateLocalModel(obj: BaseModel) {
        localRepository.update(obj as Ski)
    }

    /**
     * Vloží lokálně nový záznam do databáze
     */
    override suspend fun insertLocalModel(obj: BaseModel) {
        localRepository.insert(obj as Ski)
    }

    /**
     * Smaže záznam z lokální databáze
     */
    override suspend fun deleteLocalModel(obj: BaseModel) {
        localRepository.delete(obj as Ski)
    }

    /**
     * Vymaže všechny [Ski] uživatele
     */
    suspend fun deleteAll() {
        db.withTransaction {
            localRepository.deleteAll()
            api.deleteAll(account.getUserID())
        }
    }

}