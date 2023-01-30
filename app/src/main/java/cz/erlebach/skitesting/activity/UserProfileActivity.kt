package cz.erlebach.skitesting.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.callback.Callback
import com.auth0.android.management.ManagementException
import com.auth0.android.management.UsersAPIClient
import com.auth0.android.result.UserProfile
import cz.erlebach.skitesting.R
import cz.erlebach.skitesting.common.SessionManager
import cz.erlebach.skitesting.databinding.ActivityUserProfileBinding
import cz.erlebach.skitesting.utils.err
import cz.erlebach.skitesting.utils.lg
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserProfileBinding

    private lateinit var manager: SessionManager
    private var cachedUserProfile: UserProfile? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserProfileBinding.inflate(layoutInflater)


        binding.upBtn.setOnClickListener {
            lg("click")
            getUserProfile()

        }
        setContentView(binding.root)
    }


    private fun getUserProfile() {

        manager = SessionManager(this)
        val client = AuthenticationAPIClient(manager.accountAuth0)

        lifecycleScope.launch(Dispatchers.IO) {

            val accessToken = manager.fetchAuthToken()
            // Use the received access token to call `userInfo` and get the profile from Auth0.
            client.userInfo(accessToken)
                .start(object : Callback<UserProfile, AuthenticationException> {
                    override fun onFailure(exception: AuthenticationException) {
                        // Something went wrong!
                        err("Something went wrong!")
                        err(exception.getCode() + ": " + exception.getDescription())
                    }

                    override fun onSuccess(profile: UserProfile) {
                        // We have the user's profile!
                        val name = profile.name
                        lg("UserProfile  onSuccess")
                        if (name != null) {
                            lg(name)
                        }
                        profile.getId()?.let { lg(it) }

                        cachedUserProfile = profile
                        val userName = cachedUserProfile?.name.toString()
                        val extraInfo = cachedUserProfile?.getExtraInfo().toString()
                        val createdAt = cachedUserProfile?.createdAt.toString()
                        val pictureURL = cachedUserProfile?.pictureURL.toString()
                        val email = cachedUserProfile?.email.toString()
                        val nickname = cachedUserProfile?.nickname.toString()

                        lg("userName:$userName\n extraInfo:$extraInfo\n createdAt:$createdAt\n picture:$pictureURL\n email:$email\n nickname:$nickname" )


                    }
                })

        }

    }

}


