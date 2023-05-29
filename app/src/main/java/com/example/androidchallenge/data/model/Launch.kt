package com.example.androidchallenge.data.model

data class Launch(
    val id: String,
    val links: Links,
    val details: String,
    val launchpad: String,
    var name: String?,
    val success: Boolean,
    val upcoming: Boolean,
    val date_local: String)
