package cz.erlebach.skitesting.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import cz.erlebach.skitesting.MyDatabase
import cz.erlebach.skitesting.model.Ski
import cz.erlebach.skitesting.repository.SkiRepository
import cz.erlebach.skitesting.repository.local.SkiLocalRepository
import kotlinx.coroutines.launch

class SkiVM(private val repo: SkiRepository): BaseVM(repo) {

    /** Vrátí seznam lyží uživatele */
    val userSkis: LiveData<List<Ski>> get() =  repo.userSki

    /** Vymaže všechny lyže uživatele */
    fun deleteAll() {
        viewModelScope.launch {
            repo.deleteAll()
        }
    }






}