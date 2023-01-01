package cz.erlebach.skitesting.repository

import android.content.Context
import cz.erlebach.skitesting.network.RetrofitApiService
import cz.erlebach.skitesting.network.TestData
import retrofit2.Call
import retrofit2.Response

class RemoteServerRepository(val context: Context) {

    // WEB call --------------------------------
    suspend fun testPost(testData: TestData): Response<TestData> {
        return RetrofitApiService(this.context).skiTestingServerAPI.testPost(testData)
    }

    suspend fun testGet(): Call<TestData> {
        return RetrofitApiService(this.context).skiTestingServerAPI.getTestData()
    }
    suspend fun testGet2(): Response<TestData> {
        return RetrofitApiService(this.context).skiTestingServerAPI.getTestData2()
    }


}