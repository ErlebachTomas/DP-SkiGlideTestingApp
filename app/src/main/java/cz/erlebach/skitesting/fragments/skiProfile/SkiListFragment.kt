package cz.erlebach.skitesting.fragments.skiProfile

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cz.erlebach.skitesting.R
import cz.erlebach.skitesting.common.template.MyViewModelFactory
import cz.erlebach.skitesting.repository.remote.SkiRemoteRepository
import cz.erlebach.skitesting.utils.err

import cz.erlebach.skitesting.viewModel.local.SkiVM
import cz.erlebach.skitesting.viewModel.remote.SkiRemoteVM


class SkiListFragment : Fragment() {

    private lateinit var skiViewModel: SkiVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_ski_list, container, false)

        init(view) // nastaví adaptér

        view.findViewById<View>(R.id.fsl_btnAddSki).setOnClickListener {
            // přepnutí fragmentu na vkládání přes nav
            findNavController().navigate(R.id.action_skiListFragment_to_addSkiFragment)
        }

        view.findViewById<View>(R.id.fsl_btnBack).setOnClickListener {
            // ukončit aktivitu
            activity?.finish()

        }
        view.findViewById<View>(R.id.fsl_btnDelete).setOnClickListener {
            deleteAllItems()
        }


        return view
    }

    /** Inicializace vm a adaptéru */
    private fun init(view : View ) {

        val userID = "auth0|62c3317067fdea356d289028" // todo get UserID
        // todo check internet connection

        val repository = SkiRemoteRepository(requireContext())
        val viewModelFactory = MyViewModelFactory(SkiRemoteVM(repository,userID))
        val viewModel = ViewModelProvider(this, viewModelFactory)[SkiRemoteVM::class.java]

        //viewModel.fetchData()

        val adapter = SkiListAdapter()

        val recyclerView = view.findViewById<RecyclerView>(R.id.fsl_recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        viewModel.data.observe(viewLifecycleOwner, Observer { response ->

            if(response.isSuccessful){
                response.body()?.let { adapter.setData(it) }
                } else {
                    err(response.message())
                }
            })
        }

    /** Načte z ROOM */
    private fun localStorage(view : View) {

        skiViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        ).get(
            SkiVM::class.java
        )

        val adapter = SkiListAdapter()

        val recyclerView = view.findViewById<RecyclerView>(R.id.fsl_recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter


        skiViewModel.readAllData.observe(viewLifecycleOwner) { ski ->
            adapter.setData(ski)
        }

    }


    /** vymazat vše */
    private fun deleteAllItems() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton(getString(R.string.Yes)) { _, _ ->

            skiViewModel.deleteAll()

            Toast.makeText(requireContext(), getString(R.string.delete_success_message), Toast.LENGTH_SHORT).show()

        }
        builder.setNegativeButton(R.string.No) {_, _ ->}

        builder.setTitle( getString(R.string.delete))
        builder.setMessage(getString(R.string.delete_everything))
        builder.create().show()
    }


}