package cz.erlebach.skitesting.viewModel.local

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import cz.erlebach.skitesting.MyDatabase
import cz.erlebach.skitesting.model.SkiRide
import cz.erlebach.skitesting.repository.local.BaseRepository
import cz.erlebach.skitesting.repository.local.SkiRideLocalRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Repozitář pro testovací jízdy
 */
class SkiRideLocalVM(application: Application) : BaseLocalVM<SkiRide>(application) {

    private val _repository: SkiRideLocalRepository
    val readAllData: LiveData<List<SkiRide>>

    init {

        val skiRideDao = MyDatabase.getDatabase(
            application
        ).skiRideDao()

        _repository = SkiRideLocalRepository(skiRideDao)
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
     * @param testID [String] id testu
     * @return [LiveData] jízdy testu
     */
    fun loadTestSessionRideByID(testID: String) : LiveData<List<SkiRide>> {
        return _repository.loadTestSessionRideByID(testID)
    }

}