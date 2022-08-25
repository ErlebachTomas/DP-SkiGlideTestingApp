package cz.erlebach.skitesting.fragments.measurement

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import cz.erlebach.skitesting.R
import cz.erlebach.skitesting.databinding.FragmentMeasurementUpdateBinding
import cz.erlebach.skitesting.databinding.FragmentMeasurementUpdateSkiRideBinding
import cz.erlebach.skitesting.fragments.skiProfile.UpdateSkiFragmentArgs
import cz.erlebach.skitesting.model.Ski
import cz.erlebach.skitesting.model.SkiRide
import cz.erlebach.skitesting.viewModel.SkiRideVM
import cz.erlebach.skitesting.viewModel.SkiVM


class UpdateSkiRideFragment : Fragment() {

    private var _binding: FragmentMeasurementUpdateSkiRideBinding? = null
    private val binding get()= _binding!!

    private val args by navArgs<UpdateSkiRideFragmentArgs>()
    private var selectedSkiID: Int =  args.skiRide.skiID

    private lateinit var viewModel: SkiRideVM
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMeasurementUpdateSkiRideBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this)[SkiRideVM::class.java]


        fillForm(args.skiRide)

        binding.usrBtnSave.setOnClickListener {
            updateItem()
        }

        binding.usrBtnBack.setOnClickListener {
            findNavController().navigate(R.id.action_updateSkiRideFragment_to_skiRideListFragment)
        }

        binding.usrBtnDelete.setOnClickListener {
            deleteItem()
        }

        return binding.root

    }

    private fun deleteItem() {
        viewModel.delete(args.skiRide)
        Toast.makeText(requireContext(), getString(R.string.delete_success_message), Toast.LENGTH_SHORT).show()

        findNavController().navigate(R.id.action_updateSkiRideFragment_to_skiRideListFragment)
    }

    /**
     * Fill the form with the current Ski
     */
    private fun fillForm(skiRide: SkiRide) {

        //todo spinery? maybe we should function this


        binding.usrResult.setText(skiRide.result.toString())
        binding.usrNote.setText(skiRide.note)
    }

    private fun updateItem() {

        val result = binding.usrResult.text
        val note = binding.usrNote.text
        //todo selected ski id
        val updateItem = SkiRide(
            args.skiRide.id,
            selectedSkiID,
            args.skiRide.testSessionID,
            result.toString().toDouble(),
            note.toString()
        )

        viewModel.update(updateItem)
        Toast.makeText(requireContext(), getString(R.string.update_success_message), Toast.LENGTH_SHORT).show()

        findNavController().navigate(R.id.action_updateSkiRideFragment_to_skiRideListFragment)


    }

}