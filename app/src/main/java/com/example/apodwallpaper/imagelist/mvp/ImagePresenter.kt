package com.example.apodwallpaper.imagelist.mvp

import com.example.apodwallpaper.common.presentation.BasePresenter
import com.example.apodwallpaper.data.network.DefaultResponse
import com.example.apodwallpaper.data.network.RestAPI
import com.example.apodwallpaper.data.network.dto.ImageDTO
import com.example.apodwallpaper.imagelist.common.State
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import java.io.IOException

class ImagePresenter(instance: RestAPI) : BasePresenter<ImageView>() {
    private val restApi: RestAPI = instance


    override fun onFirstViewAttach() {
        //requestImage()
        //requestImageByDate("2020-01-01")
    }

    public fun requestImage(){
        viewState.showState(State.Loading)
        val imageDisposable : Disposable = restApi
            .getImage()
            .image()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::checkResponseAndShowState, this::handleError)
        disposeOnDestroy(imageDisposable)
    }

    public fun requestImageByDate(date : String){
        viewState.showState(State.Loading)
        val imageDisposable : Disposable = restApi
            .getImage()
            .imageByDate(date)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::checkResponseAndShowState, this::handleError)
        disposeOnDestroy(imageDisposable)
    }

    private fun handleError(th: Throwable){
        if (th is IOException) {
            viewState.showState(State.NetworkError)
            return
        }
        viewState.showState(State.ServerError)
    }

    private fun checkResponseAndShowState(response: Response<ImageDTO>) {
        //Here I use Guard Clauses. You can find more here:
        //https://refactoring.com/catalog/replaceNestedConditionalWithGuardClauses.html
        //Here we have 4 clauses:
        if (!response.isSuccessful) {
            viewState.showState(State.ServerError)
            return
        }
        val body: ImageDTO? = response.body()
        if (body == null) {
            viewState.showState(State.HasNoData)
            return
        }

        viewState.showData(body)
        viewState.showState(State.HasData)
    }


}