package cz.erlebach.skitesting.common.template

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cz.erlebach.skitesting.repository.RemoteServerRepository
import cz.erlebach.skitesting.viewModel.RemoteServerVM

/**
 * Výchozí ViewModelProvider může instancovat pouze ViewModel bez parametrů v konstruktoru.
 */
class MyViewModelFactory(private val repository: RemoteServerRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(RemoteServerVM::class.java)) {
           RemoteServerVM(this.repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}

