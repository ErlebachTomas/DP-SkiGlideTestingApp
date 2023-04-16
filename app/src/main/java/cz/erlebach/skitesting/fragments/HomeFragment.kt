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
import cz.erlebach.skitesting.databinding.FragmentMainHomeBinding
import cz.erlebach.skitesting.databinding.FragmentNewApiVersionBinding

/**
Domovská obrazovka
 */
class HomeFragment : Fragment() {

    private var _binding: FragmentMainHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMainHomeBinding.inflate(inflater, container, false)

        binding.btnSignOut.setOnClickListener { _ ->
            (activity as MainActivity?)!!.logout()
        }
        binding.btnSkiProfile.setOnClickListener { _ ->
            val intent = Intent(activity, SkiProfileActivity::class.java)
            startActivity(intent)
        }

        binding.btnMeasurement.setOnClickListener { _ ->

            val intent = Intent(activity, MeasurementActivity::class.java)
            startActivity(intent)
        }

        binding.btnRecommendation.setOnClickListener { _ ->

            val intent = Intent(activity, RecommendationActivity::class.java)
            startActivity(intent)
        }

        binding.btnTest.setOnClickListener { _ ->
            (activity as MainActivity?)!!.syncWithServer()
        }
        binding.btnUserProfile.setOnClickListener { _ ->
            val intent = Intent(activity, UserProfileActivity::class.java)
            startActivity(intent)
        }

        return binding.root

    }

}