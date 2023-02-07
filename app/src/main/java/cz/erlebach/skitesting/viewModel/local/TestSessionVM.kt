package cz.erlebach.skitesting.viewModel.local

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import cz.erlebach.skitesting.MyDatabase
import cz.erlebach.skitesting.model.TestSession
import cz.erlebach.skitesting.repository.local.TestSessionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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


    suspend fun add(TestSession: TestSession): String {
        return repository.add(TestSession)
    }

    fun insert(TestSession: TestSession) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(TestSession)
        }
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