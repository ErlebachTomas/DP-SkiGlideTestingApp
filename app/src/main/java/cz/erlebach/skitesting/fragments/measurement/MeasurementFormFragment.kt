package cz.erlebach.skitesting.fragments.measurement

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import cz.erlebach.skitesting.R
import cz.erlebach.skitesting.databinding.FragmentMeasurementFormBinding
import cz.erlebach.skitesting.fragments.skiProfile.SkiListFragmentDirections
import cz.erlebach.skitesting.model.Ski
import cz.erlebach.skitesting.model.TestSession
import cz.erlebach.skitesting.viewModel.SkiVM
import cz.erlebach.skitesting.viewModel.TestSessionVM
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


/**
 Formulář pro nastavení nového měření
 */
class MeasurementFormFragment : Fragment() {

    private val TAG = "mf"

    private var _binding: FragmentMeasurementFormBinding? = null
    private val binding get() = _binding!!

    private var datetime: Calendar = GregorianCalendar(TimeZone.getDefault())
    private lateinit var testSessionVM: TestSessionVM

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMeasurementFormBinding.inflate(inflater, container, false)

        testSessionVM = ViewModelProvider(this)[TestSessionVM::class.java]

        return binding.root

    }

    /**
     * Naplní nabídku
     */
    private fun createSpinners() {
        val adapterProfile = ArrayAdapter.createFromResource(requireContext(),R.array.snowType, android.R.layout.simple_spinner_dropdown_item)
        binding.mfSnowSpinner.adapter = adapterProfile
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createSpinners()

        binding.mfBtnSave.setOnClickListener {
            saveForm()
            findNavController().navigate(R.id.action_measurementFormFragment_to_addSkiRideFragment)
        }

        setPickers()

        binding.mfBtnBack.setOnClickListener {
            activity?.finish() //ukončí aktivitu
        }
    }

    private fun setPickers() {
        binding.mfBtnDatePicker.setOnClickListener {
            showDatePickerDialog(binding.mfTwDate)
        }
        binding.mfBtnTimePicker.setOnClickListener {
            showTimePickerDialog(binding.mfTwTime)
        }

    }

    /**
     * uloží měření do db
     */
    private fun saveForm() {

        val airTemperature = binding.mfTemperature.text.toString().toDouble()
        val snowTemperature = binding.mfSnowTemperature.toString().toDouble()

        val snowType = binding.mfSnowSpinner.selectedItem.toString() //todo předělat, zatím jen pro testování

        if(!TextUtils.isEmpty(binding.mfTemperature.text)
            && !TextUtils.isEmpty(binding.mfSnowTemperature.text)
            && !TextUtils.isEmpty(binding.mfSnowSpinner.selectedItem.toString())
        )  {
            // kontrola vyplnění
            val testSesion = TestSession(0,
                datetime.time,
                airTemperature,
                snowTemperature,
                snowType,
                null)

            CoroutineScope(Dispatchers.IO).launch() {

                val id: Long = testSessionVM.add(testSesion) //uloží do db
                Toast.makeText(context, context?.getString(R.string.success) , Toast.LENGTH_LONG).show()

                val action = MeasurementFormFragmentDirections.actionMeasurementFormFragmentToAddSkiRideFragment(id)
                findNavController().navigate(action)
            }

        } else
            Toast.makeText(context, context?.getString(R.string.errEmptyFormField), Toast.LENGTH_LONG).show()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Výběr data
     */
    private fun showDatePickerDialog(tw:TextView) {

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { _, myear, mmonth, mday ->

            datetime.set(myear,mmonth,mday)

            //todo smazat ?
            /*
            val monthOfYear = mmonth+1
            val date = "$mday/$monthOfYear/$myear"
            tw.text = date
             */

            datetime.get(Calendar.YEAR)
            datetime.get(Calendar.MONTH)

            val format = SimpleDateFormat("MMMM d, yyyy h:mm")
            format.format(datetime.time)

            tw.text = format.format(datetime.time)

        }, year, month, day)

        dpd.show()
    }

    /**
     * Výběr času
     */
    private fun showTimePickerDialog(tw:TextView) {
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        val tp  = TimePickerDialog(requireContext(), TimePickerDialog.OnTimeSetListener { _, hh, mm ->

            datetime.set(Calendar.MINUTE,mm)
            datetime.set(Calendar.HOUR, hh)

            Log.v("time","$hh:$mm")
            // tw.text = "$hh:$mm"

            val format = SimpleDateFormat("MMMM d, yyyy h:mm")
            tw.text =  format.format(datetime.time)

        }, hour, minute, true)

        tp.show()

    }

}