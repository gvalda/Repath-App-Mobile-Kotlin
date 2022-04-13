package com.example.googlemapsapplication.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.example.googlemapsapplication.R
import com.example.googlemapsapplication.adapter.ReportItemAdapter
import com.example.googlemapsapplication.data.ReportsRepository

class ReportsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reports)

        val dataset = ReportsRepository().loadReports()
        val reportsRecyclerView = findViewById<RecyclerView>(R.id.reports_recycler_view)
        reportsRecyclerView.adapter = ReportItemAdapter(this, dataset)
        reportsRecyclerView.setHasFixedSize(true)
    }
}