package cz.erlebach.skitesting.fragments.recommendation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import cz.erlebach.skitesting.MainActivity
import cz.erlebach.skitesting.R
import cz.erlebach.skitesting.databinding.FragmentRecommendationFirstBinding



/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentRecommendationFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRecommendationFirstBinding.inflate(inflater, container, false)

        /*
       binding.recApiCall.setOnClickListener {
        // todo api call ?
       }
        */

        return binding.root

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
        binding.rcBtnBack.setOnClickListener {
            activity?.finish()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}