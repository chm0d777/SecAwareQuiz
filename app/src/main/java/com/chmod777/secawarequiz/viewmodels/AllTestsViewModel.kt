package com.chmod777.secawarequiz.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chmod777.secawarequiz.data.model.TestListItem // Corrected import
import com.chmod777.secawarequiz.data.repository.AllTestsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AllTestsViewModel(
    private val repository: AllTestsRepository = AllTestsRepository() // Basic instantiation
) : ViewModel() {

    private val _testItems = MutableStateFlow<List<TestListItem>>(emptyList())
    val testItems: StateFlow<List<TestListItem>> = _testItems.asStateFlow()

    init {
        loadTestItems()
    }

    private fun loadTestItems() {
        viewModelScope.launch {
            _testItems.value = repository.getAllTestsAndGames()
        }
    }
}
