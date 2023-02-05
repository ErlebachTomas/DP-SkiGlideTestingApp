package cz.erlebach.skitesting.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.callback.Callback
import com.auth0.android.result.UserProfile
import cz.erlebach.skitesting.common.SessionManager
import cz.erlebach.skitesting.databinding.ActivityUserProfileBinding
import cz.erlebach.skitesting.utils.lg
import cz.erlebach.skitesting.utils.showSnackBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserProfileBinding
    private lateinit var manager: SessionManager



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserProfileBinding.inflate(layoutInflater)

        binding.upUsername.text = "Profil" // undone R string

        manager = SessionManager.getInstance(this)

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

        setContentView(binding.root)
    }


    private fun showInfo() {
        CoroutineScope(Dispatchers.IO).launch() {
        manager.getUserProfile(object : Callback<UserProfile,AuthenticationException> {
            override fun onFailure(error: AuthenticationException) {
                binding.upUserInfo.text = error.message
            }

            override fun onSuccess(userProfile: UserProfile) {

                binding.upUsername.text = "Přihlášen jako: ${userProfile.nickname}"

                val userName = userProfile.name.toString()
                val extraInfo = userProfile.getExtraInfo().toString()
                val createdAt = userProfile.createdAt.toString()
                val pictureURL = userProfile.pictureURL.toString()
                val email = userProfile.email.toString()
                val nickname = userProfile.nickname.toString()

                binding.upUserInfo.text = "userName:$userName\n extraInfo:$extraInfo\n createdAt:$createdAt\n picture:$pictureURL\n email:$email\n nickname:$nickname"
            }
        })
    }}



}


