package cz.erlebach.skitesting.network

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse

class ApiService {

    companion object {
        /**
         * URL API */
      // const val BASE_URL = "http://skitest.nti.tul.cz:3000"
     const val BASE_URL = "https://b92b-2a00-1028-83ca-8026-a433-92b2-c470-bf2b.eu.ngrok.io/"
    }
    /** Corutines backgroundTask, realizace načtení dat z API */
    public suspend fun displayTestData(api: IWebApi){

        try {
            val response = api.getTestData().awaitResponse()
            if (response.isSuccessful) {
                val resData = response.body()!!
                withContext(Dispatchers.Main) { // nahrada await
                    Log.v("API", resData.data) // ATRIBUT
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main){
                Log.e("API","nepodařilo se načíst data z Internetu, zkontrolujte připojení")
            }
        }
    }
}


