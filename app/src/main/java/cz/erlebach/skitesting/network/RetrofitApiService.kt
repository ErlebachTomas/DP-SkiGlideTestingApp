package cz.erlebach.skitesting.network

import android.content.Context
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Retrofit instance class, poskytuje API
 */
class RetrofitApiService(context: Context) {

    companion object {
      /** URL API */
      // const val BASE_URL = "http://skitest.nti.tul.cz:3000"
     const val BASE_URL = "https://1572-2a00-1028-83ca-8026-fda9-f10-c74f-c9e0.eu.ngrok.io"
    }

    private val client = OkHttpClient.Builder().apply {
        addInterceptor(Auth0Interceptor(context))
    }.build()

    /**  Retrofit instance */
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /** Poskytuje [IWebApi] API interface  */
    val skiTestingServerAPI: IWebApi by lazy {
        retrofit.create(IWebApi::class.java)
    }

    // + pripadne dalsi API
}

