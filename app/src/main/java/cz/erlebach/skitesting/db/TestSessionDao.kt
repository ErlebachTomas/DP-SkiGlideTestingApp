package cz.erlebach.skitesting.db

import androidx.lifecycle.LiveData
import androidx.room.*
import cz.erlebach.skitesting.MyDatabase
import cz.erlebach.skitesting.model.SkiRide
import cz.erlebach.skitesting.model.TestSession

/** Toto rozhraní definuje standardní operace, které se dají provádět s tabulkou [TestSession] */
@Dao
interface TestSessionDao : BaseDao<TestSession>{

    /**
     * Vrátí id posledního přidaného prvku
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE) //todo předělat pak
    suspend fun add(testSession : TestSession)

    @Query("DELETE FROM " + MyDatabase.testSessionsTableName)
    suspend fun deleteAllTestSessions()

    @Query("SELECT * FROM " + MyDatabase.testSessionsTableName)
    fun getLiveData(): LiveData<List<TestSession>>
}