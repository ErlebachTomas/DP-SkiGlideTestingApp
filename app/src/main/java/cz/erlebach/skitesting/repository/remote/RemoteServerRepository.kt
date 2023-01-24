package cz.erlebach.skitesting.repository.remote

import android.content.Context
import cz.erlebach.skitesting.network.RetrofitApiService
import cz.erlebach.skitesting.network.TestData
import retrofit2.Response

class RemoteServerRepository(val context: Context) {

    private val api = RetrofitApiService(this.context).skiTestingServerAPI

    // WEB call --------------------------------
    suspend fun testPost(testData: TestData): Response<TestData> {
        return api.testPost(testData)
    }

    suspend fun testGet2(): Response<TestData> {
        return api.getTestData2()
    }




}