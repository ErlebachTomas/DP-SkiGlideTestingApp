package cz.erlebach.skitesting.fragments.skiProfile

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import cz.erlebach.skitesting.R

import cz.erlebach.skitesting.model.Ski

class SkiListAdapter(): RecyclerView.Adapter<SkiListAdapter.MyViewHolder>(){

    private var myList = emptyList<Ski>()

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        // todo jednotlivé položky
        //val name:TextView  = itemView.findViewById(R.id.adap_tx_name)

        /**
         * Nastaví data v jednotlivém řádku recyclerViewu
         */
        fun blindSki(ski: Ski) {
            val name:TextView = itemView.findViewById(R.id.adap_tx_name)
            name.text = ski.name

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.adapter_fragment_ski_list_row, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = myList[position] // Lyže

        // todo jednotlivé položky
        //holder.itemView.findViewById<TextView>(R.id.adap_tx_name).text = currentItem.name
        //holder.name.text = currentItem.name

        holder.blindSki(currentItem)


        //todo lisenery (tlacitka a layout long)
        holder.itemView.findViewById<View>(R.id.adap_layout_skiRow).setOnClickListener() {
            //Toast.makeText(holder.itemView.context, "click " + currentItem.id, Toast.LENGTH_SHORT).show()
            Log.v("click", "click " + currentItem.name)
            
           val action = SkiListFragmentDirections.actionSkiListFragmentToUpdateSkiFragment(currentItem)
           holder.itemView.findNavController().navigate(action)
        }

        holder.itemView.findViewById<View>(R.id.adap_layout_skiRow).setOnLongClickListener() {

            Toast.makeText(holder.itemView.context, "Long click " + currentItem.name, Toast.LENGTH_SHORT).show()
            true
        }
    }

    override fun getItemCount(): Int {
        return myList.size
    }


    fun setData(list: List<Ski>){
        this.myList = list
        notifyDataSetChanged()
    }
}