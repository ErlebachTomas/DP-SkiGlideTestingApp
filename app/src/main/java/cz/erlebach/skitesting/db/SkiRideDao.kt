package cz.erlebach.skitesting.db

import androidx.lifecycle.LiveData
import androidx.room.Query
import cz.erlebach.skitesting.MyDatabase
import cz.erlebach.skitesting.model.SkiRide

interface SkiRideDao : BaseDao<SkiRideDao> {

    @Query("DELETE FROM " + MyDatabase.skiRideTableName)
    suspend fun deleteAllSkiRides()

    @Query("SELECT * FROM " + MyDatabase.skiRideTableName)
    fun getLiveData(): LiveData<List<SkiRide>>

}