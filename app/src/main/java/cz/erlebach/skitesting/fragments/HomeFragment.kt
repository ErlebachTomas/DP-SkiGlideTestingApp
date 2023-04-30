package cz.erlebach.skitesting.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import cz.erlebach.skitesting.MainActivity
import cz.erlebach.skitesting.R
import cz.erlebach.skitesting.activity.HelpActivity
import cz.erlebach.skitesting.activity.MeasurementActivity
import cz.erlebach.skitesting.activity.RecommendationActivity
import cz.erlebach.skitesting.activity.Sandbox
import cz.erlebach.skitesting.activity.SkiProfileActivity
import cz.erlebach.skitesting.activity.Stopwatch
import cz.erlebach.skitesting.activity.UserProfileActivity
import cz.erlebach.skitesting.common.utils.lg
import cz.erlebach.skitesting.common.utils.toast
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

        binding.btnSync.setOnClickListener { _ ->
            (activity as MainActivity?)!!.syncWithServer()
            toast(requireContext(),getString(R.string.sync))
        }
        binding.btnUserProfile.setOnClickListener { _ ->
            val intent = Intent(activity, UserProfileActivity::class.java)
            startActivity(intent)
        }

        binding.btnInfo.setOnClickListener { _ ->
            val intent = Intent(activity, HelpActivity::class.java) 
        }
       
        binding.sandbox.setOnClickListener { _ ->
            startActivity(Intent(activity, Sandbox::class.java))

           /* val intent = Intent(activity, Stopwatch::class.java)
            resultLauncher.launch(intent) // print value
            */

        }


        return binding.root

    }

    /*
    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            data?.let {
                val value = data.getLongExtra(Stopwatch.timeTAG, 0)
               lg(value.toString())
                toast(requireContext(),value.toString())
            }
        }
    }

     */

}