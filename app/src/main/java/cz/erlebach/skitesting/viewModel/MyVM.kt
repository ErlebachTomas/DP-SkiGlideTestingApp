package cz.erlebach.skitesting.viewModel

import androidx.lifecycle.viewModelScope
import cz.erlebach.skitesting.repository.MyRepository
import kotlinx.coroutines.launch

class MyVM(private val repo: MyRepository): VM(repo) {

    /** Vymaže všechny lyže uživatele */
    fun deleteAll() {
        viewModelScope.launch {
            repo.deleteAll()
        }
    }



}