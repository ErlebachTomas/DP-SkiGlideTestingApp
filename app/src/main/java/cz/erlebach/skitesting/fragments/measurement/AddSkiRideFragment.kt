package cz.erlebach.skitesting.fragments.measurement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.room.util.findColumnIndexBySuffix
import cz.erlebach.skitesting.R
import cz.erlebach.skitesting.databinding.FragmentAddSkiRideBinding
import cz.erlebach.skitesting.viewModel.SkiVM


class AddSkiRideFragment : Fragment() {

    private var _binding: FragmentAddSkiRideBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAddSkiRideBinding.inflate(inflater, container, false)

        binding.srBtnSave.setOnClickListener {
            activity?.finish() //todo changeFragmentTo
        }

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


       //todo initSpinnerData()

    }


    private fun initSpinnerData() {

       val  skiViewModel = ViewModelProvider(
           this,
           ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application))[SkiVM::class.java]


            val allSkis = context?.let {
                ArrayAdapter<Any>(it, R.layout.fragment_add_ski_ride)
            }
        skiViewModel.readAllData
                .observe(viewLifecycleOwner) { skis ->
                    skis?.forEach { ski ->
                        allSkis?.add(ski)
                    }
                }
        binding.mfSkiSpinner.adapter = allSkis

        binding.mfSkiSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    Toast.makeText(requireContext(), "$allSkis", Toast.LENGTH_LONG).show()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
            }
        }
    }

