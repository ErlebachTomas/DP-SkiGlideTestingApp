package cz.erlebach.skitesting.network

import android.content.Context
import cz.erlebach.skitesting.R
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

    const val BASE_URL = "https://5f6c-2001-718-1c01-152-4143-7712-b3ff-ef26.eu.ngrok.io"



      const val URL = "$BASE_URL/api/"
    }

    private val client = OkHttpClient.Builder().apply {
        addInterceptor(Auth0Interceptor(context))
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

    // + pripadne dalsi API
}


