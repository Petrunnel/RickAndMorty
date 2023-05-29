package com.petrunnel.rickandmorty.ui.details

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
class DetailsViewModel
@Inject constructor(
    private val apiService: RickAndMortyApiService
) : ViewModel() {

    private val _status = MutableLiveData<Character>()
    val status: LiveData<Character> = _status
    private var id: Int? = null

    companion object {
        private const val TAG = "MyLog"
    }

    private fun getRickAndMortyCharacterById() {
        if (id != null) {
            viewModelScope.launch {
                try {
                    val result = apiService.getCharacterById(id!!)
                    _status.value = result
                } catch (e: Exception) {
                    Log.d(TAG, "Failure: ${e.message}")
                }
            }
        }
    }

    fun setCharacterIdAndGetData(id: Int) {
        this.id = id
        getRickAndMortyCharacterById()
    }

    fun refresh() {
        getRickAndMortyCharacterById()
    }
}