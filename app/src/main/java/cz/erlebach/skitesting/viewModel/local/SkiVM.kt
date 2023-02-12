package cz.erlebach.skitesting.viewModel.local

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import cz.erlebach.skitesting.MyDatabase
import cz.erlebach.skitesting.model.Ski
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import cz.erlebach.skitesting.repository.local.SkiLocalRepository

/**
 * ViewModel s využitím Kotlin coroutines
 */
class SkiVM(application: Application): AndroidViewModel(application)  {

    private val repository: SkiLocalRepository

    val readAllData: LiveData<List<Ski>>

    init {

        val skiDao = MyDatabase.getDatabase(
            application
        ).skiDao()

        repository = SkiLocalRepository(skiDao)

        readAllData = repository.readAllData
    }

    fun addSki(Ski: Ski){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addSki(Ski)
        }
    }

    fun updateSki(Ski: Ski){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateSki(Ski)
        }
    }

    fun deleteSki(Ski: Ski){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteSki(Ski)
        }
    }

    fun deleteAll(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll()
        }
    }
}