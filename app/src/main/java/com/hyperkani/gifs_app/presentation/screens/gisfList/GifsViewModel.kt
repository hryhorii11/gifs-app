package com.hyperkani.gifs_app.presentation.screens.gisfList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.hyperkani.gifs_app.data.model.Gif
import com.hyperkani.gifs_app.data.repository.GifsRepository
import com.hyperkani.gifs_app.presentation.utils.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GifsViewModel @Inject constructor(
    private val repository: GifsRepository,
    private val crashlytics: FirebaseCrashlytics
) : ViewModel() {


    private var originalList: List<Gif> = emptyList()
    private val _gifs = MutableStateFlow<UIState<List<Gif>>>(UIState.Idle())
    val gifs: StateFlow<UIState<List<Gif>>> = _gifs

    private val _selectedGifIndex = MutableStateFlow<Int?>(null)
    val selectedGifIndex: StateFlow<Int?> = _selectedGifIndex

    init {
        setGifs()
        logNonFatal()
    }
    fun logNonFatal() {
        crashlytics.setCustomKey("action", "logNonFatal")
        crashlytics.log("User pressed non-fatal button")
        try {
            // Штучна помилка, яку ми перехоплюємо
            val x = 1 / 0
        } catch (e: Throwable) {
            crashlytics.recordException(IllegalStateException("Non-fatal demo exception", e))
        }
    }

    fun setGifs() {
        viewModelScope.launch {

            _gifs.value = UIState.Loading()
            repository.getGifs().collect {
                if (it.isSuccess) {
                    originalList = it.getOrNull()!!
                    _gifs.value = UIState.Success(originalList)
                } else {
                    _gifs.value = UIState.Error(it.exceptionOrNull())
                }
            }
        }
    }

    fun search(searchQuery: String) {
        _gifs.value = UIState.Success(originalList.filter { it.title.contains(searchQuery) })
    }

    fun selectGif(gif: Gif) {
        _selectedGifIndex.value = originalList.indexOf(gif)
    }


}