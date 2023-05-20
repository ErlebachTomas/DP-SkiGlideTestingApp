package cz.erlebach.skitesting.common.template

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Výchozí ViewModelProvider může instancovat pouze ViewModel bez parametrů v konstruktoru
 */
class MyViewModelFactory<R: ViewModel>(private val repository: R): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    @SuppressWarnings("unchecked")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(repository.javaClass)) {
            repository as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}

