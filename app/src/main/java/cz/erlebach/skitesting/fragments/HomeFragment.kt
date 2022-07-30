package cz.erlebach.skitesting.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import cz.erlebach.skitesting.MainActivity
import cz.erlebach.skitesting.R
import cz.erlebach.skitesting.activity.RecommendationActivity
import cz.erlebach.skitesting.activity.SkiProfileActivity

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

        val myView = inflater.inflate(R.layout.fragment_home, container, false)

        myView.findViewById<Button>(R.id.btn_sign_out).setOnClickListener { _ ->
            (activity as MainActivity?)!!.logout()
        }

        myView.findViewById<Button>(R.id.btn_ski_profile).setOnClickListener { _ ->
            (activity as MainActivity?)!!.logout()
        }

        myView.findViewById<Button>(R.id.btn_ski_profile).setOnClickListener { _ ->
            val intent = Intent(activity, SkiProfileActivity::class.java)
            startActivity(intent)
        }

        myView.findViewById<Button>(R.id.btn_measurement).setOnClickListener { _ ->
            Log.v("Home","click")
            val intent = Intent(activity, RecommendationActivity::class.java)
            startActivity(intent)
        }

        return myView

    }

}