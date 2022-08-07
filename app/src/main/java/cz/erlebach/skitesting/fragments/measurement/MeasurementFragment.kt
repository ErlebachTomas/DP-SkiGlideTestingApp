package cz.erlebach.skitesting.fragments.measurement

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import cz.erlebach.skitesting.R
import cz.erlebach.skitesting.databinding.FragmentMeasurementBinding

class MeasurementFragment : Fragment() {

    private var _binding: FragmentMeasurementBinding? = null
    private val binding get() = _binding!!

    //todo https://thecommonwise.com/blogs/60f6ea9bea3d10001503eac3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMeasurementBinding.inflate(inflater, container, false)

        binding.mlBtnAddNew.setOnClickListener {
            findNavController().navigate(R.id.action_mListFragment_to_measurementFormFragment)

        }
        return binding.root
    }


}