package com.example.androidchallenge.data.model

data class LaunchesSearchRequest(
    val query: QuerySearch,
    val options: Pagination?
)
