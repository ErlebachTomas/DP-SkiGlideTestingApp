package cz.erlebach.skitesting.utils

import android.util.Base64
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.SecureRandom

class AuthorizationClient(domain: String, appPackage: String) {

    /**
     *  Kryptograficky náhodný klíč [codeVerifier], který bude pro vyžádání tokenů odeslán do Auth0
     */
    val codeVerifier: String
    /**
     * Konstanta, musí být authorization_code
     */
    val grantType: String = "authorization_code"
    /**
     * Denotes the kind of credential that Auth0 will return (code or token). For this flow, the value must be code
     */
    val responseType: String = "code"

    /**
     * The valid callback URL set in your Application settings
     */
    val redirectUri: String
    /**
    Zkratka pro algoritmus SHA-256
     */
    val codeChallengeMethod = "S256"

    /**
     * The scopes for which you want to request authorization. These must be separated by a space.
     */
    val scope = "read:ski offline_access"

    /** libovolný alfanumerický řetězec, který vaše aplikace přidá do počátečního požadavku a který Auth0 zahrne při přesměrování zpět na vaši aplikace */
    val state: String

    /** Generated challenge from the code_verifier */
    val codeChallenge: String

    init {
        val sr = SecureRandom()
        val code = ByteArray(32)
        sr.nextBytes(code)
        codeVerifier = Base64.encodeToString(code, Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING)
        redirectUri = "app://$domain/android/$appPackage/callback"
        state = "state" + sr.nextInt().toString()
        codeChallenge = generateCodeChallenge()
    }

    /**
     * Generuje codeChallenge z [codeVerifier] který bude odeslán do Auth0 pro získání autorizačního kódu
     * @return codeChallenge String
     */
    private fun generateCodeChallenge(): String {
        val bytes: ByteArray = codeVerifier.toByteArray(StandardCharsets.US_ASCII)
        val md: MessageDigest = MessageDigest.getInstance("SHA-256")
        md.update(bytes, 0, bytes.size)
        val digest: ByteArray = md.digest()

        return Base64.encodeToString(digest, Base64.URL_SAFE)
    }




}