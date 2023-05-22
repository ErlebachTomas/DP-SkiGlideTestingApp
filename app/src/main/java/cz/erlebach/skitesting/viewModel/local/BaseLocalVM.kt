package cz.erlebach.skitesting.viewModel.local

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cz.erlebach.skitesting.repository.local.BaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * ViewModel je třída, která je zodpovědná za přípravu a správu dat pro Activity.
 * @param T model
 */
abstract class BaseLocalVM<T>(application: Application): AndroidViewModel(application)  {

    abstract val repository: BaseRepository<T>

    fun insert(entity: T) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(entity)
        }
    }
    fun update(entity: T) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.update(entity)
        }
    }
    fun delete(entity: T) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(entity)
        }
    }

}