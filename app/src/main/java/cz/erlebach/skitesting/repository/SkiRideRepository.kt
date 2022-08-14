package cz.erlebach.skitesting.repository

import androidx.lifecycle.LiveData
import cz.erlebach.skitesting.db.SkiRideDao
import cz.erlebach.skitesting.model.SkiRide


class SkiRideRepository(private val skiRideDao: SkiRideDao) : BaseRepository<SkiRideDao>(skiRideDao) {

    val readAllData: LiveData<List<SkiRide>> = skiRideDao.getLiveData()

    suspend fun deleteAll() {
        skiRideDao.deleteAllSkiRides()
    }

}