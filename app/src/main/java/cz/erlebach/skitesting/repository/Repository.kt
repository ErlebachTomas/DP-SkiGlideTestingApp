package cz.erlebach.skitesting.repository

import android.content.Context
import androidx.room.withTransaction
import cz.erlebach.skitesting.MyDatabase
import cz.erlebach.skitesting.common.SessionManager
import cz.erlebach.skitesting.common.exceptions.RetrofitError
import cz.erlebach.skitesting.common.interfaces.IAccountManagement
import cz.erlebach.skitesting.common.utils.dataStatus.DataStatus
import cz.erlebach.skitesting.common.utils.info
import cz.erlebach.skitesting.common.utils.isDeviceOnline
import cz.erlebach.skitesting.common.utils.wtf
import cz.erlebach.skitesting.model.BaseModel
import cz.erlebach.skitesting.network.RetrofitApiService
import cz.erlebach.skitesting.repository.resource.networkBoundResource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

/** Kombinuje lokální a vzdálená data */
abstract class Repository (
    private val context: Context,
    )
{
    protected val db = MyDatabase.getDatabase(context)
    protected val account: IAccountManagement = SessionManager.getInstance(context)
    /**
     * Načtení dat z lokální ROOM databáze pomocí Dao
     */
    protected abstract fun getLocalDataFlow(): Flow<List<BaseModel>>
    /**
     * Načtení dat pomocí [RetrofitApiService]
     */
    protected abstract suspend fun getRemoteData(): List<BaseModel>

    /**
     * Načtení [BaseModel] záznam
     */
    protected abstract suspend fun getLocalModelByID(primaryKey : String) : BaseModel?

    /**
     * Načtení a synchronizace dat pomocí flow
     * nejprve se provede načtení offline dat z databáze lambda funkce (localFlow)
     * následně dojde ke stažení online dat z webového api (fetchFromRemote)
     * a provede se synchronizace online dat s offline daty (sync)
     */
    open val readAllData = networkBoundResource(
        localFlow = {
            this.getLocalDataFlow()
        },
        fetchFromRemote = {
            delay(1000) // jinak se při mazání zase stáhne a nic se nesmaže
            this.getRemoteData()
        },
        sync = { listResponse ->
            for (onlineData: BaseModel in listResponse) {
                // if novější tak update, pokud není vložit jako novou
                val offlineData = getLocalModelByID(onlineData.id)
                if (offlineData != null) {

                    if (onlineData.isNewer(offlineData)) {
                        info("aktualizuji ${onlineData.toString()}")
                        updateLocalModel(onlineData)
                    }
                } else {
                    info("stahuji a vkládám ${onlineData.toString()}")
                    insertLocalModel(onlineData)
                }
            }
            info("synchronized ${listResponse.count()}")
        }

    )

    /**
     * Vymaže data ze serveru
     * pokud není zařízené připojene k síti, pouze nastaví příznak pro vymazání
     */
    suspend fun delete(obj: BaseModel)  {

        if(isDeviceOnline(context)) {
            db.withTransaction {
                deleteRemote(account.getUserID(), obj)
                obj.status = DataStatus.REMOVED
                updateLocalModel(obj)
                info("deleting" + obj.id )
            }
        } else {
           obj.status = DataStatus.REMOVED
           updateLocalModel(obj)
        }

    }

    /**
     * Pomocná funkce
     * @param obj [BaseModel]
     * @param localAction lambda funkce pro lokální repozitář
     * @param remoteAction lambda funkce pro vzdálený repozitář
     */
    private suspend fun action(
        obj: BaseModel,
        localAction: suspend (BaseModel) -> Unit,
        remoteAction: suspend (String, BaseModel ) -> Unit
    ): DataStatus {

        return if(isDeviceOnline(context)) {
            obj.status = DataStatus.ONLINE

            db.withTransaction {
                remoteAction(account.getUserID(), obj)
                localAction(obj)
            }
            DataStatus.ONLINE

        } else {
            obj.status = DataStatus.OFFLINE
            localAction(obj)
            DataStatus.OFFLINE
        }
    }


    /**
     * Získá list z api Response<List<BaseModel>>     *
     *  @throws RetrofitError error pokud response nebyla úspěšná
     */
    protected fun <T> getListFromResponse(response: Response<List<T>>):List<T>  {
        if (!response.isSuccessful) {
            val err = RetrofitError(response.code(),response.message(),response.errorBody())
            wtf("getListFromResponse failed", err)
            throw err
        } else {
            return response.body()!!
        }
    }


    /**
     * Umožňuje editaci [BaseModel] jak na serveru tak lokálně
     * @param obj [BaseModel]
     */

    suspend fun update(obj: BaseModel) {
       val result = this.action(obj = obj, localAction = {this.updateLocalModel(obj = it)},
            remoteAction = { id, data -> this.updateRemote(id, data)}
            )
        info("Updated $result $obj" )
    }

    /**
     * Vložení nového záznamu jak na serverer tak lokálně do databáze
     */
    suspend fun insert(obj: BaseModel) {
        val result = this.action(obj = obj, localAction = {this.insertLocalModel(obj=it)},
            remoteAction = { id, data -> this.insertRemote(id, data)}
        )
        info("Updated $result $obj" )
    }


    /**
     * Zajišťuje nahrání nového záznamu na server
     * @param userID jednoznačné ID uživatele
     * @param obj [BaseModel] nový záznam
     */
    protected abstract suspend fun insertRemote(userID: String, obj: BaseModel)

    /**
     * Provede editaci záznamu na serveru
     * @param userID jednoznačné ID uživatele
     * @param obj [BaseModel] změněný záznam
     */
    protected abstract suspend fun updateRemote(userID: String, obj: BaseModel)

    /**
     * Vymaže záznam ze serveru
     * @param userID jednoznačné ID uživatele
     * @param obj [BaseModel] záznam, který bude smazán
     */
    protected abstract suspend fun deleteRemote(userID: String, obj: BaseModel)

    /**
     * Provede lokální editaci záznamu v databázi
     */
    protected abstract suspend fun updateLocalModel(obj: BaseModel)
    /**
     * Vloží lokálně nový záznam do databáze
     */
    protected abstract suspend fun insertLocalModel(obj: BaseModel)

    /**
     * Smaže záznam z lokální databáze
     */
    protected abstract suspend fun deleteLocalModel(obj: BaseModel)




}



