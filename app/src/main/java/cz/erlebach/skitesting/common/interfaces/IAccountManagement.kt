package cz.erlebach.skitesting.common.interfaces

import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.callback.Callback
import com.auth0.android.result.Credentials

interface IAccountManagement {

    /** Přihlášení k účtu
     */
    fun login(callback: Callback<Credentials, AuthenticationException>)

    /** Odhlášení z uživatelského účtu */
    fun logout(callback: Callback<Void?, AuthenticationException>)

    /**
     * ID přihlášeného uživatele
     * @return jednoznačný identifikátor uživatele */
    suspend fun getUserID(): String
}