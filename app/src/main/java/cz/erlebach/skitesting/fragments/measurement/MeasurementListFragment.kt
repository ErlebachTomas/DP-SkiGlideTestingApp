package cz.erlebach.skitesting.fragments.measurement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import cz.erlebach.skitesting.R
import cz.erlebach.skitesting.databinding.FragmentMeasurementListBinding
import cz.erlebach.skitesting.viewModel.local.TestSessionVM

class MeasurementListFragment : Fragment() {

    private var _binding: FragmentMeasurementListBinding? = null
    private val binding get() = _binding!!

    //todo https://thecommonwise.com/blogs/60f6ea9bea3d10001503eac3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMeasurementListBinding.inflate(inflater, container, false)


        init()

        binding.mlBtnAddNew.setOnClickListener {

            findNavController().navigate(R.id.action_MeasurementFragment_to_measurementFormFragment)

        }

        return binding.root
    }
    private fun init() {

        val viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application))[TestSessionVM::class.java]

        val adapter = MeasurementRecyclerViewAdapter()

        binding.mlRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.mlRecyclerView.adapter = adapter

        viewModel.readAllData.observe(viewLifecycleOwner) { item ->
            adapter.setData(item)
        }

    }



}