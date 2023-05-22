package cz.erlebach.skitesting.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.lifecycleScope
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.callback.Callback
import com.auth0.android.result.UserProfile
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.authentication
import com.github.kittinunf.fuel.json.responseJson
import cz.erlebach.skitesting.BuildConfig
import cz.erlebach.skitesting.R
import cz.erlebach.skitesting.common.SessionManager
import cz.erlebach.skitesting.common.utils.lg
import cz.erlebach.skitesting.common.utils.showSnackBar
import cz.erlebach.skitesting.common.utils.toast
import cz.erlebach.skitesting.common.utils.wtf
import cz.erlebach.skitesting.databinding.ActivityUserProfileBinding
import cz.erlebach.skitesting.network.RetrofitApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Uživatelský profil a vše s ním spojené
 */
class UserProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserProfileBinding
    private lateinit var manager: SessionManager
    private lateinit var token: String
    private val url = RetrofitApiService.URL + "export"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserProfileBinding.inflate(layoutInflater)

        binding.upUsername.text = getString(R.string.profile)
        binding.upServer.text = "API: ${BuildConfig.SERVER_URL}${BuildConfig.API_VERSION}"

        manager = SessionManager.getInstance(this@UserProfileActivity)
        lifecycleScope.launch(Dispatchers.IO) {
            token = manager.fetchAuthToken()
        }

        binding.upUsername.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch() {
                lg(manager.getUserID())
                showSnackBar(binding.root,"id:${manager.getUserID()}")
            }
        }

        showInfo()

        binding.upBack.setOnClickListener {
            this.finish()
        }


        binding.btnSaveJson.setOnClickListener {

            if (Environment.isExternalStorageManager()) {
                openFolderPicker()
            } else {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
            }

        }

        setContentView(binding.root)
    }


    /** Načte informace o uživatelském účtu */
    private fun showInfo() {
        CoroutineScope(Dispatchers.IO).launch() {
        manager.getUserProfile(object : Callback<UserProfile,AuthenticationException> {
            override fun onFailure(error: AuthenticationException) {
                binding.upUserInfo.text = error.message
            }

            override fun onSuccess(result: UserProfile) {

                binding.upUsername.text = getString(R.string.logged, result.nickname)

                val userName = result.name.toString()
                val extraInfo = result.getExtraInfo().toString()
                val createdAt = result.createdAt.toString()
                val pictureURL = result.pictureURL.toString()
                val email = result.email.toString()
                val nickname = result.nickname.toString()

                binding.upUserInfo.text = getString(R.string.user_info,
                    userName,
                    extraInfo,
                    createdAt,
                    pictureURL,
                    email,
                    nickname
                )
            }
        })
    }}

   /** Načtení a uložení uživatelských dat ze serveru */
    private var folderPickerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val uri = result.data?.data
                uri?.let {
                    Fuel.get(url)
                        .authentication()
                        .bearer(token)
                        .responseJson { _, _, result ->

                            result.fold(success = { json ->
                                saveJsonToFile(it, json.obj().toString())
                            }, failure = { error ->
                                wtf("Failed to load data from sever", error)
                            })
                        }
                }
            }
        }

/** výběr umístění pro export */
private fun openFolderPicker() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        folderPickerLauncher.launch(intent)
    }

    /** Uloží JSON do souboru */
    private fun saveJsonToFile(folderUri: Uri, jsonObject: String, fileName : String = "exportedData.json" ) {
        try {
            val pickedDir = DocumentFile.fromTreeUri(this, folderUri)
            val file = pickedDir?.createFile("application/json", fileName)
            val outputStream = file?.uri?.let { contentResolver.openOutputStream(it) }

            outputStream?.use { output ->
                output.write(jsonObject.toByteArray())
                runOnUiThread {
                    toast(this, getString(R.string.saved)) // jinak hodí chybu  "Can't toast on a thread that has not called Looper.prepare()"
                }
            }
        } catch (e: Exception) {
            wtf( "Error saving JSON file", e)

            runOnUiThread {
            toast(this,getString(R.string.save_error))
            }

        }
    }
}


