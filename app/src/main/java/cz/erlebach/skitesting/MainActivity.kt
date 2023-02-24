package cz.erlebach.skitesting

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.authentication.storage.CredentialsManager
import com.auth0.android.authentication.storage.CredentialsManagerException
import com.auth0.android.authentication.storage.SharedPreferencesStorage
import com.auth0.android.callback.Callback
import com.auth0.android.result.Credentials
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.authentication
import com.github.kittinunf.fuel.json.responseJson
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import cz.erlebach.skitesting.common.SessionManager
import cz.erlebach.skitesting.common.utils.debug
import cz.erlebach.skitesting.common.utils.err
import cz.erlebach.skitesting.common.utils.isDeviceOnline
import cz.erlebach.skitesting.common.utils.lg
import cz.erlebach.skitesting.databinding.ActivityMainBinding
import cz.erlebach.skitesting.fragments.HomeFragment
import cz.erlebach.skitesting.fragments.LoginFragment
import cz.erlebach.skitesting.fragments.NoConnectionFragment
import cz.erlebach.skitesting.model.Ski
import cz.erlebach.skitesting.network.RetrofitApiService
import cz.erlebach.skitesting.network.SyncWorker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {
    /**
     * Instance vazební třídy obsahující přímé odkazy (nahrazuje findViewById konstrukci)
     */
    private lateinit var binding: ActivityMainBinding
    private lateinit var authManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // val PACKAGE_NAME = applicationContext.packageName
        binding = ActivityMainBinding.inflate(layoutInflater) // metoda generující binding class

        this.authManager = SessionManager.getInstance(this)

        checkAllpermissions()

        if (!isDeviceOnline(this)) {
            changeFragmentTo(NoConnectionFragment()) // undone offline politika
        } else {
            if (authManager.checkIfloginIsValid()) {
                changeFragmentTo(HomeFragment())
            } else {
                lg("Authent")
                changeFragmentTo(LoginFragment())
            }
        }

    }

    /**
     * Realizace přihlášení do aplikace + login sesion pomocí shared pref
     */
    fun login() {
        authManager.login(object : Callback<Credentials, AuthenticationException> {
            override fun onFailure(error: AuthenticationException) {
                toast(getString(R.string.login_failure_message) + ": " + error.message)
                changeFragmentTo(NoConnectionFragment())
            }

            override fun onSuccess(result: Credentials) {
                changeFragmentTo(HomeFragment())
            }
        })

    }

    /**
     * Odhlášení
     */
    fun logout() {
        authManager.logout(object : Callback<Void?, AuthenticationException> {

            override fun onFailure(error: AuthenticationException) {
                toast(getString(R.string.logout_err_message) + error.getCode())
            }

            override fun onSuccess(result: Void?) {
                toast(getString(R.string.login_success_message)) // undone logout msg
                changeFragmentTo(LoginFragment())
            }
        })
    }


    /**
     * kontrola aktivniho přihlašeni
     */
    private fun checkIfloginInfoAlreadyExist(): Boolean {

        val auth0 = Auth0(getString(R.string.auth0_client_id), getString(R.string.auth0_domain))
        val authAPIClient = AuthenticationAPIClient(auth0)
        val sharedPrefStorage = SharedPreferencesStorage(this)
        val credentialsManager = CredentialsManager(authAPIClient, sharedPrefStorage)

        return credentialsManager.hasValidCredentials()
    }

    /**
     * Kontrola potřebných oprávnění
     */
    private fun checkAllpermissions() {

        val requestCode = 1

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
            != PackageManager.PERMISSION_GRANTED
        // || ...
        ) {
            lg("vyzadovano udeleni opravneni ")
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_NETWORK_STATE
                    // ..
                ),
                requestCode
            )
        } else {
            lg("opravneni udeleno")
        }

    }

    /* TOOLS */
    /**
     * Přepne fragment v hlavním okně na jiný fragment
     */
    private fun changeFragmentTo(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.ma_fragmentContainerView, fragment)
        transaction.commit()
    }

    /** info výpis na obrazovku */
    private fun showSnackBar(text: String) {
        Snackbar.make(binding.root, text, Snackbar.LENGTH_LONG).show()

    }

    /** Zobrazí toast */
    private fun toast(text: String, length: Int = Toast.LENGTH_SHORT) {
        lg(text)
        Toast.makeText(applicationContext, text, length).show()
    }

    /**
     * Jedinná funkční metoda na získání API tokenu
     */
    fun auth0TestConnection() {

        var token: String

        val auth0 = Auth0(getString(R.string.auth0_client_id), getString(R.string.auth0_domain))
        val authAPIClient = AuthenticationAPIClient(auth0)
        val sharedPrefStorage = SharedPreferencesStorage(this)

        val credentialsManager = CredentialsManager(authAPIClient, sharedPrefStorage)

        credentialsManager.getCredentials(object :
            Callback<Credentials, CredentialsManagerException> {

            override fun onFailure(error: CredentialsManagerException) {
                err(error.message.toString())
                toast(error.message.toString())
            }

            override fun onSuccess(result: Credentials) {

                token = result.accessToken
                lg("Access token retrieved")

                lifecycleScope.launch(Dispatchers.IO) {

                    val url = RetrofitApiService.URL + "getAllUsers"

                    Fuel.get(url)
                        .authentication()
                        .bearer(token)
                        .responseJson { _, _, result ->
                            result.fold(success = { json ->
                                lg("Access token work, retrieve:")
                                lg(json.array().toString())

                            }, failure = { error ->
                                err(error.toString())
                            })
                        }

                }

            }
        })

    }

    fun apiCall() {

        lifecycleScope.launch(Dispatchers.IO) {

            val url = RetrofitApiService.URL + "getAllUsers"

            Fuel.get(url)
                .authentication()
                .bearer(authManager.fetchAuthToken())
                .responseJson { _, _, result ->
                    result.fold(success = { json ->
                        lg("Access token work, retrieve:")
                        lg(json.array().toString())

                    }, failure = { error ->
                        err(error.toString())
                    })
                }
        }

    }

    /** Synchronizace dat */
    fun testButtonFunction() {
        testuju()


        //lg("Test button function")
        //val workManager = WorkManager.getInstance(this)

        /*
        val wr = OneTimeWorkRequestBuilder<MyWorker>().build()
        workManager.enqueue(wr)
        */

        //val syncWorkRequest = OneTimeWorkRequestBuilder<SyncWorker>().build()
        //workManager.enqueue(syncWorkRequest)

    }

    fun testuju() {
        val gson = Gson()
        val ski = Ski(name="test")
        val json = gson.toJson(ski)
        lg(json.toString())

        val context = this.applicationContext
        lifecycleScope.launch(Dispatchers.IO) {
            val id = SessionManager.getInstance(context).getUserID()
            val api = RetrofitApiService(context).skiAPI
            val response = api.getAllData(id)
            debug("getList response")
            debug(response.code().toString())
            debug(response.message().toString())
            debug(response.raw().toString())
            if (!response.isSuccessful) {
                err(response.message())
            } else {
              lg(response.body()!!.toString())
            }
        }
    }
}
