package cz.erlebach.skitesting.fragments.measurement

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import cz.erlebach.skitesting.R
import cz.erlebach.skitesting.common.utils.date.getDateFormatString
import cz.erlebach.skitesting.databinding.AdapterFragmentMeasurementSkiRideListBinding
import cz.erlebach.skitesting.model.SkiRide
import cz.erlebach.skitesting.model.wrappers.SkiRideWithSki
import cz.erlebach.skitesting.viewModel.SkiRideVM


/**
 * [RecyclerView.Adapter] který zobrazuje [SkiRideWithSki] (obalová třída [SkiRide]) pro [SkiRideListFragment].
 */
class SkiRideListAdapter(
    val viewModel: SkiRideVM
) : RecyclerView.Adapter<SkiRideListAdapter.ViewHolder>() {

    private var values: List<SkiRideWithSki> = emptyList<SkiRideWithSki>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            AdapterFragmentMeasurementSkiRideListBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )

    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentSkiRideWithSki = values[position]

        val skiName = currentSkiRideWithSki.ski.name
        val result = currentSkiRideWithSki.skiRide.result
        val note = currentSkiRideWithSki.skiRide.note

        holder.noteTV.text = note
        holder.resultTV.text = result.toString()
        holder.skiNameTV.text = skiName

        holder.timestampTV.text = getDateFormatString(currentSkiRideWithSki.skiRide.updatedAt)

        holder.itemView.findViewById<View>(R.id.adap_layout_skiRide_row).setOnClickListener {
            val action = SkiRideListFragmentDirections.actionSkiRideListFragmentToUpdateSkiRideFragment(
                currentSkiRideWithSki.skiRide
            )
            holder.itemView.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: AdapterFragmentMeasurementSkiRideListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val noteTV: TextView = binding.srlContent
        val skiNameTV: TextView = binding.slrSkiName
        val resultTV: TextView = binding.slrSkiResult
        val timestampTV : TextView = binding.slrTimestamp

        override fun toString(): String {
            return super.toString() + " '" + noteTV.text + "'"
        }
    }

    fun setData(list: List<SkiRideWithSki>) {
        this.values = list
        notifyDataSetChanged()
    }

}