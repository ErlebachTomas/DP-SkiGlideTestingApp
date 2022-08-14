package cz.erlebach.skitesting.fragments.measurement

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import cz.erlebach.skitesting.R
import cz.erlebach.skitesting.databinding.FragmentMeasurementAddSkiRideBinding
import cz.erlebach.skitesting.model.Ski
import cz.erlebach.skitesting.viewModel.SkiVM


class AddSkiRideFragment : Fragment() {

    private var _binding: FragmentMeasurementAddSkiRideBinding? = null
    private val binding get() = _binding!!

    private val TAG = "AddSkiRideFragment"

    private lateinit var selectedSki: Ski

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentMeasurementAddSkiRideBinding.inflate(inflater, container, false)

        binding.srBtnSave.setOnClickListener {
            Log.v(TAG,"save")
            activity?.finish() //todo changeFragmentTo
        }

        binding.srBtnBack.setOnClickListener {
            Log.v(TAG,"back")
            activity?.finish() //todo changeFragmentTo
        }
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

            initSpinnerData()

    }


    private fun initSpinnerData() {

       val  skiViewModel = ViewModelProvider(
           this,
           ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application))[SkiVM::class.java]


       val allSkis = context?.let {
                ArrayAdapter<Ski>(it, R.layout.adapter_spinner_measurement_ski_row,R.id.mf_twSpinnerRow)
       }

        //todo ošetření žádná lyže
        skiViewModel.readAllData
                .observe(viewLifecycleOwner) { skis ->
                    skis?.forEach { ski ->
                        allSkis?.add(ski)
                    }
                }


        binding.mfSkiSpinner.adapter = allSkis //todo bind spinner to custom object list https://stackoverflow.com/a/21169383

        binding.mfSkiSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                    selectedSki = parent?.selectedItem as Ski
                    Toast.makeText(requireContext(), selectedSki.name, Toast.LENGTH_SHORT).show()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
            }
        }

}

