package cz.erlebach.skitesting.fragments.skiProfile

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import cz.erlebach.skitesting.R
import cz.erlebach.skitesting.common.template.MyViewModelFactory
import cz.erlebach.skitesting.databinding.FragmentMeasurementAddSkiRideBinding
import cz.erlebach.skitesting.databinding.FragmentSkiAddSkiBinding
import cz.erlebach.skitesting.model.Ski
import cz.erlebach.skitesting.repository.SkiRepository
import cz.erlebach.skitesting.viewModel.SkiVM


/**
Fragment obsahujuící formulář pro přidání profilu Lyže
 */
class AddSkiFragment : Fragment() {

    private var _binding: FragmentSkiAddSkiBinding? = null
    private val binding get() = _binding!!

    lateinit var viewModel : SkiVM



     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSkiAddSkiBinding.inflate(inflater, container, false)


        val viewModelFactory = MyViewModelFactory(SkiVM(SkiRepository(requireContext())))
        viewModel = ViewModelProvider(this, viewModelFactory)[SkiVM::class.java]

        binding.fasBtnSave.setOnClickListener {

            sendForm()
        }
        return binding.root

    }
    /**
     * Uloží formulář do db
     */

    private fun sendForm() {
        val context = requireContext()

        val name = binding.fasBtnSave.text.toString()


        //kontrola správného vyplnění polí
        if(!TextUtils.isEmpty(name))  {

            val ski = Ski(name= name)
             viewModel.insert(ski)

            Toast.makeText(context, context.getString(R.string.success), Toast.LENGTH_LONG).show()

            findNavController().navigate(R.id.action_addSkiFragment_to_skiListFragment) // návrat zpět na fragment s výpisem

        } else {
            val borderDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.my_btn_border)
            binding.fasBtnSave.background = borderDrawable
            Toast.makeText(context, context.getString(R.string.errEmptyFormField), Toast.LENGTH_LONG).show()
        }

    }
}