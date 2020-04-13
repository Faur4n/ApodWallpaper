package com.example.apodwallpaper.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.apodwallpaper.data.network.dto.ImageDTO
import com.example.apodwallpaper.imagelist.common.State
import com.example.apodwallpaper.imagelist.mvp.ImageView

class MainFragment : Fragment(), ImageView {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    companion object {
        @JvmStatic
        fun newInstance() = MainFragment()
    }

    override fun showData(image: ImageDTO) {
    }

    override fun showState(state: State) {
    }
}
