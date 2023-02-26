package cz.erlebach.skitesting.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import cz.erlebach.skitesting.model.BaseModel
import cz.erlebach.skitesting.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


open class VM (val repository: Repository): ViewModel() {

    val data get() = repository.readAllData.asLiveData()

    fun insert(entity: BaseModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(entity)
        }
    }
    fun update(entity: BaseModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.update(entity)
        }
    }
    fun delete(entity: BaseModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(entity)
        }
    }
}