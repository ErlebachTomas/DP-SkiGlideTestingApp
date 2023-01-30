package cz.erlebach.skitesting.common

import android.content.Context
import androidx.lifecycle.lifecycleScope
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.authentication.storage.CredentialsManager
import com.auth0.android.authentication.storage.CredentialsManagerException
import com.auth0.android.authentication.storage.SharedPreferencesStorage
import com.auth0.android.callback.Callback
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.result.Credentials
import com.auth0.android.result.UserProfile
import cz.erlebach.skitesting.R
import cz.erlebach.skitesting.common.interfaces.IAccountManagement
import cz.erlebach.skitesting.utils.err
import cz.erlebach.skitesting.utils.lg
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * Auth0 manager, klient pro obsluhu Auth0 služeb
 * @author Tomas Erlebach
 */
class SessionManager(val context: Context) : IAccountManagement {

    val credentialsManager: CredentialsManager

    private var cachedUserID: String? = null

    private val account: Auth0 = Auth0(
        context.resources.getString(R.string.auth0_client_id),
        context.resources.getString(R.string.auth0_domain)
    )
    /* Auth0 */
    val accountAuth0 get() = account

    init {
        val authAPIClient = AuthenticationAPIClient(account)
        val sharedPrefStorage = SharedPreferencesStorage(this.context)
        credentialsManager = CredentialsManager(authAPIClient, sharedPrefStorage)

    }

    /**
     * Get accessToken
     * @return AccessToken string
     */
    suspend fun fetchAuthToken(): String = suspendCancellableCoroutine { conn ->

        credentialsManager.getCredentials(object :
            Callback<Credentials, CredentialsManagerException> {

            override fun onFailure(error: CredentialsManagerException) {
                err(error.message.toString())
                conn.resumeWithException(error)
            }

            override fun onSuccess(result: Credentials) {
                lg("Access token retrieved")
                lg(result.expiresAt.toString())
                conn.resume(result.accessToken)
            }
        })
    }


    companion object {
        /**
         * kontrola aktivniho přihlašeni
         * @param context
         */
        fun checkIfloginIsValid(context: Context): Boolean {

            val auth0 = Auth0(
                context.resources.getString(R.string.auth0_client_id),
                context.resources.getString(R.string.auth0_domain)
            )
            val authAPIClient = AuthenticationAPIClient(auth0)
            val sharedPrefStorage = SharedPreferencesStorage(context)
            val credentialsManager = CredentialsManager(authAPIClient, sharedPrefStorage)

            return credentialsManager.hasValidCredentials()
        }
    }

    /**
     * Funkce pro přihlášení uživatele do aplikace
     * @param callback
     */
    override fun login(callback: Callback<Credentials, AuthenticationException>) {

        WebAuthProvider
            .login(account)
            .withScheme(getString((R.string.auth0_scheme)))
            .withScope(getString(R.string.login_scopes))
            .withAudience(getString(R.string.api_identifier)) // API_id v auth0 slouží jako audience, jinak nefunguje na serveru token k api
            .start(this.context, object : Callback<Credentials, AuthenticationException> {

                /** uloží credentials a vyvolá callback */
                override fun onSuccess(result: Credentials) {
                    credentialsManager.saveCredentials(result)
                    callback.onSuccess(result)
                }

                override fun onFailure(error: AuthenticationException) {
                    callback.onFailure(error)
                }

            })
    }

    /**
     * Odhlášení uživateslkého účtu z aplikace
     * @param callback
     */
    override fun logout(callback: Callback<Void?, AuthenticationException>) {
        WebAuthProvider
            .logout(account) // Auth0 tenant account
            .withScheme(getString(R.string.auth0_scheme)) // The callback scheme
            .start(this.context, callback)
        cachedUserID = null
    }

    /** přístup ke R zdrojům */
    private fun getString(id: Int): String {
        return context.resources.getString(id)
    }

    /**
     * kontrola aktivniho přihlašeni */
    fun checkIfloginIsValid(): Boolean {
        return credentialsManager.hasValidCredentials()
    }
    /** získa uživatelské metadata z Auth0 serveru */
    public suspend fun getUserProfile(callback: Callback<UserProfile, AuthenticationException>) {
        val client = AuthenticationAPIClient(account)
        client.userInfo(this.fetchAuthToken())
            .start(object : Callback<UserProfile, AuthenticationException> {
                override fun onFailure(error: AuthenticationException) {
                    callback.onFailure(error)
                }

                override fun onSuccess(profile: UserProfile) {
                    cachedUserID = profile.getId()
                    callback.onSuccess(profile)
                }
            })
    }
    /** Vrátí userId
     * @return String userID
     * */
    public suspend fun getUserID(): String {
        if (cachedUserID != null) {
            return cachedUserID!!
        } else {
            val token = this.fetchAuthToken()
            return suspendCancellableCoroutine { conn ->
                val client = AuthenticationAPIClient(account)
                client.userInfo(token)
                    .start(object : Callback<UserProfile, AuthenticationException> {
                        override fun onFailure(error: AuthenticationException) {
                            err(error.getCode() + "|" + error.getDescription())
                            conn.resumeWithException(error)

                        }
                        override fun onSuccess(profile: UserProfile) {
                            cachedUserID = profile.getId()
                            if (cachedUserID != null) {
                                conn.resume(cachedUserID!!)
                            } else {
                                conn.resumeWithException(NullPointerException("User profile id is null"))
                            }

                        }
                    })
            }
        }
    }


}