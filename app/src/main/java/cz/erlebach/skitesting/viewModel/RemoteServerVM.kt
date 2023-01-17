package cz.erlebach.skitesting.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.erlebach.skitesting.network.TestData
import cz.erlebach.skitesting.repository.RemoteServerRepository
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.awaitResponse

class RemoteServerVM(private val repository: RemoteServerRepository): ViewModel() {

   var res: MutableLiveData<TestData> = MutableLiveData()
   var res2 : MutableLiveData<Response<TestData>> = MutableLiveData() // TODO this

    fun testPost(testData: TestData) {
        viewModelScope.launch {
            repository.testPost(testData)
        }
    }

    fun get() {
        viewModelScope.launch {
             res.value = repository.testGet().awaitResponse().body()!!
        }
    }

    fun get2Response() {
        viewModelScope.launch {
            res2.value = repository.testGet2()
        }
    }

}