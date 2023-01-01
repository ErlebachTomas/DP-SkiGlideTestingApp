package cz.erlebach.skitesting.fragments.recommendation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.authentication
import com.github.kittinunf.fuel.json.responseJson
import cz.erlebach.skitesting.R
import cz.erlebach.skitesting.common.SessionManager
import cz.erlebach.skitesting.common.template.MyViewModelFactory
import cz.erlebach.skitesting.databinding.FragmentRecommendationFirstBinding
import cz.erlebach.skitesting.network.RetrofitApiService
import cz.erlebach.skitesting.network.TestData
import cz.erlebach.skitesting.repository.RemoteServerRepository
import cz.erlebach.skitesting.utils.err
import cz.erlebach.skitesting.utils.log
import cz.erlebach.skitesting.viewModel.RemoteServerVM
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentRecommendationFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRecommendationFirstBinding.inflate(inflater, container, false)


       binding.recApiCall.setOnClickListener {
           log("recApiCall")
           lifecycleScope.launch(Dispatchers.IO) {
               val authManager = SessionManager(requireContext())
               val token = authManager.fetchAuthToken()
               log(token)

               val url = RetrofitApiService.BASE_URL + "/api/getAllUsers"

               Fuel.get(url)
                   .authentication()
                   .bearer(token)
                   .responseJson { _, _, result ->
                       result.fold(success = { json ->
                           log("Access token work, retrieve:")
                           log(json.array().toString())

                       }, failure = { error ->
                           err(error.toString())
                       })
                   }

           }

       }
        binding.recapibtn.setOnClickListener {
            // TODO api test
            log("====== TEST =====")
            val data = TestData("testuju!!!")

            val viewModelFactory = MyViewModelFactory(RemoteServerRepository(requireContext()))
            val viewModel = ViewModelProvider(this, viewModelFactory)[RemoteServerVM::class.java]

            viewModel.testPost(data)

            viewModel.get2Response()
            viewModel.res2.observe(viewLifecycleOwner, Observer { response ->
                if(response.isSuccessful){
                    log( response.body().toString())
                    log( response.code().toString())
                    log(response.headers().toString())
                } else {
                    err(response.message())
                }
            })

            viewModel.res.value?.data?.let { log(it) }

        }
        return binding.root

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
        binding.rcBtnBack.setOnClickListener {
            activity?.finish()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}