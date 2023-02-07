package cz.erlebach.skitesting.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import cz.erlebach.skitesting.MainActivity
import cz.erlebach.skitesting.R
import cz.erlebach.skitesting.activity.MeasurementActivity
import cz.erlebach.skitesting.activity.RecommendationActivity
import cz.erlebach.skitesting.activity.SkiProfileActivity
import cz.erlebach.skitesting.activity.UserProfileActivity

/**
Domovská obrazovka
 */
class HomeFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val myView = inflater.inflate(R.layout.fragment_main_home, container, false)

        myView.findViewById<Button>(R.id.btn_sign_out).setOnClickListener { _ ->
            (activity as MainActivity?)!!.logout()
        }

        myView.findViewById<Button>(R.id.btn_ski_profile).setOnClickListener { _ ->
            val intent = Intent(activity, SkiProfileActivity::class.java)
            startActivity(intent)
        }

        myView.findViewById<Button>(R.id.btn_measurement).setOnClickListener { _ ->

            val intent = Intent(activity, MeasurementActivity::class.java)
            startActivity(intent)
        }

        myView.findViewById<Button>(R.id.btn_recommendation).setOnClickListener { _ ->

            val intent = Intent(activity, RecommendationActivity::class.java)
            startActivity(intent)
        }

        myView.findViewById<Button>(R.id.btnTest).setOnClickListener { _ ->
            (activity as MainActivity?)!!.testButtonFunction()
        }

        myView.findViewById<Button>(R.id.btn_userProfile).setOnClickListener { _ ->

            val intent = Intent(activity, UserProfileActivity::class.java)
            startActivity(intent)
        }

        return myView

    }

}