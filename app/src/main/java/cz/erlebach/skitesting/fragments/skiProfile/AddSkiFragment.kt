package cz.erlebach.skitesting.fragments.skiProfile

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import cz.erlebach.skitesting.R
import cz.erlebach.skitesting.common.SessionManager
import cz.erlebach.skitesting.common.template.MyViewModelFactory
import cz.erlebach.skitesting.model.Ski
import cz.erlebach.skitesting.repository.remote.SkiRemoteRepository
import cz.erlebach.skitesting.utils.generateDateISO8601string
import cz.erlebach.skitesting.viewModel.local.SkiVM
import cz.erlebach.skitesting.viewModel.remote.SkiRemoteVM
import kotlin.random.Random


/**
Fragment obsahujuící formulář pro přidání profilu Lyže
 */
class AddSkiFragment : Fragment() {

    //todo private lateinit var skiViewModel: SkiVM // ViewModel pro práci s db
    private lateinit var skiRemoteViewModel: SkiRemoteVM

    lateinit var myView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        myView = inflater.inflate(R.layout.fragment_ski_add_ski, container, false)

        //todo skiViewModel = ViewModelProvider(this).get(SkiVM::class.java)
        initVM()


        myView.findViewById<View>(R.id.fas_btnSave).setOnClickListener {

            sendForm()
        }
        return myView

    }
    /**
     * Uloží formulář do db
     */
    private fun sendForm() {
        val context = requireContext()

        val name = myView.findViewById<TextView>(R.id.fas_tx_name).text.toString()

        //kontrola správného vyplnění polí
        if(!TextUtils.isEmpty(name))  {

            val id = Random.nextInt(0, Int.MAX_VALUE) // todo vyřešit jinak, zatím pro test
            val ski = Ski(id,name, null, generateDateISO8601string()) //todo description

            // todo skiViewModel.addSki(ski)
            skiRemoteViewModel.insert(ski)

            Toast.makeText(context, context.getString(R.string.success), Toast.LENGTH_LONG).show()

            findNavController().navigate(R.id.action_addSkiFragment_to_skiListFragment) // návrat zpět na fragment s výpisem

        } else {
            Toast.makeText(context, context.getString(R.string.errEmptyFormField), Toast.LENGTH_LONG).show()
        }

    }

    private fun initVM() {

        val account = SessionManager.getInstance(requireContext())

        val repository = SkiRemoteRepository(requireContext())
        val viewModelFactory = MyViewModelFactory(SkiRemoteVM(repository,account))
        skiRemoteViewModel = ViewModelProvider(this, viewModelFactory)[SkiRemoteVM::class.java]

    }


}