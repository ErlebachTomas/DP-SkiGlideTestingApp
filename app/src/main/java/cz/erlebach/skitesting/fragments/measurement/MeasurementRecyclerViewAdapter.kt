package cz.erlebach.skitesting.fragments.measurement

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cz.erlebach.skitesting.R
import cz.erlebach.skitesting.databinding.AdapterFragmentMeasurementListContentBinding

import cz.erlebach.skitesting.model.TestSession
import cz.erlebach.skitesting.utils.getDateFormatString


/**
 * [RecyclerView.Adapter] který zobrazuje [TestSession].
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
        val item = values[position]
        holder.idView.text = item.id.toString()
        holder.contentView.text = getDateFormatString(item.datetime)

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