package cz.erlebach.skitesting.viewModel.remote
import androidx.lifecycle.viewModelScope
import cz.erlebach.skitesting.common.interfaces.IAccountManagement
import cz.erlebach.skitesting.model.Ski
import cz.erlebach.skitesting.repository.remote.SkiRemoteRepository
import kotlinx.coroutines.launch

class SkiRemoteVM(val repository: SkiRemoteRepository, private val account : IAccountManagement) : GenericRemoteServerVM<Ski>(repository,account) {

    /**
     * @throws IllegalStateException
     */
    init {
        fetchData()
    }

    /** Vymaže všechny lyže uživatele */
    fun deleteAll() {
        viewModelScope.launch {
            repository.deleteAll(account.getUserID())
        }
    }


}