package cz.erlebach.skitesting.repository.local

import androidx.lifecycle.LiveData
import cz.erlebach.skitesting.common.utils.dataStatus.DataStatus
import cz.erlebach.skitesting.db.SkiRideDao
import cz.erlebach.skitesting.model.SkiRide
import cz.erlebach.skitesting.model.wrappers.SkiRideWithSki
import kotlinx.coroutines.flow.Flow


class SkiRideLocalRepository(private val skiRideDao: SkiRideDao) : LocalBaseRepository<SkiRide>(skiRideDao) {

    val readAllData: LiveData<List<SkiRide>> = skiRideDao.getLiveData()

    suspend fun deleteAll() {
        skiRideDao.deleteAllSkiRides()
    }

     fun loadTestSessionRideByID(testID: String): LiveData<List<SkiRide>> {
        return skiRideDao.loadTestSessionRideByID(testID)
    }
    /** Vrátí data i s informací o lyžích */
    fun getJoinedSkiRidesForTest(testID: String, filterStatus: DataStatus = DataStatus.REMOVED): Flow<List<SkiRideWithSki>> {
        return skiRideDao.getJoinedSkiRidesForTestSession(testID, filterStatus)
    }


}