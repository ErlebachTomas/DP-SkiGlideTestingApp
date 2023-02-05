package cz.erlebach.skitesting.repository.local


import androidx.lifecycle.LiveData
import cz.erlebach.skitesting.db.SkiDao
import cz.erlebach.skitesting.model.Ski

class SkiRepository(private val skiDao: SkiDao) {

    val readAllData: LiveData<List<Ski>> = skiDao.getLiveData()

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