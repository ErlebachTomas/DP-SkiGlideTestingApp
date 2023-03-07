package cz.erlebach.skitesting.repository.local

import androidx.lifecycle.LiveData
import cz.erlebach.skitesting.db.SkiRideDao
import cz.erlebach.skitesting.model.SkiRide


class SkiRideLocalRepository(private val skiRideDao: SkiRideDao) : BaseRepository<SkiRide>(skiRideDao) {

    val readAllData: LiveData<List<SkiRide>> = skiRideDao.getLiveData()

    suspend fun deleteAll() {
        skiRideDao.deleteAllSkiRides()
    }

     fun loadTestSessionRideByID(testID: String): LiveData<List<SkiRide>> {
        return skiRideDao.loadTestSessionRideByID(testID)
    }



}