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
import cz.erlebach.skitesting.model.TestSession
import cz.erlebach.skitesting.utils.getDateFormatString
import cz.erlebach.skitesting.viewModel.TestSessionVM
import java.util.*


/**
 Fragment umožňující editaci [TestSession]
 */
class MeasurementUpdateFragment : Fragment() {

    private var _binding: FragmentMeasurementUpdateBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<MeasurementUpdateFragmentArgs>()

    private lateinit var viewModel: TestSessionVM

    private lateinit var datetime : Date
    private lateinit var snowType :String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMeasurementUpdateBinding.inflate(inflater, container, false)

        datetime = args.testSession.datetime
        snowType = args.testSession.snowType
        viewModel = ViewModelProvider(this)[TestSessionVM::class.java]

        fillForm(args.testSession)

        binding.umfBtnBack.setOnClickListener {
            findNavController().navigate(R.id.action_measurementUpdateFragment_to_MeasurementFragment)

        }

        binding.umfBtnSave.setOnClickListener {
            updateItem()
        }

        binding.umfBtnDelete.setOnClickListener {

            deleteItem()
        }


        return binding.root

    }

    private fun deleteItem() {
        viewModel.delete(args.testSession)
        Toast.makeText(requireContext(), R.string.success, Toast.LENGTH_SHORT).show()
        findNavController().navigate(R.id.action_measurementUpdateFragment_to_MeasurementFragment)
    }


    private fun fillForm(testSession: TestSession) {

        binding.umfSnowTemperature.setText(testSession.snowTemperature.toString())
        binding.umfTemperature.setText(testSession.airTemperature.toString())

        binding.umfTwDate.text = getDateFormatString(testSession.datetime,"MM d yyyy")
        binding.umfTwTime.text = getDateFormatString(testSession.datetime,"h:mm")


        //todo dodělat picker atd + třída pro PickerDialog?
        // todo snowType
    }


    private fun updateItem() {
        // todo validace dat

        val updateItem = TestSession(
            args.testSession.id,
            datetime,
            binding.umfTemperature.text.toString().toDouble(),
            binding.umfTwDate.text.toString().toDouble(),
            snowType,
            null
        )

        viewModel.update(updateItem)

        Toast.makeText(requireContext(), getString(R.string.update_success_message), Toast.LENGTH_SHORT).show()
        findNavController().navigate(R.id.action_measurementUpdateFragment_to_MeasurementFragment)
    }


}