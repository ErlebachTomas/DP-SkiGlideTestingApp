package cz.erlebach.skitesting.viewModel.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.erlebach.skitesting.common.interfaces.IAccountManagement
import cz.erlebach.skitesting.common.interfaces.IRemoteServerRepository
import cz.erlebach.skitesting.common.utils.err
import cz.erlebach.skitesting.common.utils.lg
import kotlinx.coroutines.launch
import retrofit2.Response


/**
Obecná rozšířená třída [ViewModel], která používá repozitář implementující rozhraní [IRemoteServerRepository]
pro načítání dat. ViewModel jako argument konstruktoru přijímá objekt konkrétního repozitáře
 */
open class GenericRemoteServerVM<T>(private val repository: IRemoteServerRepository<T>, private val account: IAccountManagement): ViewModel() {

    protected var responseData : MutableLiveData<Response<List<T>>> = MutableLiveData()

    val data: LiveData<Response<List<T>>>
        get() = responseData

    /**
     * Načte data ze serveru
     * @throws IllegalStateException když se načtení nezdaří
     */
     fun fetchData() {
        viewModelScope.launch {

            val response = repository.getAllData(account.getUserID())
            if (response.isSuccessful) {
                responseData.value = response
            } else {
                err("Fetching data failed")
                err(response.code().toString())
                err(response.errorBody().toString())
                throw IllegalStateException("Response: " + response.code())
            }
        }
    }

    fun insert(obj: T) {
        viewModelScope.launch {
            val id = account.getUserID()
            lg("insert: $id")
            repository.insert(id, obj)
        }
    }
    fun update( obj: T) {
        viewModelScope.launch {
            repository.update(account.getUserID(), obj)
        }
    }
    fun delete( obj: T) {
        viewModelScope.launch {
            repository.delete(account.getUserID(), obj)
        }
    }

}