package cz.erlebach.skitesting.network

import android.content.Context
import cz.erlebach.skitesting.BuildConfig
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

      /** URL API
       * @see gradle.properties
       * */

      val URL: String get() {
          return if(!BuildConfig.TEST_MODE_ENABLED) {
              "${BuildConfig.SERVER_URL}${BuildConfig.API_VERSION}"
          } else {
              val baseUrl = "https://7c03-2001-718-1c01-152-3923-7a3d-bcf3-d0fa.eu.ngrok.io"
              "$baseUrl/api/"
          }
      }

      /*
      const val BASE_URL = "https://73ce-2001-718-1c01-152-2c96-5ee0-8de5-1be2.eu.ngrok.io"
      const val URL = "$BASE_URL/api/"
      */

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
    /** továrna */
    fun <T> apiFactory(clas: Class<T>): T {
        return retrofit.create(clas)

    }



    // + pripadne dalsi API
}


