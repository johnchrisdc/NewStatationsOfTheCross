package com.jcdc.newstationsofthecross.data.repository

import android.content.Context
import com.google.gson.Gson
import com.jcdc.newstationsofthecross.data.model.StationData
import com.jcdc.newstationsofthecross.data.model.StationGridItem
import java.io.InputStreamReader

class StationRepository(private val context: Context) {
    private val prefs = context.getSharedPreferences("station_prefs", Context.MODE_PRIVATE)

    fun getStationData(): StationData? {
        return try {
            val inputStream = context.assets.open("station.json")
            val reader = InputStreamReader(inputStream)
            Gson().fromJson(reader, StationData::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun getGridItems(): List<StationGridItem> {
        val data = getStationData() ?: return emptyList()
        val items = mutableListOf<StationGridItem>()
        items.add(StationGridItem.IntroductionItem(data.title, data.introduction))
        items.addAll(data.stations.map { StationGridItem.StationItem(it) })
        items.add(StationGridItem.ConclusionItem(data.conclusion))
        return items
    }

    fun isStationCompleted(title: String): Boolean {
        return prefs.getBoolean(title, false)
    }

    fun setStationCompleted(title: String, completed: Boolean) {
        prefs.edit().putBoolean(title, completed).apply()
    }

    fun getAllCompletedTitles(): Set<String> {
        return prefs.all.filterValues { it as? Boolean == true }.keys
    }

    fun clearAllProgress() {
        prefs.edit().clear().apply()
    }
}
