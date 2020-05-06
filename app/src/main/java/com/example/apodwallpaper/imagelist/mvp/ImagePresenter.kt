package com.example.apodwallpaper.imagelist.mvp

import android.content.Context
import com.bumptech.glide.Glide
import com.example.apodwallpaper.common.presentation.BasePresenter
import com.example.apodwallpaper.data.network.RestAPI
import com.example.apodwallpaper.data.network.dto.ImageDTO
import com.example.apodwallpaper.imagelist.common.State
import com.example.apodwallpaper.utils.DateDisplayUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import java.io.IOException
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*

class ImagePresenter(instance: RestAPI) : BasePresenter<ImageView>() {
    private val restApi: RestAPI = instance


    override fun onFirstViewAttach() {
        //requestImage()
        //requestImageByDate("2020-01-01")
        showImagesOfCurrentMonth()
    }



    public fun showImagesOfCurrentMonth(){
        val calendar =  Calendar.getInstance(Locale.getDefault())
        val maxDay = Calendar.getInstance(Locale.getDefault())[Calendar.DATE]
        val MONTH_YEAR_DISPLAY_PATTERN = "yyyy-MM-dd"
        val format = SimpleDateFormat(MONTH_YEAR_DISPLAY_PATTERN, Locale.getDefault())

        for(i in 1..maxDay){
            calendar.set(Calendar.DATE,i)
            val date = format.format(calendar.time)
            requestImageByDate(date)
        }
    }



    public fun showImagesByDate(year: Int, monthOfYear: Int){
        val calendar = DateDisplayUtils.formatMonthYear(year, monthOfYear)
        val currentCalendar = Calendar.getInstance(Locale.getDefault())
        val maxDay =
            if(calendar.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR)
                &&
                calendar.get(Calendar.MONTH) == currentCalendar.get(Calendar.MONTH)
            ){
                currentCalendar.get(Calendar.DATE)
            }else{
                calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
            }

        val MONTH_YEAR_DISPLAY_PATTERN = "yyyy-MM-dd"
        val format = SimpleDateFormat(MONTH_YEAR_DISPLAY_PATTERN, Locale.getDefault())

        for(i in 1..maxDay){
            calendar.set(Calendar.DATE,i)
            val date = format.format(calendar.time)
            requestImageByDate(date)
        }
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
        if(body.mediaType != "image"){
            viewState.showState(State.HasNoData)
            return
        }

        viewState.showData(body)
        viewState.showState(State.HasData)
    }


}