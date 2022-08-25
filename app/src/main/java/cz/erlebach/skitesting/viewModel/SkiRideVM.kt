package cz.erlebach.skitesting.viewModel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import cz.erlebach.skitesting.MyDatabase
import cz.erlebach.skitesting.model.SkiRide
import cz.erlebach.skitesting.repository.BaseRepository
import cz.erlebach.skitesting.repository.SkiRideRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Repozitář pro testovací jízdy
 */
class SkiRideVM(application: Application) : BaseVM<SkiRide>(application) {

    private val _repository: SkiRideRepository
    val readAllData: LiveData<List<SkiRide>>

    init {

        val skiRideDao = MyDatabase.getDatabase(
            application
        ).skiRideDao()

        _repository = SkiRideRepository(skiRideDao)
        readAllData = _repository.readAllData
    }

    override val repository: BaseRepository<SkiRide>
        get() = _repository

    /**
     * Smaže všechna data z repozitáře
     */
    fun deleteAll(){
        viewModelScope.launch(Dispatchers.IO) {
            _repository.deleteAll()
        }
    }

    /**
     * Načte všechny provedené jízdy z testu
     * @param testID [Long] id testu
     * @return [LiveData] jízdy testu
     */
    fun loadTestSessionRideByID(testID: Long) : LiveData<List<SkiRide>> {
        return _repository.loadTestSessionRideByID(testID)
    }

}