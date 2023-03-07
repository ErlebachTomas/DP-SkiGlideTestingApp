package cz.erlebach.skitesting.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import cz.erlebach.skitesting.model.Ski
import cz.erlebach.skitesting.model.SkiRide
import cz.erlebach.skitesting.repository.SkiRepository
import cz.erlebach.skitesting.repository.SkiRideRepository
import cz.erlebach.skitesting.repository.TestSessionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class SkiRideVM(private val repo: SkiRideRepository): BaseVM(repo) {

    /**
     * Načte všechny provedené jízdy z testu
     * @param testID [String] id testu
     * @return [LiveData] jízdy testu
     */
    fun loadTestSessionRideByID(idTestSession: String): LiveData<List<SkiRide>> {
        return repo.loadTestSessionRideByID(idTestSession)
    }

    suspend fun getSki(skiRide: SkiRide): Ski? {
      return repo.getSki(skiRide)

    }

}