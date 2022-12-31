package cz.erlebach.skitesting.fragments.measurement

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.navigation.findNavController
import com.google.android.material.internal.ContextUtils.getActivity
import cz.erlebach.skitesting.R
import cz.erlebach.skitesting.activity.MeasurementActivity
import cz.erlebach.skitesting.databinding.AdapterFragmentMeasurementListContentBinding

import cz.erlebach.skitesting.model.TestSession
import cz.erlebach.skitesting.utils.getDateFormatString


/**
 * [RecyclerView.Adapter] který zobrazuje [TestSession] pro [MeasurementListFragment].
 */
class MeasurementRecyclerViewAdapter(
):RecyclerView.Adapter<MeasurementRecyclerViewAdapter.ViewHolder>() {

    private var values: List<TestSession> = emptyList<TestSession>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            AdapterFragmentMeasurementListContentBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val testSession = values[position]

        holder.idView.text = testSession.id.toString()
        holder.contentView.text = getDateFormatString(testSession.datetime)

        holder.itemView.findViewById<View>(R.id.adap_layout_measurement_row).setOnClickListener {

            val action = MeasurementListFragmentDirections.actionMeasurementFragmentToSkiRideListFragment(
                testSession.id
            )
            holder.itemView.findNavController().navigate(action)
        }

        holder.itemView.findViewById<View>(R.id.adap_layout_measurement_row).setOnLongClickListener {

         val action = MeasurementListFragmentDirections.actionMeasurementFragmentToMeasurementUpdateFragment(
             testSession)

             holder.itemView.findNavController().navigate(action)

            return@setOnLongClickListener true
        }

    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: AdapterFragmentMeasurementListContentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val idView: TextView = binding.itemNumber
        val contentView: TextView = binding.content

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }

    fun setData(list: List<TestSession>){
        this.values = list
        notifyDataSetChanged()
    }

}