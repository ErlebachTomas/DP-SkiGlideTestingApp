package cz.erlebach.skitesting.activity

import android.os.Bundle
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
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TestActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityTestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_test)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()

            // todo pokrocila implementace dle API, trida API client
                // test https://ski-glide-testing.herokuapp.com/api/data

                val BASE_URL = "https://ski-glide-testing.herokuapp.com"

                val api = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(IWebApi::class.java)

                val service = ApiService()
                // Corutines scope na aktivitu
                lifecycleScope.launch(Dispatchers.IO) {

                    service.displayTestData(api)
                }


        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_test)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }


}