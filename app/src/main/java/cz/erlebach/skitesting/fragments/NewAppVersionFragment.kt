package cz.erlebach.skitesting.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import cz.erlebach.skitesting.databinding.FragmentNewApiVersionBinding

/**
 * A simple [Fragment] subclass.
 * Use the [NewAppVersionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewAppVersionFragment : Fragment() {

    private var _binding: FragmentNewApiVersionBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =  FragmentNewApiVersionBinding.inflate(inflater, container, false)

        binding.fnavBtnClose.setOnClickListener {
           activity?.finish()
        }
        val info = requireArguments().getString("info")
        binding.fnavInfo.text = info.toString()

        return binding.root

    }


}