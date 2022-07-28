package cz.erlebach.skitesting.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import cz.erlebach.skitesting.MyDatabase
import cz.erlebach.skitesting.model.TestSession
import cz.erlebach.skitesting.repository.TestSessionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TestSessionVM (application: Application): AndroidViewModel(application)  {

    private val repository: TestSessionRepository

    val readAllData: LiveData<List<TestSession>>

    init {

        val dao = MyDatabase.getDatabase(
            application
        ).testSessionDao()

        repository = TestSessionRepository(dao)

        readAllData = repository.readAllData
    }


    suspend fun add(TestSession: TestSession): Long = withContext(Dispatchers.IO) {
        repository.add(TestSession)
    }



    fun update(TestSession: TestSession){
        viewModelScope.launch(Dispatchers.IO) {
            repository.update(TestSession)
        }
    }

    fun delete(TestSession: TestSession){
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(TestSession)
        }
    }
}