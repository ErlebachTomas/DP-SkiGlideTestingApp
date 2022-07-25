package cz.erlebach.skitesting.activity

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.snackbar.Snackbar
import cz.erlebach.skitesting.R
import cz.erlebach.skitesting.databinding.ActivityTestBinding
import cz.erlebach.skitesting.network.ApiService
import cz.erlebach.skitesting.network.IWebApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory

class TestActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityTestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_test)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.fab.setOnClickListener { view ->

            val BASE_URL = "https://ski-glide-testing.herokuapp.com"

            Snackbar.make(view, "Call " + BASE_URL, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()

            // todo pokrocila implementace dle API, trida API client
            // test https://ski-glide-testing.herokuapp.com/api/data

                val api = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(IWebApi::class.java)

                // Corutines scope na aktivitu
                lifecycleScope.launch(Dispatchers.IO) {

                    try {
                        val response = api.getTestData().awaitResponse()
                        if (response.isSuccessful) {
                            val resData = response.body()!!
                            withContext(Dispatchers.Main) { // nahrada await

                                Log.v("API", resData.data) // ATRIBUT
                                Toast.makeText(applicationContext,resData.data,Toast.LENGTH_LONG).show()

                            }

                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main){
                            Toast.makeText(applicationContext, "nepodařilo se načíst data z Internetu, zkontrolujte připojení",Toast.LENGTH_LONG).show()
                            Log.e("API",e.message.toString())

                        }
                    }

                }


        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_test)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }


}