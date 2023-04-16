package cz.erlebach.skitesting.fragments.recommendation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import cz.erlebach.skitesting.fragments.recommendation.recyclerview.ChildData
import cz.erlebach.skitesting.fragments.recommendation.recyclerview.ParentData
import cz.erlebach.skitesting.databinding.FragmentRecommendationResultBinding


class ResultFragment : Fragment() {

    private var _binding: FragmentRecommendationResultBinding? = null
    private val binding get() = _binding!!

    private val listData : MutableList<ParentData> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecommendationResultBinding.inflate(inflater, container, false)

        initDataTest()

        val adapter = ResultAdapter(requireContext(),listData)
        binding.rvResults.layoutManager = LinearLayoutManager(requireContext())
        binding.rvResults.adapter = adapter



        return binding.root
    }


    private fun initDataTest() {

        val parentData: Array<String> = arrayOf("3°C", "5°C", "-5°C", "0°C")

        val childDataData: MutableList<ChildData> = mutableListOf(
            ChildData("Rossignol"),
            ChildData("Salomon"),
            ChildData("K2"),
            ChildData("Fischer")
        )

        val parentObj = ParentData(text = parentData[0], subList = childDataData)
        listData.add(parentObj)

    }
}