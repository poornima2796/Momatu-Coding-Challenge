package com.poorni.momatugallerychallenge.repositories

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.poorni.momatugallerychallenge.model.Photo
import com.poorni.momatugallerychallenge.network.NetworkService
import io.reactivex.disposables.CompositeDisposable

class PicsumDataSourceFactory(
    private val compositeDisposable: CompositeDisposable,
    private val networkService: NetworkService
) : DataSource.Factory<Int, Photo>() {

    val newsDataSourceLiveData = MutableLiveData<PicsumDataSource>()

    override fun create(): DataSource<Int, Photo> {
        val newsDataSource = PicsumDataSource(networkService, compositeDisposable)
        newsDataSourceLiveData.postValue(newsDataSource)
        return newsDataSource
    }
}