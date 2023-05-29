package com.petrunnel.rickandmorty.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.petrunnel.rickandmorty.network.RickAndMortyApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.petrunnel.rickandmorty.model.Character


@HiltViewModel
class SearchViewModel
@Inject constructor(
    private val apiService: RickAndMortyApiService
) : ViewModel() {

    private val _status = MutableLiveData<List<Character>>()
    val status: LiveData<List<Character>> = _status
    private var page: Int = MOVIE_STARTING_PAGE_INDEX
    private var maxPage: Int = MOVIE_STARTING_PAGE_INDEX
    private var name = ""

    companion object {
        private const val TAG = "MyLog"
        const val MOVIE_STARTING_PAGE_INDEX = 1
    }

    fun setName(name: String) {
        this.name = name.trim()
        page = MOVIE_STARTING_PAGE_INDEX
        if (name.isNotBlank())
            refresh()
        else
            _status.value = emptyList()
    }

    fun loadDataNext() {
        if (name.isNotBlank()) {
            page++
            if (page <= maxPage) {
                getRickAndMortyCharacterByName(page, name)
            }
        }
    }

    private fun refresh() {
        viewModelScope.launch {
            try {
                page = MOVIE_STARTING_PAGE_INDEX
                val listResult = apiService.getCharacterByName(page, name)
                _status.value = listResult.results
                maxPage = listResult.info.pages
            } catch (e: Exception) {
                page = MOVIE_STARTING_PAGE_INDEX
                _status.value = emptyList()
                Log.d(TAG, "Failure: ${e.message}")
            }
        }
    }

    private fun getRickAndMortyCharacterByName(page: Int, name: String) {
        viewModelScope.launch {
            try {
                val listResult = apiService.getCharacterByName(page, name)
                val joinedList: ArrayList<Character> = arrayListOf()
                status.value?.let { joinedList.addAll(it) }
                joinedList.addAll(listResult.results)
                _status.value = joinedList
                maxPage = listResult.info.pages
            } catch (e: Exception) {
                Log.d(TAG, "Failure: ${e.message}")
            }
        }
    }
}

