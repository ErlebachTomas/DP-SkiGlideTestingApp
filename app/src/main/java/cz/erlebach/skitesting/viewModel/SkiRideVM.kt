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

    fun deleteAll(){
        viewModelScope.launch(Dispatchers.IO) {
            _repository.deleteAll()
        }
    }

}