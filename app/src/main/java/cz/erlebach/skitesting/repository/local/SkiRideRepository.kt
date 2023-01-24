package cz.erlebach.skitesting.repository.local

import androidx.lifecycle.LiveData
import cz.erlebach.skitesting.db.SkiRideDao
import cz.erlebach.skitesting.model.SkiRide


class SkiRideRepository(private val skiRideDao: SkiRideDao) : BaseRepository<SkiRide>(skiRideDao) {

    val readAllData: LiveData<List<SkiRide>> = skiRideDao.getLiveData()

    suspend fun deleteAll() {
        skiRideDao.deleteAllSkiRides()
    }

     fun loadTestSessionRideByID(testID: Long): LiveData<List<SkiRide>> {
        return skiRideDao.loadTestSessionRideByID(testID)
    }



}