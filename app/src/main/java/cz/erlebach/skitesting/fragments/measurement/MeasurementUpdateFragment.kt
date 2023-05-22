package cz.erlebach.skitesting.fragments.measurement

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import cz.erlebach.skitesting.R
import cz.erlebach.skitesting.common.template.MyViewModelFactory
import cz.erlebach.skitesting.databinding.FragmentMeasurementUpdateBinding
import cz.erlebach.skitesting.model.TestSession
import cz.erlebach.skitesting.common.utils.date.generateDateISO8601string
import cz.erlebach.skitesting.common.utils.date.getDateFormatString
import cz.erlebach.skitesting.repository.TestSessionRepository
import cz.erlebach.skitesting.viewModel.TestSessionVM
import java.text.SimpleDateFormat
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
    private var calendar : Calendar = GregorianCalendar(TimeZone.getDefault())
    private val dateFormat = SimpleDateFormat("dd.MM-yyyy", Locale.getDefault())
    private val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMeasurementUpdateBinding.inflate(inflater, container, false)

        datetime = args.testSession.datetime
        calendar.time = datetime

        binding.testType.text = resources.getStringArray(R.array.testType)[args.testSession.testType]

        viewModel = ViewModelProvider(this,
           factory= MyViewModelFactory(TestSessionVM(TestSessionRepository(requireContext())))
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
        Toast.makeText(requireContext(), R.string.success, Toast.LENGTH_LONG).show()

        Handler(Looper.getMainLooper()).postDelayed({
            findNavController().navigate(R.id.action_measurementUpdateFragment_to_MeasurementFragment)
        }, 500)
    }


    private fun fillForm(testSession: TestSession) {

        binding.umfSnowTemperature.setText(testSession.snowTemperature.toString())
        binding.umfTemperature.setText(testSession.airTemperature.toString())

        binding.umfTwDate.text = getDateFormatString(testSession.datetime,"MM d yyyy")
        binding.umfTwTime.text = getDateFormatString(testSession.datetime,"h:mm")

        val adapterProfile = ArrayAdapter.createFromResource(requireContext(),R.array.snowType, android.R.layout.simple_spinner_dropdown_item)
        binding.umfSnowSpinner.adapter = adapterProfile
        binding.umfSnowSpinner.setSelection(args.testSession.snowType)

        binding.umfNote.setText(args.testSession.note)
        setPickers()

    }
    private fun setPickers() {

        binding.umfBtnDatePicker.setOnClickListener {
            showDatePickerDialog(binding.umfTwDate)
        }
        binding.umfBtnTimePicker.setOnClickListener {
            showTimePickerDialog(binding.umfTwTime)
        }

    }
    private fun showDatePickerDialog(tw: TextView) {

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(requireContext(), { _, myear, mmonth, mday ->

            calendar.set(myear, mmonth, mday)

            tw.text = dateFormat.format(calendar.time)

        }, year, month, day)

        dpd.show()
    }
    private fun showTimePickerDialog(tw:TextView) {
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        val tp  = TimePickerDialog(requireContext(), { _, hh, mm ->

            calendar.set(Calendar.MINUTE,mm)
            calendar.set(Calendar.HOUR, hh)

            tw.text =  timeFormat.format(calendar.time)

        }, hour, minute, true)

        tp.show()

    }

    private fun updateItem() {

        if(TextUtils.isEmpty(binding.umfTemperature.text)
            && TextUtils.isEmpty(binding.umfSnowTemperature.text)) {
            Toast.makeText(context, context?.getString(R.string.errEmptyFormField), Toast.LENGTH_LONG).show()
            return
        }

        val id = args.testSession.id
        val date = calendar.time
        val temperature = binding.umfTemperature.text.toString().toDouble()

        val updateItem = TestSession(
            id=id,
            datetime = date,
            airTemperature = temperature,
            snowTemperature = binding.umfSnowTemperature.text.toString().toDouble(),
            snowType = binding.umfSnowSpinner.selectedItemPosition,
            testType = args.testSession.testType,
            humidity = args.testSession.humidity,
            note = binding.umfNote.text.toString(),
            updatedAt = generateDateISO8601string()
        )
        viewModel.update(updateItem)

        Toast.makeText(requireContext(), getString(R.string.update_success_message), Toast.LENGTH_SHORT).show()
        findNavController().navigate(R.id.action_measurementUpdateFragment_to_MeasurementFragment)
    }


}