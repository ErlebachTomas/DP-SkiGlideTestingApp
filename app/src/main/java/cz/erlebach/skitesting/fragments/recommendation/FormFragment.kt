package cz.erlebach.skitesting.fragments.recommendation

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.text.set
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import cz.erlebach.skitesting.R
import cz.erlebach.skitesting.common.utils.err
import cz.erlebach.skitesting.common.utils.lg
import cz.erlebach.skitesting.common.utils.toast
import cz.erlebach.skitesting.databinding.FragmentRecommendationFormBinding
import cz.erlebach.skitesting.fragments.template.MyViewModelFactory
import cz.erlebach.skitesting.model.TestSession
import cz.erlebach.skitesting.repository.remote.RemoteServerRepository
import cz.erlebach.skitesting.viewModel.remote.RemoteServerVM
import java.util.Date

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class FormFragment : Fragment() {
    private var _binding: FragmentRecommendationFormBinding? = null
    private val binding get() = _binding!!
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRecommendationFormBinding.inflate(inflater, container, false)
        createSpinners()

        binding.rcBtnSend.setOnClickListener {
          sendForm()
        }
        binding.rcSkip.setOnClickListener {
            binding.rcTemperature.setText("-1")
            binding.rcSnowTemperature.setText("0")
            sendForm()
        }

        binding.rcBack.setOnClickListener {
            activity?.finish()
        }


        return binding.root
    }

    private fun sendForm() {

        val airTemperature = binding.rcTemperature.text.toString()
        val snowTemperature = binding.rcSnowTemperature.text.toString()

        var humidity : Double? = null
        if (!TextUtils.isEmpty(binding.rcHumidity.text)) {
            humidity = binding.rcHumidity.text.toString().toDouble()
        }

        val snowType = binding.rcSnowSpinner.selectedItemPosition

        if(!TextUtils.isEmpty(binding.rcTemperature.text)
            && !TextUtils.isEmpty(binding.rcSnowTemperature.text)
            && !TextUtils.isEmpty(binding.rcSnowSpinner.selectedItem.toString())
        )  {

        val test = TestSession(
            datetime = Date(),
            airTemperature = airTemperature.toDouble(),
            snowTemperature = snowTemperature.toDouble(),
            snowType = snowType,
            testType = 1,
            humidity = humidity,
            note= "input"
        )
        val action = FormFragmentDirections.actionFormFragmentToResultFragment(test)
        findNavController().navigate(action)

        } else {
            lg("nejsou vyplněne všechny pole")
            val borderDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.my_btn_border)
            binding.rcTemperature.background = borderDrawable
            binding.rcSnowTemperature.background  = borderDrawable

            Toast.makeText(context, context?.getString(R.string.errEmptyFormField), Toast.LENGTH_LONG).show()
        }




    }

    private fun createSpinners() {
        val adapterProfile = ArrayAdapter.createFromResource(requireContext(),R.array.snowType, android.R.layout.simple_spinner_dropdown_item)
        binding.rcSnowSpinner.adapter = adapterProfile
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment fragment_recommendation_form.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FormFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}