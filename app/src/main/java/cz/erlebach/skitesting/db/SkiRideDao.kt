package cz.erlebach.skitesting.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import cz.erlebach.skitesting.MyDatabase
import cz.erlebach.skitesting.model.SkiRide
/** Toto rozhraní definuje standardní operace, které se dají provádět s tabulkou [SkiRide]  */
@Dao
interface SkiRideDao : BaseDao<SkiRide> {

    @Query("DELETE FROM " + MyDatabase.skiRideTableName)
    suspend fun deleteAllSkiRides()

    @Query("SELECT * FROM " + MyDatabase.skiRideTableName)
    fun getLiveData(): LiveData<List<SkiRide>>

    @Query("SELECT * FROM " + MyDatabase.skiRideTableName + " WHERE testSessionID = :testID")
    fun loadTestSessionRideByID(testID: Long): LiveData<List<SkiRide>>
}