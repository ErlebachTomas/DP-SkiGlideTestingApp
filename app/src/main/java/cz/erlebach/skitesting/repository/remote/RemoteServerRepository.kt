package cz.erlebach.skitesting.repository.remote

import android.content.Context
import cz.erlebach.skitesting.network.RetrofitApiService
import cz.erlebach.skitesting.network.model.TestDataBody
import retrofit2.Response

class RemoteServerRepository(val context: Context) {

    private val api = RetrofitApiService(this.context).skiTestingServerAPI

    // WEB call --------------------------------
    suspend fun testPost(testDataBody: TestDataBody): Response<TestDataBody> {
        return api.testPost(testDataBody)
    }

    suspend fun testGet2(): Response<TestDataBody> {
        return api.getTestData2()
    }




}