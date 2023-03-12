package cz.erlebach.skitesting.fragments.measurement

import android.annotation.SuppressLint
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
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import cz.erlebach.skitesting.R
import cz.erlebach.skitesting.common.template.MyViewModelFactory
import cz.erlebach.skitesting.common.utils.debug
import cz.erlebach.skitesting.databinding.FragmentMeasurementFormBinding
import cz.erlebach.skitesting.model.TestSession
import cz.erlebach.skitesting.common.utils.generateDateISO8601string
import cz.erlebach.skitesting.common.utils.lg
import cz.erlebach.skitesting.repository.TestSessionRepository
import cz.erlebach.skitesting.viewModel.TestSessionVM
import cz.erlebach.skitesting.viewModel.local.TestSessionLocalVM
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
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

    val dateFormat = SimpleDateFormat("dd.MM-yyyy", Locale.getDefault())
    val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMeasurementFormBinding.inflate(inflater, container, false)

        testSessionVM = ViewModelProvider(this,
            factory= MyViewModelFactory(TestSessionVM(TestSessionRepository(requireContext())))
        )[TestSessionVM::class.java]

        return binding.root

    }

    /**
     * Naplní volice
     */
    private fun createSpinners() {
        val adapterProfile = ArrayAdapter.createFromResource(requireContext(),R.array.snowType, android.R.layout.simple_spinner_dropdown_item)
        binding.mfSnowSpinner.adapter = adapterProfile

        binding.mfTestTypeSpinner.adapter = ArrayAdapter.createFromResource(requireContext(),R.array.testType, android.R.layout.simple_spinner_dropdown_item)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createSpinners()

        binding.mfBtnSave.setOnClickListener {

            lg( "mfBtnSave")

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

        datetime = Calendar.getInstance() // get current time
        binding.mfTwDate.text = dateFormat.format(datetime.time)
        binding.mfTwTime.text = timeFormat.format(datetime.time)

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

        val airTemperature = binding.mfTemperature.text.toString()
        val snowTemperature = binding.mfSnowTemperature.text.toString()
        val humidity = binding.mfHumidity.text.toString()

        val snowType = binding.mfSnowSpinner.selectedItemPosition //text pak .selectedItem.toString()
        val testType = binding.mfTestTypeSpinner.selectedItemPosition

        lg(  "Zadano: $airTemperature, $snowTemperature, " + binding.mfSnowSpinner.selectedItem.toString() )

        if(!TextUtils.isEmpty(binding.mfTemperature.text)
            && !TextUtils.isEmpty(binding.mfSnowTemperature.text)
            && !TextUtils.isEmpty(binding.mfSnowSpinner.selectedItem.toString())
        )  {
            // kontrola vyplnění


            val testSession = TestSession(
                datetime = datetime.time,
                airTemperature = airTemperature.toDouble(),
                snowTemperature = snowTemperature.toDouble(),
                snowType = snowType,
                testType = testType,
                humidity= humidity.toDoubleOrNull(),
                note = binding.mfNote.text.toString(),
            )

            CoroutineScope(Dispatchers.IO).launch() {

                testSessionVM.insert(testSession) //uloží do db
                lg( "Měření uložono jako ${testSession.id}")

                withContext(Dispatchers.Main) {
                    // musi udělat main thread až po uloženi
                    val action = MeasurementFormFragmentDirections.actionMeasurementFormFragmentToAddSkiRideFragment(testSession.id)
                    findNavController().navigate(action)
                }

            }

        } else {
            lg("nejsou vyplněne všechny pole")

            val borderDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.my_btn_border)
            binding.mfTemperature.background = borderDrawable
            binding.mfSnowTemperature.background  = borderDrawable

            Toast.makeText(context, context?.getString(R.string.errEmptyFormField), Toast.LENGTH_LONG).show()
        }


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

            tw.text = dateFormat.format(datetime.time)

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
            tw.text =  timeFormat.format(datetime.time)

        }, hour, minute, true)

        tp.show()

    }

}