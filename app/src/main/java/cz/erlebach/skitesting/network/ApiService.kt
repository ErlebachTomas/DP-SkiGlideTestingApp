package cz.erlebach.skitesting.network

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse

class ApiService {

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


