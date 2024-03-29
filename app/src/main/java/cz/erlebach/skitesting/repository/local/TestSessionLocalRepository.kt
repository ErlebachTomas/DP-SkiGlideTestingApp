package cz.erlebach.skitesting.repository.local

import androidx.lifecycle.LiveData
import cz.erlebach.skitesting.db.TestSessionDao
import cz.erlebach.skitesting.model.TestSession

class TestSessionLocalRepository(private val testSessionDao: TestSessionDao) : LocalBaseRepository<TestSession>(testSessionDao){

    val readAllData: LiveData<List<TestSession>> = testSessionDao.getLiveData()

    suspend fun add(testSession:TestSession): String {
        testSessionDao.add(testSession) // pro autogenerate long jen return
        return testSession.id
    }

    suspend fun deleteAll(){
        testSessionDao.deleteAllTestSessions()
    }
}
