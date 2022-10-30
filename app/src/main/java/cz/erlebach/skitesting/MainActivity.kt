package cz.erlebach.skitesting

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.callback.Callback
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.result.Credentials
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.Headers
import com.github.kittinunf.fuel.json.responseJson
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import cz.erlebach.skitesting.common.interfaces.IAccountManagement
import cz.erlebach.skitesting.databinding.ActivityMainBinding
import cz.erlebach.skitesting.fragments.HomeFragment
import cz.erlebach.skitesting.fragments.LoginFragment
import cz.erlebach.skitesting.fragments.NoConnectionFragment
import cz.erlebach.skitesting.network.ApiService
import kotlinx.coroutines.*
import java.net.HttpURLConnection
import java.net.URL


class MainActivity : AppCompatActivity(), IAccountManagement {
    private val TAG = "ActivityMain"
    /**
     * Instance vazební třídy obsahující přímé odkazy (nahrazuje findViewById konstrukci)
     */
    private lateinit var binding: ActivityMainBinding
    /* Auth0 */
    private lateinit var account: Auth0
    private var cachedCredentials: Credentials? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater) // metoda generující binding class

        checkAllpermissions()

        if (!this.isDeviceOnline(this)) {
            changeFragmentTo(NoConnectionFragment()) // undone offline politika
        } else {
            if(checkIfloginInfoAlreadyExist()) {
                autologin()
            } else {
                changeFragmentTo(LoginFragment())
            }
        }

        account = Auth0(
            getString(R.string.auth0_client_id),
            getString(R.string.auth0_domain)
        )

    }

    /**
     * Realizace přihlášení do aplikace + login sesion pomocí shared pref
     */
    override fun login() {

        WebAuthProvider
            .login(account)
            .withScheme(getString(R.string.auth0_scheme))
            .withScope(getString(R.string.login_scopes))
            .withAudience(getString(R.string.login_audience, getString(R.string.auth0_domain)))
            .start(this, object : Callback<Credentials, AuthenticationException> {

                override fun onFailure(error: AuthenticationException) {
                    toast(getString(R.string.login_failure_message) + ": " + error.message)
                    changeFragmentTo(NoConnectionFragment())
                }

                override fun onSuccess(result: Credentials) {

                    cachedCredentials = result // výsledek uložen do shared pref jako json

                    val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return
                    with (sharedPref.edit()) {

                        val gson = Gson()
                        val json = gson.toJson(result)

                        putString(getString(R.string.cachedCredentials), json)
                        apply()
                    }
                    changeFragmentTo(HomeFragment())

                }
            })
    }

    /**
     * Odhlášení
     */
    override fun logout() {
        WebAuthProvider
            .logout(account) // Auth0 tenant account
            .withScheme(getString(R.string.auth0_scheme)) // The callback scheme
            .start(this, object : Callback<Void?, AuthenticationException> {

                override fun onFailure(error: AuthenticationException) {
                    toast( getString(R.string.logout_err_message) + error.getCode())
                }

                override fun onSuccess(result: Void?) {
                    toast( getString(R.string.login_success_message)) // undone logout msg
                    changeFragmentTo(LoginFragment())
                    cachedCredentials = null
                }
            })
    }


    fun autologin() {
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        val myCredentials = sharedPref.getString(getString(R.string.cachedCredentials), null)
        cachedCredentials = Gson().fromJson(myCredentials, Credentials::class.java)

        toast(getString(R.string.login_success_message))
        changeFragmentTo(HomeFragment())
    }

    /**
     * kontrola aktivniho přihlašeni
     */
    private fun checkIfloginInfoAlreadyExist(): Boolean {
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        val value = sharedPref.getString(getString(R.string.cachedCredentials), null)
        return value != null
    }

    /**
     * Kontrola potřebných oprávnění
     */
    private fun checkAllpermissions() {

        val requestCode = 1

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
            != PackageManager.PERMISSION_GRANTED
        // || ...
        ){
            log("vyzadovano udeleni opravneni ")
            ActivityCompat.requestPermissions(this,
                arrayOf(
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_NETWORK_STATE
                    // ..
                    ),
                requestCode)
        } else {
            log("opravneni udeleno")
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

    /** Zjednodušená logovací funkce pro debug */
    private fun log(text: String, tag: String = TAG) {
        Log.v(tag, text)
    }

    /** info výpis na obrazovku */
    private fun showSnackBar(text: String) {
        Snackbar.make(binding.root, text, Snackbar.LENGTH_LONG).show()

    }

    /** Zobrazí toast */
    private fun toast(text: String, length: Int = Toast.LENGTH_SHORT) {
        log(text)
        Toast.makeText(applicationContext,text,length).show()
    }

    /** kontroluje zda je zařízení připojeno k internetu */
    private fun isDeviceOnline(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                return true
            }
        }
        return false
    }


    fun apiCall() {
        val url = ApiService.BASE_URL + "/api/getAllUsers"

        lifecycleScope.launch(Dispatchers.IO) {
           // fuelGet(url)
            //sendGet(url)

            cachedCredentials?.let { getApiToken(it) }
        }

    // undone https://github.com/zeeshanejaz/unirest-android | https://github.com/kittinunf/fuel
        /*
    if (token!= null) {

        log("apiCall $url $token")

        val response = Unirest.get(url)
            .header("content-type", "application/json")
            .header("authorization", "Bearer $token")
            .asJsonAsync { httpResponse ->
                toast(httpResponse.body.toPrettyString())
                log(httpResponse.body.toPrettyString())

                httpResponse.ifFailure {
                    toast( "Faill")
                }
                httpResponse.ifSuccess {
                    var data =  it.body.toPrettyString()
                    toast(
                         data
                    )
                    log("log $data")
                }
            }
    } else {
        throw IllegalStateException("Invalid credentials")
    }
    */
    }

    fun sendGet(url: String) {
        val geUrl = URL(url)

        with(geUrl.openConnection() as HttpURLConnection) {
            requestMethod = "GET"  // optional default is GET

            log("\nSent 'GET' request to URL : $geUrl; Response Code : $responseCode")

            inputStream.bufferedReader().use {
                it.lines().forEach { line ->
                   log(line)
                }
            }
        }
    }

    fun fuelGet( url: String) {
        // DO get token
        /*
       val token = "mytoken"
        Fuel.get("https://httpbin.org/bearer")
            .authentication()
            .bearer(token)
            .response { result -> }
        * */
        Fuel.get(url).responseJson { _, _, result ->
            result.fold(success = { json ->
                log(json.array().toString())
            }, failure = { error ->
                Log.e(TAG, error.toString())
            })
        }
    }


    /**
     * Získá access API jw token
     * @see <a href="https://auth0.com/docs/get-started/architecture-scenarios/mobile-api/part-3">Auth0 doc</a>
     */
    private fun getApiToken(credentials: Credentials) {

        val domain = getString(R.string.auth0_domain)
        val clientID = getString(R.string.auth0_client_id)
        val apiIdentifier = getString(R.string.api_identifier)
        val clientSecret = credentials.idToken

        Fuel.post("https://$domain/oauth/token")
            .header(Headers.CONTENT_TYPE, "application/x-www-form-urlencoded")
            .body("grant_type=client_credentials&client_id=$clientID&client_secret=$clientSecret&audience=$apiIdentifier")
            .also { println(it)
            }
            .responseJson { _, _, result ->
                result.fold(success = { json ->
                    log(json.array().toString())
                }, failure = { error ->
                    Log.e(TAG, error.toString()) // -> "Grant type 'client_credentials' not allowed for the client.",
                })
            }
/*
    val response: HttpResponse<String> = Unirest.post("https://YOUR_DOMAIN/oauth/token")
        .header("content-type", "application/x-www-form-urlencoded")
        .body("grant_type=client_credentials&client_id=YOUR_CLIENT_ID&client_secret=YOUR_CLIENT_SECRET&audience=YOUR_API_IDENTIFIER")
        .asString()
*/
    }

}