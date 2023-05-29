package com.example.androidchallenge.data.model

data class LaunchResponse(
    val docs: List<Launch>,
    val limit: Int,
    val totalPages: Int,
    val page: Int,
    val nextPage: Int,
    val hasNextPage: Boolean
)
