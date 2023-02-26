package cz.erlebach.skitesting.repository.local


import androidx.lifecycle.LiveData
import cz.erlebach.skitesting.db.SkiDao
import cz.erlebach.skitesting.model.BaseModel
import cz.erlebach.skitesting.model.Ski
import kotlinx.coroutines.flow.Flow

class SkiLocalRepository(private val skiDao: SkiDao): BaseRepository<Ski>(skiDao) {

    val readAllData: LiveData<List<Ski>> = skiDao.getLiveData()
    val getDataFlow: Flow<List<Ski>> = skiDao.getFlow()

    suspend fun addSki(ski:Ski){
        skiDao.addSki(ski)
    }

    suspend fun updateSki(ski:Ski){
        skiDao.updateSki(ski)
    }

    suspend fun deleteSki(ski:Ski){
        skiDao.deleteSki(ski)
    }

    suspend fun deleteAll(){
        skiDao.deleteAllSkis()
    }

}