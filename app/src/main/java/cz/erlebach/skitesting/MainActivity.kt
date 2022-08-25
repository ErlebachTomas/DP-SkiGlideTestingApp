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
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.callback.Callback
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.result.Credentials
import com.google.android.material.snackbar.Snackbar
import cz.erlebach.skitesting.databinding.ActivityMainBinding
import cz.erlebach.skitesting.fragments.HomeFragment
import cz.erlebach.skitesting.fragments.LoginFragment
import cz.erlebach.skitesting.fragments.NoConnectionFragment
import cz.erlebach.skitesting.common.interfaces.IAccountManagement


class MainActivity : AppCompatActivity(), IAccountManagement {
    private val TAG = "ActivityMain"
    /**
     * Instance vazební třídy obsahující přímé odkazy (nahrazuje findViewById konstrukci)
     */
    private lateinit var binding: ActivityMainBinding
    /* Auth0 */
    private lateinit var account: Auth0
    private var cachedCredentials: Credentials? = null

    // todo autologin a debug mod

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater) // metoda generující binding class

        checkAllpermissions()

        if (!this.isDeviceOnline(this)) {
            changeFragmentTo(NoConnectionFragment())
        }

        account = Auth0(
            getString(R.string.auth0_client_id),
            getString(R.string.auth0_domain)
        )

    }

    override fun login() {

        log("login")

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

                    cachedCredentials = result

                    toast(getString(R.string.login_success_message) + result.accessToken)

                    changeFragmentTo(HomeFragment())

                }
            })
    }

    override fun logout() {
        WebAuthProvider
            .logout(account) // Auth0 tenant account
            .withScheme(getString(R.string.auth0_scheme)) // The callback scheme
            .start(this, object : Callback<Void?, AuthenticationException> {

                override fun onFailure(error: AuthenticationException) {
                    toast( getString(R.string.logout_err_message) + error.getCode())
                }

                override fun onSuccess(result: Void?) {
                    toast( getString(R.string.login_success_message))
                    changeFragmentTo(LoginFragment())
                    cachedCredentials = null
                }
            })
    }
    /*
    jen pro debug
     */
    fun skip() {
        changeFragmentTo(HomeFragment())
        //todo: uprava pro offline použití
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


}