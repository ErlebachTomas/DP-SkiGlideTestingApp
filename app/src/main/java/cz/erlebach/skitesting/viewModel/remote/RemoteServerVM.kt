package cz.erlebach.skitesting.viewModel.remote

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.erlebach.skitesting.model.TestSession
import cz.erlebach.skitesting.network.model.DemoDataBody
import cz.erlebach.skitesting.network.model.recomendation.RecommendationDataBody
import cz.erlebach.skitesting.repository.remote.RemoteServerRepository
import kotlinx.coroutines.launch
import retrofit2.Response


class RemoteServerVM(private val repository: RemoteServerRepository): ViewModel() {

   var demoLiveData : MutableLiveData<Response<DemoDataBody>> = MutableLiveData()

    var recommendationLiveData : MutableLiveData<Response<List<RecommendationDataBody>>> = MutableLiveData()

    fun calculateRecommendation(test: TestSession) {
        viewModelScope.launch {
            recommendationLiveData.value = repository.recomendacion(test)
        }
    }


    /* ------------------------ */
    fun testPost(demoDataBody: DemoDataBody) {
        viewModelScope.launch {
            repository.testPost(demoDataBody)
        }
    }

    fun get2Response() {
        viewModelScope.launch {
            demoLiveData.value = repository.testGet2()
        }
    }

}