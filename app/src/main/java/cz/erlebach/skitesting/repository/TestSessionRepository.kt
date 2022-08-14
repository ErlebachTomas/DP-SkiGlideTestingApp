package cz.erlebach.skitesting.repository

import androidx.lifecycle.LiveData
import cz.erlebach.skitesting.db.TestSessionDao
import cz.erlebach.skitesting.model.TestSession

class TestSessionRepository(private val testSessionDao: TestSessionDao){

    val readAllData: LiveData<List<TestSession>> = testSessionDao.getLiveData()

    suspend fun add(testSession:TestSession): Long{
        return testSessionDao.add(testSession)
    }

    suspend fun insert(testSession:TestSession) {
        return testSessionDao.insert(testSession)
    }

    suspend fun update(testSession:TestSession){
        testSessionDao.update(testSession)
    }

    suspend fun delete(testSession:TestSession){
        testSessionDao.delete(testSession)
    }

    suspend fun deleteAll(){
        testSessionDao.deleteAllTestSessions()
    }
}
