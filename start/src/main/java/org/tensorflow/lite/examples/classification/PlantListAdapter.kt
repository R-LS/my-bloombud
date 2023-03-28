package org.tensorflow.lite.examples.classification

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PlantListAdapter(private val context: Context, courseModelArrayList: ArrayList<PlantModel>) :
    RecyclerView.Adapter<PlantListAdapter.ViewHolder>() {
    private val courseModelArrayList: ArrayList<PlantModel>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlantListAdapter.ViewHolder {
        // to inflate the layout for each item of recycler view.
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.plantcard_layout, parent, false)
        return ViewHolder(view)
    }



    override fun onBindViewHolder(holder: PlantListAdapter.ViewHolder, position: Int) {
        // to set data to textview and imageview of each card layout
        val model: PlantModel = courseModelArrayList[position]
        holder.plantNameTV.setText(model.getPlantName())
        holder.plantSpeciesTV.setText(model.getPlantSpecies())
        holder.plantSiteTV.setText(model.getPlantSite())
        //holder.courseIV.setImageResource(model.getPlantName())
        holder.editButton.setOnClickListener(){
            val myIntent = Intent(holder.itemView.context, NewBloomBudActivity::class.java)
            myIntent.putExtra("plantName",holder.plantNameTV.text.toString())
            holder.itemView.context.startActivity(myIntent)
        }
    }

    override fun getItemCount(): Int {
        // this method is used for showing number of card items in recycler view.
        return courseModelArrayList.size
    }

    // View holder class for initializing of your views such as TextView and Imageview.
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //val courseIV: ImageView
        val plantNameTV: TextView
        val plantSpeciesTV: TextView
        val plantSiteTV: TextView
        val editButton: Button
        init {
            //courseIV = itemView.findViewById(R.id.plantNameTV)
            plantNameTV = itemView.findViewById(R.id.plantNameTV)
            plantSpeciesTV = itemView.findViewById(R.id.plantSpeciesTV)
            plantSiteTV = itemView.findViewById(R.id.plantSiteTV)
            editButton = itemView.findViewById(R.id.editButton)
        }
    }

    // Constructor
    init {
        this.courseModelArrayList = courseModelArrayList
    }
}