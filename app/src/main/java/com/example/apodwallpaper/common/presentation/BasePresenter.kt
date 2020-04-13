package com.example.apodwallpaper.common.presentation


import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import moxy.MvpPresenter
import moxy.MvpView

abstract class BasePresenter<View : MvpView?> :
    MvpPresenter<View>() {
    private var compositeDisposable: CompositeDisposable? = null

    protected fun disposeOnDestroy(disposable: Disposable) {
        if (compositeDisposable == null) {
            compositeDisposable = CompositeDisposable()
        }
    }

    override fun onDestroy() {
        if (compositeDisposable != null) {
            compositeDisposable!!.clear()
            compositeDisposable = null
        }
        super.onDestroy()
    }
}