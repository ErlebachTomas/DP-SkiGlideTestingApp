package cz.erlebach.skitesting.fragments.skiProfile

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cz.erlebach.skitesting.R
import cz.erlebach.skitesting.common.template.MyViewModelFactory
import cz.erlebach.skitesting.common.utils.lg
import cz.erlebach.skitesting.common.utils.toast
import cz.erlebach.skitesting.databinding.FragmentSkiListBinding
import cz.erlebach.skitesting.repository.SkiRepository
import cz.erlebach.skitesting.viewModel.SkiVM


class SkiListFragment : Fragment() {

    private lateinit var viewModel: SkiVM

    private var _binding: FragmentSkiListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSkiListBinding.inflate(inflater, container, false)

        initVM()

        binding.fslBtnAddSki.setOnClickListener {
            // přepnutí fragmentu na vkládání přes nav
            findNavController().navigate(R.id.action_skiListFragment_to_addSkiFragment)
        }

        binding.fslBtnBack.setOnClickListener {
            // ukončit aktivitu
            activity?.finish()

        }
        binding.fslBtnDelete.setOnClickListener {
            deleteAllItems()
        }

        return binding.root
    }

    private fun initVM() {

        val adapter = SkiListAdapter()

        val recyclerView = binding.fslRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        val viewModelFactory = MyViewModelFactory(SkiVM(SkiRepository(requireContext())))

        try {
            viewModel = ViewModelProvider(this, viewModelFactory)[SkiVM::class.java]
            viewModel.data.observe(viewLifecycleOwner) { resource ->
              resource.data?.let {
                    adapter.setData(it) }
                }

        } catch (err: IllegalStateException) {
                toast(requireContext(), err.message.toString())
       } catch (E: Exception) {
           toast(requireContext(), E.message.toString())
        }


    }

    /** vymazat vše */
    private fun deleteAllItems() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton(getString(R.string.Yes)) { _, _ ->

             viewModel.deleteAll()

            Toast.makeText(requireContext(), getString(R.string.delete_success_message), Toast.LENGTH_SHORT).show()

        }
        builder.setNegativeButton(R.string.No) {_, _ ->}

        builder.setTitle( getString(R.string.delete))
        builder.setMessage(getString(R.string.delete_everything))
        builder.create().show()
    }


}