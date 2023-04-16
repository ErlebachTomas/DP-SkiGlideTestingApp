package cz.erlebach.skitesting.fragments.recommendation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import cz.erlebach.skitesting.fragments.template.MyViewModelFactory
import cz.erlebach.skitesting.databinding.FragmentRecommendationFirstBinding
import cz.erlebach.skitesting.network.RetrofitApiService
import cz.erlebach.skitesting.network.model.TestDataBody
import cz.erlebach.skitesting.repository.remote.RemoteServerRepository
import cz.erlebach.skitesting.common.utils.err
import cz.erlebach.skitesting.common.utils.lg
import cz.erlebach.skitesting.viewModel.remote.RemoteServerVM
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentRecommendationFirstBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRecommendationFirstBinding.inflate(inflater, container, false)


       binding.recApiCall.setOnClickListener {
           lg("recApiCall")
           lifecycleScope.launch(Dispatchers.IO) {
               val authManager = SessionManager.getInstance(requireContext())
               val token = authManager.fetchAuthToken()
               lg(token)

              // val url = RetrofitApiService.BASE_URL + "/api/getAllUsers"
               val url = RetrofitApiService.URL + "private"


               Fuel.get(url)
                   .authentication()
                   .bearer(token)
                   .responseJson { _, _, result ->
                       result.fold(success = { json ->
                           lg("Access token work, retrieve:")
                           lg(json.array().toString())

                       }, failure = { error ->
                           err(error.toString())
                       })
                   }

           }

       }
        binding.recapibtn2.setOnClickListener {
            // TODO api test
            val data = TestDataBody("testuju!!!")

            val repo = RemoteServerRepository(requireContext())
            val viewModelFactory = MyViewModelFactory(RemoteServerVM(repo))
            val viewModel = ViewModelProvider(this, viewModelFactory)[RemoteServerVM::class.java]

            viewModel.testPost(data)

            viewModel.get2Response()
            viewModel.res2.observe(viewLifecycleOwner, Observer { response ->
                if(response.isSuccessful){
                    lg("get2 retrieve:")
                    lg( response.body().toString())
                    lg( response.code().toString())
                    lg(response.headers().toString())
                } else {
                    err(response.message())
                }
            })

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