package cz.erlebach.skitesting.repository.remote

import android.content.Context
import cz.erlebach.skitesting.common.SessionManager
import cz.erlebach.skitesting.common.interfaces.IAccountManagement
import cz.erlebach.skitesting.model.TestSession
import cz.erlebach.skitesting.network.RetrofitApiService
import cz.erlebach.skitesting.network.model.DemoDataBody
import cz.erlebach.skitesting.network.model.GeneralDataBody
import cz.erlebach.skitesting.network.model.recomendation.RecommendationDataBody
import retrofit2.Response

class RemoteServerRepository(val context: Context) {

    private val api = RetrofitApiService(this.context).skiTestingServerAPI
    val account: IAccountManagement = SessionManager.getInstance(context)

    /** doporučení lyží */
    suspend fun recomendacion(test: TestSession): Response<List<RecommendationDataBody>> {
        return api.recomendacion(GeneralDataBody(account.getUserID(), test))
    }

    // demo WEB call --------------------------------
    suspend fun testPost(demoDataBody: DemoDataBody): Response<DemoDataBody> {
        return api.testPost(demoDataBody)
    }

    suspend fun testGet2(): Response<DemoDataBody> {
        return api.getTestData2()
    }




}