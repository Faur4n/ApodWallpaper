package com.example.apodwallpaper.imagelist.mvp


import com.example.apodwallpaper.data.network.dto.ImageDTO
import com.example.apodwallpaper.imagelist.common.State
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface ImageView  : MvpView {

    fun showData(image : ImageDTO)

    fun showState(state : State)
}