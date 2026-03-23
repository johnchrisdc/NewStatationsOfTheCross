package com.jcdc.newstationsofthecross.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jcdc.newstationsofthecross.data.model.StationGridItem
import com.jcdc.newstationsofthecross.data.repository.StationRepository

class StationViewModel(private val repository: StationRepository) : ViewModel() {
    private val _gridItems = MutableLiveData<List<StationGridItem>>()
    val gridItems: LiveData<List<StationGridItem>> = _gridItems

    private val _completedTitles = MutableLiveData<Set<String>>()
    val completedTitles: LiveData<Set<String>> = _completedTitles

    init {
        loadCompletedTitles()
    }

    fun loadStations() {
        _gridItems.value = repository.getGridItems()
    }

    fun loadCompletedTitles() {
        _completedTitles.value = repository.getAllCompletedTitles()
    }

    fun toggleStationCompleted(title: String) {
        val currentStatus = repository.isStationCompleted(title)
        repository.setStationCompleted(title, !currentStatus)
        loadCompletedTitles()
    }

    fun isCompleted(title: String): Boolean {
        return repository.isStationCompleted(title)
    }

    fun clearAllProgress() {
        repository.clearAllProgress()
        loadCompletedTitles()
    }
}
