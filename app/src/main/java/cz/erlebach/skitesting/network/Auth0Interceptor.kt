package cz.erlebach.skitesting.network

import android.content.Context
import com.auth0.android.Auth0
import cz.erlebach.skitesting.common.SessionManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Interceptor slouží pro přidání Auth0 tokenu do hlavičky každého requestu
 */
class Auth0Interceptor(context: Context) : Interceptor {

    private val sessionManager = SessionManager.getInstance(context)

    /** Přidá Bearer token do hlavičky requestu,
     * implementace ověřování tokenu a obnovení tokenu v retrofitu pomocí [sessionManager] a coroutine */
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        runBlocking {
            val token = sessionManager.fetchAuthToken()
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }
        return chain.proceed(requestBuilder.build())
    }
}