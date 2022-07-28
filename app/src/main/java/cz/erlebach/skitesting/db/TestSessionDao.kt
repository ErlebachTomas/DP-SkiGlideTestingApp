package cz.erlebach.skitesting.db

import androidx.lifecycle.LiveData
import androidx.room.*
import cz.erlebach.skitesting.MyDatabase
import cz.erlebach.skitesting.model.TestSession

@Dao
interface TestSessionDao : BaseDao<TestSession>{

    /**
     * Vrátí id posledního přidaného prvku
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE) //todo předělat pak
    suspend fun add(testSession : TestSession): Long

    @Query("DELETE FROM " + MyDatabase.testSessionsTableName)
    suspend fun deleteAllTestSessions()

    @Query("SELECT * FROM " + MyDatabase.testSessionsTableName)
    fun getLiveData(): LiveData<List<TestSession>>
}