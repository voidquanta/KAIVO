package com.kaivo.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaivo.app.data.ClipItem
import com.kaivo.app.data.ClipboardRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface OneTimeEvent {
    data object Copied : OneTimeEvent
    data object Saved : OneTimeEvent
    data object ClipboardEmpty : OneTimeEvent
}

class HomeViewModel(private val repository: ClipboardRepository) : ViewModel() {

    private val _pasteFieldText = MutableStateFlow("")
    val pasteFieldText: StateFlow<String> = _pasteFieldText

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _events = MutableStateFlow<OneTimeEvent?>(null)
    val events: StateFlow<OneTimeEvent?> = _events

    private val allItems: StateFlow<List<ClipItem>> = repository.observeAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val visibleItems: StateFlow<List<ClipItem>> = combine(allItems, _searchQuery) { items, query ->
        if (query.isBlank()) items
        else items.filter { it.content.contains(query, ignoreCase = true) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onPasteFieldChange(text: String) {
        _pasteFieldText.value = text
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    /** Called only right after the caller has read the system clipboard. */
    fun onClipboardRead(text: String?) {
        if (text.isNullOrBlank()) {
            _events.value = OneTimeEvent.ClipboardEmpty
        } else {
            _pasteFieldText.value = text
        }
    }

    fun saveCurrentField() {
        val text = _pasteFieldText.value
        if (text.isBlank()) return
        viewModelScope.launch {
            repository.save(text)
            _pasteFieldText.value = ""
            _events.value = OneTimeEvent.Saved
        }
    }

    fun onItemCopied() {
        _events.value = OneTimeEvent.Copied
    }

    fun deleteItem(item: ClipItem) {
        viewModelScope.launch { repository.delete(item) }
    }

    fun togglePin(item: ClipItem) {
        viewModelScope.launch { repository.togglePin(item) }
    }

    suspend fun getAllForExport(): List<ClipItem> = repository.getAllOnce()

    fun deleteAllData() {
        viewModelScope.launch { repository.deleteAll() }
    }

    fun consumeEvent() {
        _events.value = null
    }
}

class HomeViewModelFactory(
    private val repository: ClipboardRepository
) : androidx.lifecycle.ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(repository) as T
    }
}
