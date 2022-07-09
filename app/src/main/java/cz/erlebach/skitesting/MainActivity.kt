package cz.erlebach.skitesting

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.callback.Callback
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.result.Credentials
import com.google.android.material.snackbar.Snackbar
import cz.erlebach.skitesting.databinding.ActivityMainBinding



class MainActivity : AppCompatActivity(), IAccountManagement {
    private val TAG = "MainActivity"
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

                override fun onFailure(exception: AuthenticationException) {
                    showSnackBar(getString(R.string.login_failure_message) + ": " + exception.message)
                }

                override fun onSuccess(credentials: Credentials) {
                    cachedCredentials = credentials

                    showSnackBar(getString(R.string.login_success_message) + credentials.accessToken)

                 // todo změnit fragment

                }
            })
    }

    override fun logout() {
        TODO("Not yet implemented")
    }


    /**
     * Kontrola potřebných oprávnění
     */
    private fun checkAllpermissions() {

        val requestCode:Int = 1

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
            != PackageManager.PERMISSION_GRANTED
        // || ...
        ){
            log("vyzadovano udeleni opravneni ")
            ActivityCompat.requestPermissions(this,
                arrayOf(
                    Manifest.permission.INTERNET,
                    // ..
                    ),
                requestCode)
        } else {
            log("opravneni udeleno")
        }

    }

    /* TOOLS */

    /** Zjednodušená logovací funkce pro debug */
    private fun log(text: String, tag: String = TAG, type: Char = 'v') {
        when (type) {
            'e' -> Log.e(tag, text)
            else -> {
                Log.v(tag, text)
            }
        }
    }

    /** info výpis na obrazovku */
    private fun showSnackBar(text: String) {
        Snackbar.make(binding.root, text, Snackbar.LENGTH_LONG).show()
    }


}