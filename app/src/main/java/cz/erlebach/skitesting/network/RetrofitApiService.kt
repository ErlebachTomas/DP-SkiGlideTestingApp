package cz.erlebach.skitesting.network

import android.content.Context
import cz.erlebach.skitesting.network.api.ISkiAPI
import cz.erlebach.skitesting.network.api.IWebApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Retrofit instance class, poskytuje API
 */
class RetrofitApiService(context: Context) {

    companion object {
      /** URL API */ // undone načítat ze settings
   // const val BASE_URL = "http://skitest.nti.tul.cz:1337"

    const val BASE_URL = "https://73ce-2001-718-1c01-152-2c96-5ee0-8de5-1be2.eu.ngrok.io"
    const val URL = "$BASE_URL/api/"

    }

    private val client = OkHttpClient.Builder().apply {
        addInterceptor(Auth0Interceptor(context))
        retryOnConnectionFailure(true) // pravděpodobné řešení IOException: unexpected end of stream on
    }.build()

    /**  Retrofit instance */
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /** Poskytuje [IWebApi] API interface  */
    val skiTestingServerAPI: IWebApi by lazy {
        retrofit.create(IWebApi::class.java)
    }

    /** Poskytuje [ISkiAPI] API interface  */
    val skiAPI: ISkiAPI by lazy {
        retrofit.create(ISkiAPI::class.java)
    }



    // + pripadne dalsi API
}


