package cz.erlebach.skitesting.fragments.measurement

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import cz.erlebach.skitesting.R
import cz.erlebach.skitesting.common.template.MyViewModelFactory
import cz.erlebach.skitesting.databinding.FragmentMeasurementUpdateSkiRideBinding
import cz.erlebach.skitesting.model.SkiRide
import cz.erlebach.skitesting.common.utils.date.generateDateISO8601string
import cz.erlebach.skitesting.model.Ski
import cz.erlebach.skitesting.repository.SkiRepository
import cz.erlebach.skitesting.repository.SkiRideRepository
import cz.erlebach.skitesting.viewModel.SkiRideVM
import cz.erlebach.skitesting.viewModel.SkiVM


class UpdateSkiRideFragment : Fragment() {

    private var _binding: FragmentMeasurementUpdateSkiRideBinding? = null
    private val binding get()= _binding!!

    private val args by navArgs<UpdateSkiRideFragmentArgs>()
    private lateinit var selectedSkiID: String

    private lateinit var viewModel: SkiRideVM
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMeasurementUpdateSkiRideBinding.inflate(inflater, container, false)

        selectedSkiID =  args.skiRide.skiID

        viewModel = ViewModelProvider(
            this,
            factory= MyViewModelFactory(SkiRideVM(SkiRideRepository(requireContext())))
        )[SkiRideVM::class.java]


        fillForm(args.skiRide)

        binding.usrBtnSave.setOnClickListener {
            updateItem()
        }

        binding.usrBtnBack.setOnClickListener {

            val action = UpdateSkiRideFragmentDirections.actionUpdateSkiRideFragmentToSkiRideListFragment(args.skiRide.testSessionID)
            findNavController().navigate(action)

        }

        binding.usrBtnDelete.setOnClickListener {
            deleteItem()
        }

        return binding.root

    }

    private fun deleteItem() {

        viewModel.delete(args.skiRide)

        Toast.makeText(requireContext(), getString(R.string.delete_success_message), Toast.LENGTH_LONG).show()

        Handler(Looper.getMainLooper()).postDelayed({
            findNavController().navigate(R.id.action_updateSkiRideFragment_to_skiRideListFragment)
        }, 1000)
    }

    /**
     * Fill the form with the current Ski
     */
    private fun fillForm(skiRide: SkiRide) {

        val skiViewModel = ViewModelProvider(this,
            factory = MyViewModelFactory(SkiVM(SkiRepository(requireContext())))
        )[SkiVM::class.java]

        val allSkis = context?.let {
            ArrayAdapter<Ski>(it, R.layout.adapter_spinner_measurement_ski_row,R.id.mf_twSpinnerRow)
        }
        skiViewModel.userSkis
            .observe(viewLifecycleOwner) { skis ->
                if (skis.isNotEmpty()) {
                    skis?.forEach { ski ->
                        allSkis?.add(ski)
                    }
                }
                val selectedSki = skis.find { it.id == args.skiRide.skiID }
                val position = allSkis?.getPosition(selectedSki)
                if (position != null && position >= 0) {
                    binding.usrSkiSpinner.setSelection(position)
                }
            }
       binding.usrSkiSpinner.adapter = allSkis

        binding.usrSkiSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedSki = parent?.selectedItem as Ski
                selectedSkiID = selectedSki.id
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                //TODO("Not yet implemented")
            }
        }
        binding.usrResult.setText(skiRide.result.toString())
        binding.usrNote.setText(skiRide.note)
    }

    private fun updateItem() {

        val result = binding.usrResult.text
        val note = binding.usrNote.text

        val updateItem = SkiRide(
            args.skiRide.id,
            selectedSkiID,
            args.skiRide.testSessionID,
            result.toString().toDouble(),
            note.toString(),
            generateDateISO8601string()
        )

        viewModel.update(updateItem)
        Toast.makeText(requireContext(), getString(R.string.update_success_message), Toast.LENGTH_SHORT).show()

        findNavController().navigate(R.id.action_updateSkiRideFragment_to_skiRideListFragment)


    }

}