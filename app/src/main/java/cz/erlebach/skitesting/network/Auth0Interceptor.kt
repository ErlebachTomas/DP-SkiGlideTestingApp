package cz.erlebach.skitesting.network

import android.content.Context
import cz.erlebach.skitesting.common.SessionManager
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Interceptor slouží pro přidání Auth0 tokenu do každého requestu
 */
class Auth0Interceptor(context: Context) : Interceptor {
    private val sessionManager = SessionManager(context)

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()


        sessionManager.fetchAuthToken()?.let {
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }

        return chain.proceed(requestBuilder.build())
    }
}