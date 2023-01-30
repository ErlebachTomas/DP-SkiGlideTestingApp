package cz.erlebach.skitesting.repository.remote

import android.content.Context
import androidx.lifecycle.ViewModel
import cz.erlebach.skitesting.common.interfaces.IRemoteServerRepository
import cz.erlebach.skitesting.model.Ski
import cz.erlebach.skitesting.model.TestSession
import cz.erlebach.skitesting.network.RetrofitApiService
import cz.erlebach.skitesting.network.TestData
import retrofit2.Call
import retrofit2.Response

class SkiRemoteRepository(val context: Context)
    : IRemoteServerRepository<Ski>
    {
    private val api = RetrofitApiService(this.context).skiTestingServerAPI


    override suspend fun getAllData(userID: String): Response<List<Ski>> {
        return api.getAllUsersSki(userID)
    }


}