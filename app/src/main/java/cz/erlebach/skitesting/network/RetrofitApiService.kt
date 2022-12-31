package cz.erlebach.skitesting.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RetrofitApiService {

    companion object {
        /**
         * URL API */
      // const val BASE_URL = "http://skitest.nti.tul.cz:3000"
     const val BASE_URL = "https://b92b-2a00-1028-83ca-8026-a433-92b2-c470-bf2b.eu.ngrok.io/"
    }

    /**  Retrofit instance */
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /** Poskytuje [IWebApi] API interface  */
    val skiTestingServerAPI: IWebApi by lazy {
        retrofit.create(IWebApi::class.java)
    }

}


