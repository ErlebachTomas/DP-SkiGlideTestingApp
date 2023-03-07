package cz.erlebach.skitesting.fragments.measurement

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import cz.erlebach.skitesting.R
import cz.erlebach.skitesting.common.template.MyViewModelFactory
import cz.erlebach.skitesting.databinding.FragmentMeasurementUpdateBinding
import cz.erlebach.skitesting.model.TestSession
import cz.erlebach.skitesting.common.utils.generateDateISO8601string
import cz.erlebach.skitesting.common.utils.getDateFormatString
import cz.erlebach.skitesting.repository.TestSessionRepository
import cz.erlebach.skitesting.viewModel.TestSessionVM
import cz.erlebach.skitesting.viewModel.local.TestSessionLocalVM
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



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMeasurementUpdateBinding.inflate(inflater, container, false)

        datetime = args.testSession.datetime

        binding.testType.text = resources.getStringArray(R.array.testType)[args.testSession.testType]

        viewModel = ViewModelProvider(this,
           factory=MyViewModelFactory(TestSessionVM(TestSessionRepository(requireContext())))
        )[TestSessionVM::class.java]

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

        val adapterProfile = ArrayAdapter.createFromResource(requireContext(),R.array.snowType, android.R.layout.simple_spinner_dropdown_item)
        binding.umfSnowSpinner.adapter = adapterProfile
        binding.umfSnowSpinner.setSelection(args.testSession.snowType)

        //todo dodělat picker atd + třída pro PickerDialog?
    }


    private fun updateItem() {

        if(TextUtils.isEmpty(binding.umfTemperature.text)
            && TextUtils.isEmpty(binding.umfSnowTemperature.text)) {
            Toast.makeText(context, context?.getString(R.string.errEmptyFormField), Toast.LENGTH_LONG).show()
            return
        }

        val updateItem = TestSession(
            args.testSession.id,
            datetime,
            binding.umfTemperature.text.toString().toDouble(),
            binding.umfTwDate.text.toString().toDouble(),
            binding.umfSnowSpinner.selectedItemPosition,
            args.testSession.testType,
            args.testSession.humidity,
            args.testSession.note,
            generateDateISO8601string()
        ) //todo dodělat update pole

        viewModel.update(updateItem)

        Toast.makeText(requireContext(), getString(R.string.update_success_message), Toast.LENGTH_SHORT).show()
        findNavController().navigate(R.id.action_measurementUpdateFragment_to_MeasurementFragment)
    }


}