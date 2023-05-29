package com.example.androidchallenge.data.remote

import com.example.androidchallenge.data.model.LaunchResponse
import com.example.androidchallenge.data.model.Launchpad
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @POST("launches/query")
    fun getLaunches(@Body launchesRequest: Any): Observable<LaunchResponse>

    @GET("launchpads")
    public fun getLaunchpads(): Observable<List<Launchpad>>

}