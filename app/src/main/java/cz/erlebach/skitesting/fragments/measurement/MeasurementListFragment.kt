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
import cz.erlebach.skitesting.common.template.MyViewModelFactory
import cz.erlebach.skitesting.common.utils.toast
import cz.erlebach.skitesting.databinding.FragmentMeasurementListBinding
import cz.erlebach.skitesting.repository.TestSessionRepository
import cz.erlebach.skitesting.viewModel.SkiVM
import cz.erlebach.skitesting.viewModel.TestSessionVM
import cz.erlebach.skitesting.viewModel.local.TestSessionLocalVM

class MeasurementListFragment : Fragment() {

    private var _binding: FragmentMeasurementListBinding? = null
    private val binding get() = _binding!!

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

        val adapter = MeasurementRecyclerViewAdapter()
        binding.mlRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.mlRecyclerView.adapter = adapter

        try {
            val viewModel = ViewModelProvider(this,
                MyViewModelFactory(TestSessionVM(TestSessionRepository(requireContext()))))[TestSessionVM::class.java]

            viewModel.data.observe(viewLifecycleOwner) { resource ->
                resource.data?.let {
                    adapter.setData(it) }
            }

        } catch (E: Exception) {
            toast(requireContext(), E.message.toString())
        }

    }



}