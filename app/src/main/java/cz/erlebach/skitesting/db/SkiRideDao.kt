package cz.erlebach.skitesting.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import cz.erlebach.skitesting.MyDatabase
import cz.erlebach.skitesting.common.utils.dataStatus.DataStatus
import cz.erlebach.skitesting.model.SkiRide
import cz.erlebach.skitesting.model.wrappers.SkiRideWithSki
import kotlinx.coroutines.flow.Flow

/** Toto rozhraní definuje standardní operace, které se dají provádět s tabulkou [SkiRide]  */
@Dao
interface SkiRideDao : BaseDao<SkiRide> {

    @Query("DELETE FROM " + MyDatabase.skiRideTableName)
    suspend fun deleteAllSkiRides()

    @Query("SELECT * FROM " + MyDatabase.skiRideTableName)
    fun getLiveData(): LiveData<List<SkiRide>>

    @Query("SELECT * FROM " + MyDatabase.skiRideTableName + " WHERE testSessionID = :testID AND status != :filterStatus")
    fun loadTestSessionRideByID(testID: String, filterStatus: DataStatus = DataStatus.REMOVED): LiveData<List<SkiRide>>

    @Query("SELECT * FROM ${MyDatabase.skiRideTableName} WHERE status != :filterStatus")
    fun getFlow(filterStatus: DataStatus = DataStatus.REMOVED): Flow<List<SkiRide>>

    @Query("SELECT * FROM ${MyDatabase.skiRideTableName} WHERE id = :id LIMIT 1")
    suspend fun getTest(id: String): SkiRide?

    @Query("SELECT * FROM ${MyDatabase.skiRideTableName} WHERE status = :status")
    suspend fun getDataByStatus(status: DataStatus = DataStatus.OFFLINE): List<SkiRide>

    @Transaction
    @Query("""
        SELECT * FROM ${MyDatabase.skiRideTableName}
        INNER JOIN ${MyDatabase.skiTableName} ON ${MyDatabase.skiRideTableName}.skiID = ${MyDatabase.skiTableName}.id
        WHERE ${MyDatabase.skiRideTableName}.status != :filterStatus 
        AND ${MyDatabase.skiRideTableName}.testSessionID = :testID
    """)
    fun getJoinedSkiRidesForTestSession(testID: String, filterStatus: DataStatus = DataStatus.REMOVED): Flow<List<SkiRideWithSki>>

}