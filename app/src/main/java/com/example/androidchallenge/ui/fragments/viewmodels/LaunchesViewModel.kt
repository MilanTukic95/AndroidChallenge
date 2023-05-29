package com.example.androidchallenge.ui.fragments.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.androidchallenge.data.model.*
import com.example.androidchallenge.data.repository.MainRepository
import com.example.androidchallenge.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject

@HiltViewModel
class LaunchesViewModel @Inject constructor(private val mainRepository: MainRepository) :
    ViewModel() {

    var currentPage = AtomicInteger(1)
    private val pageSize = 20
    val launcesList: ArrayList<LaunchDetail> = arrayListOf()
    var hasNextPage: Boolean = false
    private val _response: MutableLiveData<Resource<List<LaunchDetail>?>> = MutableLiveData()
    val response: LiveData<Resource<List<LaunchDetail>?>> = _response
    private val compositeDisposable = CompositeDisposable()


    fun getLaunches(
        queryFilter: QueryFilter?,
        querySearch: QuerySearch?
    ) {

        _response.postValue(Resource.Loading())

        val page = currentPage.getAndIncrement()

        val pagination = Pagination(page, pageSize)

        var request: Any? = null

        if (queryFilter == null && querySearch == null) {
            request = LaunchesRequest(pagination)
        }

        if (queryFilter != null && querySearch == null) {
            request = LaunchesFilterRequest(queryFilter, pagination)
        }

        if (queryFilter == null && querySearch != null) {
            request = LaunchesSearchRequest(querySearch, pagination)
        }

        val observable = Observable.zip(
            mainRepository.getLaunches(request!!), mainRepository.getLaunchpads()
        ) { t1, t2 -> Pair(t1, t2) }
            .map {
                val map2: Map<String, Launchpad> = it.second.associateBy { it.id }
                val mergedList: List<LaunchDetail> = it.first.docs.flatMap { sourceObject ->
                    val anotherObject = map2[sourceObject.launchpad]
                    if (anotherObject != null) {
                        listOf(
                            LaunchDetail(
                                sourceObject.launchpad,
                                sourceObject.name,
                                sourceObject.links.patch?.small,
                                anotherObject.name,
                                anotherObject.region,
                                sourceObject.success,
                                sourceObject.upcoming,
                                sourceObject.date_local,
                                anotherObject.full_name,
                                anotherObject.timezone,
                                anotherObject.details,
                                sourceObject.links.webcast
                            )
                        )
                    } else {
                        emptyList()
                    }
                }

                hasNextPage = it.first.hasNextPage

                return@map mergedList
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { c ->
                    launcesList.addAll(c)
                    _response.postValue(Resource.Success(launcesList))
                },
                { x ->
                    _response.postValue(Resource.Error(x.message.toString(), null))
                })

        compositeDisposable.add(observable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}