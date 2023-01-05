package com.tinysoft.tmobiletest.ui.homelist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tinysoft.tmobiletest.network.ResultWrapper
import com.tinysoft.tmobiletest.repository.Repository
import com.tinysoft.tmobiletest.ui.components.LoadingState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeListViewModel(private val repository: Repository) : ViewModel() {

    private var apiJob: Job? = null
    private val _apiResults = MutableLiveData<HomeListViewState>()
    val apiResults: LiveData<HomeListViewState> = _apiResults
    private val _loadingState = MutableLiveData<LoadingState>()
    val loadingState: LiveData<LoadingState> = _loadingState

    var viewState = HomeListViewState.EMPTY

    fun clearListResult() {
        apiJob?.cancel()
        _apiResults.postValue(HomeListViewState.EMPTY)
    }

    /**
     * Fetch Home list data from api
     * */
    fun fetchList() {
        viewState = HomeListViewState.EMPTY

        apiJob?.cancel() // cancel previous loading job
        apiJob = viewModelScope.launch(Dispatchers.IO) {

            _loadingState.postValue(LoadingState.Loading)
            // delay to fetch data from api
            delay(300)

            val loading = when (val result = repository.getHomeList()) {
                is ResultWrapper.GenericError -> LoadingState.LoadFailure(result.message ?: "Unknown error")
                ResultWrapper.NetworkError -> LoadingState.LoadFailure("Network Error")
                is ResultWrapper.Success -> {
                    viewState = HomeListViewState(
                        result.value.size,
                        result.value
                    )
                    _apiResults.postValue(viewState)

                    LoadingState.Idle
                }
            }
            _loadingState.postValue(loading)
        }
    }
}