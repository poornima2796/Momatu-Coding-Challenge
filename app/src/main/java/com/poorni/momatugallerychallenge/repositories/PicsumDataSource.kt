package com.poorni.momatugallerychallenge.repositories

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.poorni.momatugallerychallenge.model.Photo
import com.poorni.momatugallerychallenge.network.NetworkService
import com.poorni.momatugallerychallenge.network.State
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers

class PicsumDataSource(
    private val networkService: NetworkService,
    private val compositeDisposable: CompositeDisposable
) : PageKeyedDataSource<Int, Photo>() {

    var state: MutableLiveData<State> = MutableLiveData()
    private var retryCompletable: Completable? = null


    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Photo>
    ) {
        updateState(State.LOADING)
        compositeDisposable.add(
            networkService.getNews(1, params.requestedLoadSize)
                .subscribe(
                    { response ->
                        updateState(State.DONE)
                        callback.onResult(
                            response,
                            null,
                            2
                        )
                    },
                    {
                        updateState(State.ERROR)
                        setRetry(Action { loadInitial(params, callback) })
                    }
                )
        )
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Photo>) {
        updateState(State.LOADING)
        compositeDisposable.add(
            networkService.getNews(params.key, params.requestedLoadSize)
                .subscribe(
                    { response ->
                        updateState(State.DONE)
                        callback.onResult(
                            response,
                            params.key + 1
                        )
                    },
                    {
                        updateState(State.ERROR)
                        setRetry(Action { loadAfter(params, callback) })
                    }
                )
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Photo>) {
    }

    private fun updateState(state: State) {
        this.state.postValue(state)
    }

    fun retry() {
        if (retryCompletable != null) {
            compositeDisposable.add(
                retryCompletable!!
                    .subscribeOn(Schedulers.io())
                    .observeOn(mainThread())
                    .subscribe()
            )
        }
    }

    private fun setRetry(action: Action?) {
        retryCompletable = if (action == null) null else Completable.fromAction(action)
    }

}



