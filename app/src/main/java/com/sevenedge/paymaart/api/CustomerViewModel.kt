package com.sevenedge.paymaart.api

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CustomerViewModel : ViewModel() {

    // Customer list - holds the full list of customers
    private val _customerList = MutableStateFlow<List<User>>(emptyList())
    val customerList: StateFlow<List<User>> = _customerList

    // Filtered customer list - stores the filtered list based on the search query
    private val _filteredCustomers = MutableStateFlow<List<User>>(emptyList())
    val filteredCustomers: StateFlow<List<User>> = _filteredCustomers

    // Search query - holds the current search query
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    // Selected customer - holds the selected customer for detailed view
    private val _selectedCustomer = MutableStateFlow<User?>(null)
    val selectedCustomer: StateFlow<User?> = _selectedCustomer

    private val customerService: CustomerService

    private var currentPage = 1
    private var isFetching = false

    init {
        // Initialize Retrofit instance to communicate with API
        val retrofit = Retrofit.Builder()
            .baseUrl("https://reqres.in/api/") // Replace with your actual base URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        customerService = retrofit.create(CustomerService::class.java)
        fetchCustomers() // Fetch initial set of customers
    }

    // Function to fetch customers with pagination support
    fun fetchCustomers() {
        if (isFetching) return // Prevent fetching if a request is already in progress
        isFetching = true

        viewModelScope.launch {
            val response = customerService.getUsers(currentPage)
            if (response.isSuccessful) {
                val newCustomers = response.body()?.data.orEmpty()
                _customerList.value = _customerList.value + newCustomers // Append new customers
                currentPage++ // Increment page for next API call
                filterCustomers(_searchQuery.value) // Apply the current search filter
            }
            isFetching = false
        }
    }

    // Function to filter customers based on the search query
    fun filterCustomers(query: String) {
        if (query.isBlank()) {
            _filteredCustomers.value = _customerList.value // Show all customers if search query is empty
        } else {
            val queryWords = query.split(" ").map { it.trim().toLowerCase() }

            _filteredCustomers.value = _customerList.value.filter { user ->
                // Check if all query words are present in any of the fields (firstName, lastName, or email)
                queryWords.all { word ->
                    user.firstName.contains(word, ignoreCase = true) ||
                            user.lastName.contains(word, ignoreCase = true) ||
                            user.email.contains(word, ignoreCase = true)
                }
            }
        }
    }


    // Function to update the search query
    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        filterCustomers(query) // Filter customers whenever the search query changes
    }

    // Function to load more customers for pagination when the user scrolls
    fun loadMoreCustomers() {
        fetchCustomers() // Fetch next page of customers
    }

    // Function to select a customer (for detailed view or other actions)
    fun selectCustomer(customer: User) {
        _selectedCustomer.value = customer
    }

    // Alternate function to set the selected customer (if you want a different naming convention)
    fun setSelectedCustomer(customer: User) {
        _selectedCustomer.value = customer
    }
}
