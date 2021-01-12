package com.stansdevhouse.newsapp.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stansdevhouse.newsapp.domain.NewsRepositoryDelegate
import com.stansdevhouse.newsapp.domain.RequestResult
import com.stansdevhouse.newsapp.domain.model.News
import com.stansdevhouse.newsapp.util.LiveEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch


sealed class Event {
    object Loading : Event()
    object Success : Event()
    data class Error(val errorMessage: String) : Event()
    data class OpenUrl(val url: String) : Event()
}

@FlowPreview
@ExperimentalCoroutinesApi
class NewsListViewModel @ViewModelInject constructor(private val newsRepositoryDelegate: NewsRepositoryDelegate) : ViewModel() {

    //region liveData
    val liveEvent: LiveEvent<Event> = LiveEvent()

    private val _newsLiveData = MutableLiveData<List<News>>()
    val newsLiveData: LiveData<List<News>> = _newsLiveData

    private val _newsTypeLiveData = MutableLiveData<List<String>>()
    val newsTypeLiveData: LiveData<List<String>> = _newsTypeLiveData
    //endregion

    private companion object {
        private const val ALL_TEXT = "All"
    }

    private var selectedFilter = ALL_TEXT

    //region init
    init {
        refreshNews()
    }
    //endregion

    //region private helpers
    private fun getAllTypes() {
        viewModelScope.launch {
            newsRepositoryDelegate.getAllTypes()
                .catch { e ->
                    liveEvent.value =
                        Event.Error(errorMessage = e.message ?: "Error fetching types")
                }
                .collectLatest { types ->
                    _newsTypeLiveData.value = types
                }
        }
    }

    private fun getAllNews() {
        viewModelScope.launch {
            newsRepositoryDelegate.getAllNews()
                .catch { e ->
                    liveEvent.value =
                        Event.Error(errorMessage = e.message ?: "Error fetching news")
                }
                .collectLatest {
                    _newsLiveData.value = it
                }
        }
    }

    private fun getNewsByType(typeString: String) {
        viewModelScope.launch {
            newsRepositoryDelegate
                .getNewsByType(typeString)
                .onStart {
                    liveEvent.value = Event.Loading
                }
                .catch { e ->
                    liveEvent.value =
                        Event.Error(errorMessage = e.message ?: "Error fetching news")
                }
                .collect {
                    _newsLiveData.value = it
                }
        }
    }

    private fun refreshNews() {
        viewModelScope.launch {
            newsRepositoryDelegate.refresh()
                .onStart {
                    liveEvent.value = Event.Loading
                }
                .catch { e ->
                    liveEvent.value =
                        Event.Error(errorMessage = e.message ?: "Error fetching news")
                }
                .collectLatest {
                    when (it) {
                        RequestResult.Loading ->  liveEvent.value = Event.Loading
                        is RequestResult.Success -> {
                            liveEvent.value = Event.Success
                            getNews(selectedFilter)
                            getAllTypes()
                        }
                        is RequestResult.Error -> {
                            liveEvent.value = Event.Error(errorMessage = it.errorMessage)
                            getAllNews()
                            getAllTypes()
                        }
                    }
                }
        }
    }

    //endregion

    //region public helpers
    fun refreshFabClicked() {
        refreshNews()
    }

    fun getNews(type: CharSequence) {
        val typeString = type.toString()
        selectedFilter = typeString
        if (selectedFilter == ALL_TEXT) {
            getAllNews()
        } else {
            getNewsByType(typeString)
        }
    }

    fun onCardClicked(url: String) {
        liveEvent.value = Event.OpenUrl(url)
    }
    //endregion


}