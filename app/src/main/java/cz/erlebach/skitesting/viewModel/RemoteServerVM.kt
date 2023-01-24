package cz.erlebach.skitesting.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.erlebach.skitesting.network.TestData
import cz.erlebach.skitesting.repository.remote.RemoteServerRepository
import kotlinx.coroutines.launch
import retrofit2.Response


class RemoteServerVM(private val repository: RemoteServerRepository): ViewModel() {

   var res2 : MutableLiveData<Response<TestData>> = MutableLiveData()

    fun testPost(testData: TestData) {
        viewModelScope.launch {
            repository.testPost(testData)
        }
    }

    fun get2Response() {
        viewModelScope.launch {
            res2.value = repository.testGet2()
        }
    }

}