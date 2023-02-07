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
import androidx.navigation.fragment.findNavController
import cz.erlebach.skitesting.R
import cz.erlebach.skitesting.databinding.FragmentMeasurementFormBinding
import cz.erlebach.skitesting.model.TestSession
import cz.erlebach.skitesting.common.utils.generateDateISO8601string
import cz.erlebach.skitesting.viewModel.local.TestSessionVM
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
     * Naplní volice
     */
    private fun createSpinners() {
        val adapterProfile = ArrayAdapter.createFromResource(requireContext(),R.array.snowType, android.R.layout.simple_spinner_dropdown_item)
        binding.mfSnowSpinner.adapter = adapterProfile

        binding.mfTestTypeSpinner.adapter = ArrayAdapter.createFromResource(requireContext(),R.array.snowType, android.R.layout.simple_spinner_dropdown_item)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createSpinners()

        binding.mfBtnSave.setOnClickListener {

            Log.d(TAG, "mfBtnSave")

            try {
                saveForm()
            } catch (e: Exception) {
                Log.e(TAG,e.toString())
            }

        }
        setPickers()

        binding.mfBtnBack.setOnClickListener {
            activity?.finish() //ukončí aktivitu
        }

    }

    /**
     * Nastavení výběru data a času
     */
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
        val snowTemperature = binding.mfSnowTemperature.text.toString().toDouble()
        val humidity = binding.mfHumidity.text.toString().toDouble()

        val snowType = binding.mfSnowSpinner.selectedItemPosition //text pak .selectedItem.toString()
        val testType = binding.mfTestTypeSpinner.selectedItemPosition



        Log.v(  TAG, "Zadano: $airTemperature, $snowTemperature, " + binding.mfSnowSpinner.selectedItem.toString() )

        if(!TextUtils.isEmpty(binding.mfTemperature.text)
            && !TextUtils.isEmpty(binding.mfSnowTemperature.text)
            && !TextUtils.isEmpty(binding.mfSnowSpinner.selectedItem.toString())
        )  {
            // kontrola vyplnění

            val testSession = TestSession(UUID.randomUUID().toString(),
                datetime.time,
                airTemperature,
                snowTemperature,
                snowType,
                testType,
                humidity,
                binding.mfNote.toString(),
                generateDateISO8601string()
            )

            CoroutineScope(Dispatchers.IO).launch() {

                val id: String = testSessionVM.add(testSession) //uloží do db
                Log.v(TAG, "Měření uložono jako $id")

                withContext(Dispatchers.Main) {
                    // musi udělat main thread až po uloženi
                    val action = MeasurementFormFragmentDirections.actionMeasurementFormFragmentToAddSkiRideFragment(id)
                    findNavController().navigate(action)
                }

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

            Log.v(TAG,"set time $hh:$mm")

            val format = SimpleDateFormat("MMMM d, yyyy h:mm")
            tw.text =  format.format(datetime.time)

        }, hour, minute, true)

        tp.show()

    }

}