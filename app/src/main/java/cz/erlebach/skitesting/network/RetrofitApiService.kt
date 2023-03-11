package cz.erlebach.skitesting.network

import android.content.Context
import cz.erlebach.skitesting.BuildConfig
import cz.erlebach.skitesting.model.SkiRide
import cz.erlebach.skitesting.network.api.SkiAPI
import cz.erlebach.skitesting.network.api.SkiRideAPI
import cz.erlebach.skitesting.network.api.TestSessionAPI
import cz.erlebach.skitesting.network.api.WebAPI
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
              val baseUrl = "https://9d44-2a00-1028-83ca-8026-a1fd-be0d-28e6-9e3c.eu.ngrok.io"
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

    /** Poskytuje [WebAPI] API interface  */
    val skiTestingServerAPI: WebAPI by lazy {
        retrofit.create(WebAPI::class.java)
    }

    /** Poskytuje [SkiAPI] API interface  */
    val skiAPI: SkiAPI by lazy {
        retrofit.create(SkiAPI::class.java)
    }

    /** Poskytuje [SkiAPI] API interface  */
    val testSessionAPI: TestSessionAPI by lazy {
        retrofit.create(TestSessionAPI::class.java)
    }
    /** Poskytuje [SkiAPI] API interface  */
    val skiRideAPI: SkiRideAPI by lazy {
        retrofit.create(SkiRideAPI::class.java)
    }
    /** továrna */
    fun <T> apiFactory(clas: Class<T>): T {
        return retrofit.create(clas)

    }



    // + pripadne dalsi API
}


