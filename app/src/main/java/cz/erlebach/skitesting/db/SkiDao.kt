package cz.erlebach.skitesting.db

import androidx.lifecycle.LiveData
import androidx.room.*
import cz.erlebach.skitesting.MyDatabase
import cz.erlebach.skitesting.common.utils.dataStatus.DataStatus
import cz.erlebach.skitesting.model.BaseModel
import cz.erlebach.skitesting.model.Ski
import kotlinx.coroutines.flow.Flow


/**
 * Toto rozhraní definuje standardní operace, které se dají provádět s tabulkou [Ski]
 * Dao = Data Access Object */
@Dao
interface SkiDao : BaseDao<Ski>  {

    @Query("SELECT * FROM ${MyDatabase.skiTableName}")
    fun getLiveData(): LiveData<List<Ski>>

    @Query("SELECT * FROM ${MyDatabase.skiTableName} WHERE status != :filterStatus")
    fun getFlow(filterStatus: DataStatus = DataStatus.REMOVED): Flow<List<Ski>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addSki(ski: Ski)

    @Query("DELETE FROM ${MyDatabase.skiTableName}")
    suspend fun deleteAllSkis()

    @Query("SELECT * FROM ${MyDatabase.skiTableName} WHERE status = :status") //todo where status
    suspend fun getDataByStatus(status: DataStatus = DataStatus.OFFLINE): List<Ski>

    @Query("SELECT * FROM ${MyDatabase.skiTableName} WHERE id = :id LIMIT 1")
    suspend fun getSki(id: String): Ski?

}