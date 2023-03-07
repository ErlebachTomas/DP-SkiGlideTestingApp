package cz.erlebach.skitesting.fragments.measurement

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import cz.erlebach.skitesting.common.template.MyViewModelFactory

import cz.erlebach.skitesting.databinding.FragmentMeasurementSkiRideListBinding
import cz.erlebach.skitesting.repository.SkiRideRepository
import cz.erlebach.skitesting.viewModel.SkiRideVM
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SkiRideListFragment : Fragment() {

    private var _binding: FragmentMeasurementSkiRideListBinding? = null
    private val binding get() = _binding!!

    lateinit var viewModel : SkiRideVM
    private val args by navArgs<SkiRideListFragmentArgs>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMeasurementSkiRideListBinding.inflate(inflater, container, false)

        try {
            initviewModel()
        } catch (e: Exception) {
            Log.e("ViewModel",e.printStackTrace().toString())
        }

        binding.srlBtnAddNew.setOnClickListener {

                val action = SkiRideListFragmentDirections.actionSkiRideListFragmentToAddSkiRideFragment(args.idTestSession)
                findNavController().navigate(action)

        }

        return binding.root
    }

    /**
     * Načte data a naplní list jízd
     */
    private fun initviewModel() {

        viewModel = ViewModelProvider(
            this,
            MyViewModelFactory(SkiRideVM(SkiRideRepository(requireContext())))
        )[SkiRideVM::class.java]

        val adapter = SkiRideListAdapter(viewModel)

        binding.srlRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.srlRecyclerView.adapter = adapter


        val liveData = viewModel.loadTestSessionRideByID(args.idTestSession)
        liveData.observe(viewLifecycleOwner) { item ->
            adapter.setData(item)
        }



        /*
        viewModel.readAllData.observe(viewLifecycleOwner) { item ->
            adapter.setData(item)  // todo read data with id!!!!!
        }
        */
    }

}