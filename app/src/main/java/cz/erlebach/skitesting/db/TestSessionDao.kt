package cz.erlebach.skitesting.db

import androidx.lifecycle.LiveData
import androidx.room.*
import cz.erlebach.skitesting.MyDatabase
import cz.erlebach.skitesting.common.utils.dataStatus.DataStatus
import cz.erlebach.skitesting.model.Ski
import cz.erlebach.skitesting.model.SkiRide
import cz.erlebach.skitesting.model.TestSession
import kotlinx.coroutines.flow.Flow

/** Toto rozhraní definuje standardní operace, které se dají provádět s tabulkou [TestSession] */
@Dao
interface TestSessionDao : BaseDao<TestSession>{

    /**
     * Vrátí id posledního přidaného prvku
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun add(testSession : TestSession)

    @Query("DELETE FROM " + MyDatabase.testSessionsTableName)
    suspend fun deleteAllTestSessions()

    @Query("SELECT * FROM " + MyDatabase.testSessionsTableName)
    fun getLiveData(): LiveData<List<TestSession>>

    @Query("SELECT * FROM ${MyDatabase.testSessionsTableName} WHERE status != :filterByStatus")
    fun getFlow(filterByStatus: DataStatus = DataStatus.REMOVED): Flow<List<TestSession>>

    @Query("SELECT * FROM ${MyDatabase.testSessionsTableName} WHERE id = :id LIMIT 1")
    suspend fun getTest(id: String): TestSession?
    @Query("SELECT * FROM ${MyDatabase.testSessionsTableName} WHERE status = :status")
    suspend fun getDataByStatus(status: DataStatus = DataStatus.OFFLINE): List<TestSession>

}