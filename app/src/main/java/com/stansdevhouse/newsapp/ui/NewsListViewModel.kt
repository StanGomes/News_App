package com.stansdevhouse.newsapp.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stansdevhouse.newsapp.domain.NewsRepositoryDelegate
import com.stansdevhouse.newsapp.domain.model.News
import com.stansdevhouse.newsapp.util.Status
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


sealed class Result {
    object Loading : Result()
    data class Success(val news: List<News>) : Result()
    data class Error(val errorMessage: String) : Result()
}

@FlowPreview
@ExperimentalCoroutinesApi
class NewsListViewModel @ViewModelInject constructor(private val newsRepositoryDelegate: NewsRepositoryDelegate) :
    ViewModel() {

    private val _newsListViewState = MutableLiveData<Result>(Result.Loading)
    val newsListViewState: LiveData<Result> = _newsListViewState

    private val _newsTypeLiveData = MutableLiveData<List<String>>()
    val newsTypeLiveData: LiveData<List<String>> = _newsTypeLiveData

    private var selectedFilter: String? = null

    init {
        refreshNews()
        fetchAllNews()
        getAllTypes()
    }

    private fun getAllTypes() {
        viewModelScope.launch {
            newsRepositoryDelegate.getAllTypes()
                .collectLatest { types ->
                    _newsTypeLiveData.value = types
                }
        }
    }

    private fun fetchAllNews() {
        viewModelScope.launch {
            newsRepositoryDelegate.getAllNews()
                .onStart {
                    _newsListViewState.value = Result.Loading
                }
                .debounce(2000)
                .catch { e ->
                    _newsListViewState.value =
                        Result.Error(errorMessage = e.message ?: "Error fetching news")
                }
                .collectLatest {
                    when (it.status) {
                        Status.SUCCESS -> _newsListViewState.value =
                            Result.Success(news = it.data ?: emptyList())
                        Status.LOADING -> _newsListViewState.value = Result.Loading
                        Status.ERROR -> _newsListViewState.value =
                            Result.Error(errorMessage = it.message ?: "Error fetching news")
                    }
                }
        }
    }

    fun refreshNews() {
        viewModelScope.launch {
            newsRepositoryDelegate.refresh()
        }
    }

    fun filterChipSelected(type: CharSequence) {
        selectedFilter = type.toString()
        if (selectedFilter == "All") {
            fetchAllNews()
        } else {
            viewModelScope.launch {
                newsRepositoryDelegate
                    .getNewsByType(type.toString())
                    .onStart {
                        _newsListViewState.value = Result.Loading
                    }
                    .catch { e ->
                        _newsListViewState.value =
                            Result.Error(errorMessage = e.message ?: "Error fetching news")
                    }
                    .collect {
                        _newsListViewState.value = Result.Success(it)
                    }
            }
        }
    }

}