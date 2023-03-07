package cz.erlebach.skitesting.viewModel.remote

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.erlebach.skitesting.network.model.TestDataBody
import cz.erlebach.skitesting.repository.remote.RemoteServerRepository
import kotlinx.coroutines.launch
import retrofit2.Response


class RemoteServerVM(private val repository: RemoteServerRepository): ViewModel() {

   var res2 : MutableLiveData<Response<TestDataBody>> = MutableLiveData()

    fun testPost(testDataBody: TestDataBody) {
        viewModelScope.launch {
            repository.testPost(testDataBody)
        }
    }

    fun get2Response() {
        viewModelScope.launch {
            res2.value = repository.testGet2()
        }
    }

}