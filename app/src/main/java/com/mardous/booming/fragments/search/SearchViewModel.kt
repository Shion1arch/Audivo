
package com.mardous.booming.fragments.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mardous.booming.repository.Repository
import com.mardous.booming.search.SearchFilter
import com.mardous.booming.search.SearchQuery
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SearchViewModel(private val repository: Repository) : ViewModel() {

    private val _searchFilter = MutableLiveData<SearchFilter?>()
    private val _searchQuery = MutableLiveData<SearchQuery>()
    private val _searchResult = MutableLiveData<List<Any>>(ArrayList())

    fun getSearchFilter(): LiveData<SearchFilter?> = _searchFilter
    fun getSearchResult(): LiveData<List<Any>> = _searchResult

    fun useSearchFilter(filter: SearchFilter?) {
        _searchFilter.value = filter
    }

    fun useFilterMode(mode: SearchQuery.FilterMode?) {
        val value = _searchQuery.value
        if (value != null) {
            _searchQuery.value = value.copy(filterMode = mode)
        } else {
            _searchQuery.value = SearchQuery(filterMode = mode)
        }
        submitLatest()
    }

    fun submit(query: String): Job {
        val value = _searchQuery.value
        if (value != null) {
            _searchQuery.value = value.copy(searched = query)
        } else {
            _searchQuery.value = SearchQuery(searched = query)
        }
        return submitLatest()
    }

    fun submitLatest() = viewModelScope.launch(Dispatchers.IO) {
        val query = _searchQuery.value
        if (query == null) {
            _searchResult.postValue(arrayListOf())
        } else {
            _searchResult.postValue(repository.search(query, _searchFilter.value))
        }
    }
}

