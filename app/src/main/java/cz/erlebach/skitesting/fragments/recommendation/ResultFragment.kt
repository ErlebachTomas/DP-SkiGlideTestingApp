package cz.erlebach.skitesting.fragments.recommendation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import cz.erlebach.skitesting.R
import cz.erlebach.skitesting.common.utils.debug
import cz.erlebach.skitesting.common.utils.err
import cz.erlebach.skitesting.common.utils.info
import cz.erlebach.skitesting.common.utils.toast
import cz.erlebach.skitesting.common.utils.wtf
import cz.erlebach.skitesting.fragments.recommendation.recyclerview.Group
import cz.erlebach.skitesting.databinding.FragmentRecommendationResultBinding
import cz.erlebach.skitesting.fragments.template.MyViewModelFactory
import cz.erlebach.skitesting.network.model.recomendation.RecommendationDataBody
import cz.erlebach.skitesting.repository.remote.RemoteServerRepository
import cz.erlebach.skitesting.viewModel.remote.RemoteServerVM


class ResultFragment : Fragment() {

    private var _binding: FragmentRecommendationResultBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<ResultFragmentArgs>()

    private val listData : MutableList<Group> = ArrayList()
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecommendationResultBinding.inflate(inflater, container, false)
        progressBar = binding.rvProgressBar

        load()

        return binding.root
    }


    private fun noData() {
        toast(requireActivity(), getString(R.string.no_result_data))
        findNavController().navigate(ResultFragmentDirections.actionResultFragmentToFormFragment())
    }

    private fun load() {
        progressBar.visibility = View.VISIBLE

        val adapter = ResultAdapter(requireContext())
        binding.rvResults.layoutManager = LinearLayoutManager(requireContext())
        binding.rvResults.adapter = adapter

        val repo = RemoteServerRepository(requireContext())
        val viewModelFactory = MyViewModelFactory(RemoteServerVM(repo))
        val viewModel = ViewModelProvider(this, viewModelFactory)[RemoteServerVM::class.java]

        try {
            viewModel.calculateRecommendation(args.test)

            viewModel.recommendationLiveData.observe(viewLifecycleOwner, Observer { response ->

                if (response.isSuccessful) {
                    info(response.body().toString())
                    when (response.code()) {
                        200 -> {
                            val array = response.body()!!
                            for (item in array) {
                                debug("responce " + item.testData.UUID)
                                val grp = Group(data = item, subList = item.skiResults.toMutableList())
                                listData.add(grp)
                                adapter.setData(listData)
                            }
                            progressBar.visibility = View.GONE
                        }
                        210 -> {
                            progressBar.visibility = View.GONE
                            noData()
                        }
                        else -> {
                           progressBar.visibility = View.GONE
                           toast(requireContext(),response.message().toString())
                        }
                    }
                } else {
                    toast(requireContext(),getString(R.string.connection_error))
                    err("Connection errror")
                    err(response.errorBody().toString())
                    err(response.raw().toString())

                }
            })
        } catch (e: Error) {
            wtf("fail",e)
        }
    }

}