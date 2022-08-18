package cz.erlebach.skitesting.fragments.measurement

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

import cz.erlebach.skitesting.databinding.FragmentMeasurementSkiRideListBinding
import cz.erlebach.skitesting.viewModel.SkiRideVM
import cz.erlebach.skitesting.viewModel.TestSessionVM

class SkiRideListFragment : Fragment() {

    private var _binding: FragmentMeasurementSkiRideListBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMeasurementSkiRideListBinding.inflate(inflater, container, false)

        initviewModel()

        return binding.root
    }

    /**
     * Načte data a naplní list jízd
     */
    private fun initviewModel() {

        val viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application))[SkiRideVM::class.java]

        val adapter = SkiRideListAdapter()

        binding.srlRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.srlRecyclerView.adapter = adapter



        viewModel.readAllData.observe(viewLifecycleOwner) { item ->
            adapter.setData(item)  // todo read data with id!!!!!
        }

    }

}