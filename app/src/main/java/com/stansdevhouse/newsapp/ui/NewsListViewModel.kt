package com.stansdevhouse.newsapp.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stansdevhouse.newsapp.domain.NewsRepositoryDelegate
import com.stansdevhouse.newsapp.domain.RequestResult
import com.stansdevhouse.newsapp.domain.model.News
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch


sealed class ViewState {
    object Loading : ViewState()
    data class OpenUrl(val url: String): ViewState()
    data class Success(val news: List<News>) : ViewState()
    data class Error(val errorMessage: String) : ViewState()
}

@FlowPreview
@ExperimentalCoroutinesApi
class NewsListViewModel @ViewModelInject constructor(private val newsRepositoryDelegate: NewsRepositoryDelegate) : ViewModel() {

    //region liveData
    private val _newsListViewState = MutableLiveData<ViewState>(ViewState.Loading)
    val newsListViewState: LiveData<ViewState> = _newsListViewState

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
                    _newsListViewState.value =
                        ViewState.Error(errorMessage = e.message ?: "Error fetching types")
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
                    _newsListViewState.value =
                        ViewState.Error(errorMessage = e.message ?: "Error fetching news")
                }
                .collectLatest {
                    _newsListViewState.value = ViewState.Success(news = it)
                }
        }
    }
    //endregion

    //region public helpers
    fun refreshNews() {
        viewModelScope.launch {
            newsRepositoryDelegate.refresh()
                .onStart {
                    _newsListViewState.value = ViewState.Loading
                }
                .catch { e ->
                    _newsListViewState.value =
                        ViewState.Error(errorMessage = e.message ?: "Error fetching news")
                }
                .collectLatest {
                    when (it) {
                        RequestResult.Loading ->  _newsListViewState.value = ViewState.Loading
                        is RequestResult.Success -> {
                            filterChipSelected(selectedFilter)
                            getAllTypes()
                        }
                        is RequestResult.Error -> {
                            _newsListViewState.value = ViewState.Error(errorMessage = it.errorMessage)
                            getAllNews()
                            getAllTypes()
                        }
                    }
                }
        }
    }

    fun filterChipSelected(type: CharSequence) {
        val typeString = type.toString()
        selectedFilter = typeString
        if (typeString == ALL_TEXT) {
            getAllNews()
        } else {
            viewModelScope.launch {
                newsRepositoryDelegate
                    .getNewsByType(typeString)
                    .onStart {
                        _newsListViewState.value = ViewState.Loading
                    }
                    .catch { e ->
                        _newsListViewState.value =
                            ViewState.Error(errorMessage = e.message ?: "Error fetching news")
                    }
                    .collect {
                        _newsListViewState.value = ViewState.Success(it)
                    }
            }
        }
    }

    fun onCardClicked(url: String) {
        _newsListViewState.value = ViewState.OpenUrl(url)
    }
    //endregion


}