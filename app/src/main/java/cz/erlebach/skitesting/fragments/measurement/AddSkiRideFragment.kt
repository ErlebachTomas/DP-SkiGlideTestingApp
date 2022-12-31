package cz.erlebach.skitesting.fragments.measurement

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import cz.erlebach.skitesting.R
import cz.erlebach.skitesting.activity.SkiProfileActivity
import cz.erlebach.skitesting.databinding.FragmentMeasurementAddSkiRideBinding
import cz.erlebach.skitesting.fragments.skiProfile.UpdateSkiFragmentArgs
import cz.erlebach.skitesting.model.Ski
import cz.erlebach.skitesting.model.SkiRide
import cz.erlebach.skitesting.viewModel.SkiRideVM
import cz.erlebach.skitesting.viewModel.SkiVM
import cz.erlebach.skitesting.viewModel.TestSessionVM
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class AddSkiRideFragment : Fragment() {

    private var _binding: FragmentMeasurementAddSkiRideBinding? = null
    private val binding get() = _binding!!

    private val TAG = "AddSkiRideFragment"

    private val args by navArgs<AddSkiRideFragmentArgs>()
    /**
     * Zvolená lyže pro měření
     */
    private lateinit var selectedSki: Ski //todo ošetření

    private lateinit var skiRideVM: SkiRideVM

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentMeasurementAddSkiRideBinding.inflate(inflater, container, false)

        skiRideVM = ViewModelProvider(this)[SkiRideVM::class.java]

        binding.srBtnSave.setOnClickListener {
            Log.v(TAG,"save")

            save()

        }

        binding.srBtnBack.setOnClickListener {
            Log.v(TAG,"back")
            activity?.finish() //todo changeFragmentTo
        }

        binding.srAddSki.setOnClickListener {
            //TODO start Ski Activity for result?
            val intent = Intent(activity, SkiProfileActivity::class.java)
            startActivity(intent)
        }

        return binding.root

    }

    private fun save() {

        val result = binding.mfResult.text
        val note = binding.mfNote.text

        if (!TextUtils.isEmpty(result)) {
            val ride = SkiRide(0,
                selectedSki.id,
                args.idTestSession,
                result.toString().toDouble(),
                note.toString(),
                null,
                ) //todo update

            CoroutineScope(Dispatchers.IO).launch() {
                skiRideVM.insert(ride)
            }
            Toast.makeText(context, R.string.success, Toast.LENGTH_SHORT).show()

            binding.mfResult.text = null
            binding.mfNote.text = null

            // todo next fragment?

        } else {
            Toast.makeText(context, context?.getString(R.string.errEmptyFormField), Toast.LENGTH_LONG).show()
        }


    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


         initSpinnerData()

    }


    private fun initSpinnerData() {

       val  skiViewModel = ViewModelProvider(
           this,
           ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application))[SkiVM::class.java]


       val allSkis = context?.let {
                ArrayAdapter<Ski>(it, R.layout.adapter_spinner_measurement_ski_row,R.id.mf_twSpinnerRow)
       }


        skiViewModel.readAllData
                .observe(viewLifecycleOwner) { skis ->

                    if (skis.isNotEmpty()) {
                        skis?.forEach { ski ->
                            allSkis?.add(ski)
                        }
                    } else {
                        Toast.makeText(requireContext(), "No skis",Toast.LENGTH_LONG)
                        //todo ošetření žádná lyže
                    }
          }


        binding.mfSkiSpinner.adapter = allSkis //todo bind spinner to custom object list https://stackoverflow.com/a/21169383

        binding.mfSkiSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedSki = parent?.selectedItem as Ski
                    Toast.makeText(requireContext(), selectedSki.name, Toast.LENGTH_SHORT).show()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
            }


        }



}

