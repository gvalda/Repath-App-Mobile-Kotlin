package com.example.googlemapsapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.googlemapsapplication.R
import com.example.googlemapsapplication.data.models.Report

class ReportItemAdapter(
    private val context: Context,
    private val dataset: List<Report>
) : RecyclerView.Adapter<ReportItemAdapter.ItemViewHolder>() {

    class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val titleTextView: TextView = view.findViewById(R.id.obstacle_title)
        val locationTextView: TextView = view.findViewById(R.id.obstacle_location)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.rv_item_report, parent, false)

        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset[position]
        holder.titleTextView.text = item.type

        // If we will have location model we can introduce
        // a string resource to format locations
        // holder.locationTextView.text = context.resources.getString({a string resource id, variables})
        holder.locationTextView.text = item.location
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

}