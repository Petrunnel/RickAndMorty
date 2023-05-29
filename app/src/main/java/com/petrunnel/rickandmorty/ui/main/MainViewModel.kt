package com.petrunnel.rickandmorty.ui.main

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
class MainViewModel
@Inject constructor(
    private val apiService: RickAndMortyApiService
) : ViewModel() {

    private val _status = MutableLiveData<List<Character>>()
    val status: LiveData<List<Character>> = _status
    private var page: Int = MOVIE_STARTING_PAGE_INDEX
    private var maxPage: Int = 1

    companion object {
        private const val TAG = "MyLog"
        const val MOVIE_STARTING_PAGE_INDEX = 1
    }

    init {
        getRickAndMortyCharacter(MOVIE_STARTING_PAGE_INDEX)
    }

    private fun getRickAndMortyCharacter(page: Int) {
        viewModelScope.launch {
            try {
                val listResult = apiService.getCharacter(page)
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

    fun refresh() {
        viewModelScope.launch {
            try {
                page = MOVIE_STARTING_PAGE_INDEX
                val listResult = apiService.getCharacter(page)
                _status.value = listResult.results
                maxPage = listResult.info.pages
            } catch (e: Exception) {
                Log.d(TAG, "Failure: ${e.message}")
            }
        }
    }

    fun loadDataNext() {
        page++
        if (page <= maxPage) {
            getRickAndMortyCharacter(page)
        }
    }
}

