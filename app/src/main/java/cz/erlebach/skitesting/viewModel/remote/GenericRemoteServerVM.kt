package cz.erlebach.skitesting.viewModel.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.erlebach.skitesting.common.interfaces.IRemoteServerRepository
import cz.erlebach.skitesting.repository.remote.SkiRemoteRepository
import cz.erlebach.skitesting.utils.err
import kotlinx.coroutines.launch
import retrofit2.Response


/**
Obecná rozšířená třída [ViewModel], která používá repozitář implementující rozhraní [IRemoteServerRepository]
pro načítání dat. ViewModel jako argument konstruktoru přijímá objekt konkrétního repozitáře
 */
open class GenericRemoteServerVM<T>(private val repository: IRemoteServerRepository<T>): ViewModel() {

    protected var responseData : MutableLiveData<Response<T>> = MutableLiveData()

    val data: LiveData<Response<T>>
        get() = responseData

     fun fetchData() {
        viewModelScope.launch {
            val response = repository.getAllData()
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

    // todo ostatni crud a LiveData<Response<List<T>> ?

}