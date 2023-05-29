package com.example.androidchallenge.data.repository

import com.example.androidchallenge.data.remote.ApiService
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val apiService: ApiService,
) {

    fun getLaunches(launchesRequest: Any)= apiService.getLaunches(launchesRequest)

    fun getLaunchpads() =
         apiService.getLaunchpads()

}