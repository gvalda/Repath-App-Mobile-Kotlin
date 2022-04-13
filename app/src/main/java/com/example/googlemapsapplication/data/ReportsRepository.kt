package com.example.googlemapsapplication.data

import com.example.googlemapsapplication.data.models.Report
import com.example.googlemapsapplication.data.models.User

class ReportsRepository {
    fun loadReports(): List<Report> {
        return listOf<Report>(
            Report(
                "Country, Town, st.",
                User("dancha", "dancha@ktu.lt"),
                "Pothole",
                "Lorem ipsum dolor sit amet",
                1
            ),
            Report(
                "Country, Town, st.",
                User("dancha", "dancha@ktu.lt"),
                "Pothole",
                "Lorem ipsum dolor sit amet",
                1
            ),
            Report(
                "Country, Town, st.",
                User("dancha", "dancha@ktu.lt"),
                "Pothole",
                "Lorem ipsum dolor sit amet",
                1
            ),
            Report(
                "Country, Town, st.",
                User("dancha", "dancha@ktu.lt"),
                "Pothole",
                "Lorem ipsum dolor sit amet",
                1
            ),
        )
    }
}