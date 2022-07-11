package cz.erlebach.skitesting.db

import androidx.lifecycle.LiveData
import androidx.room.*
import cz.erlebach.skitesting.MyDatabase
import cz.erlebach.skitesting.model.Ski
/** Dao = Data Access Object */
@Dao
interface SkiDao {

    //@Query("SELECT * FROM skiTable")
    @Query("SELECT * FROM ${MyDatabase.skiTableName}")
    fun getLiveData(): LiveData<List<Ski>>

    // todo Strategy
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addSki(ski: Ski)

    @Update
    suspend fun updateSki(ski: Ski)

    @Delete
    suspend fun deleteSki(ski: Ski)
}