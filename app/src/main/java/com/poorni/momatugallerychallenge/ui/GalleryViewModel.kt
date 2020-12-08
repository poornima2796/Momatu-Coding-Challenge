package com.poorni.momatugallerychallenge.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.poorni.momatugallerychallenge.model.Photo
import com.poorni.momatugallerychallenge.network.NetworkService
import com.poorni.momatugallerychallenge.network.State
import com.poorni.momatugallerychallenge.repositories.PicsumDataSource
import com.poorni.momatugallerychallenge.repositories.PicsumDataSourceFactory
import io.reactivex.disposables.CompositeDisposable

class GalleryViewModel : ViewModel() {

    private val networkService = NetworkService.getService()
    var dataList: LiveData<PagedList<Photo>>
    private val compositeDisposable = CompositeDisposable()
    private val pageSize = 5
    private val picDataSourceFactory: PicsumDataSourceFactory

    init {
        picDataSourceFactory = PicsumDataSourceFactory(compositeDisposable, networkService)
        val config = PagedList.Config.Builder()
            .setPageSize(pageSize)
            .setInitialLoadSizeHint(pageSize * 2)
            .setEnablePlaceholders(false)
            .build()
        dataList = LivePagedListBuilder<Int, Photo>(picDataSourceFactory, config).build()
    }


    fun getState(): LiveData<State> = Transformations.switchMap<PicsumDataSource,
            State>(picDataSourceFactory.DataSourceLiveData, PicsumDataSource::state)

    fun retry() {
        picDataSourceFactory.DataSourceLiveData.value?.retry()
    }

    fun listIsEmpty(): Boolean {
        return dataList.value?.isEmpty() ?: true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}