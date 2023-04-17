package cz.erlebach.skitesting.fragments.recommendation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import cz.erlebach.skitesting.R
import cz.erlebach.skitesting.common.utils.err
import cz.erlebach.skitesting.common.utils.lg
import cz.erlebach.skitesting.databinding.FragmentRecommendationFirstBinding
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

        binding.btnSend.setOnClickListener {

            val repo = RemoteServerRepository(requireContext())
            val viewModelFactory = MyViewModelFactory(RemoteServerVM(repo))
            val viewModel = ViewModelProvider(this, viewModelFactory)[RemoteServerVM::class.java]


            val test = TestSession(
              datetime = Date(),
              airTemperature = 0.0,
              snowTemperature = 0.0,
              snowType = 1,
              testType = 1,
              humidity = 80.0,
               note= "input"
            );
            viewModel.recomendacion(test)

            viewModel.recommendationLiveData.observe(viewLifecycleOwner, Observer { response ->
                if(response.isSuccessful){
                    lg("retrieve:")
                    lg( response.body().toString())
                    lg( response.code().toString())
                    lg(response.headers().toString())
                } else {
                    err(response.message())
                }
            })

            //findNavController().navigate(R.id.action_formFragment_to_resultFragment)

        }

        return binding.root
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