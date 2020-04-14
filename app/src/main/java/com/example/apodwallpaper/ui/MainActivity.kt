package com.example.apodwallpaper.ui

import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.apodwallpaper.R
import com.example.apodwallpaper.data.network.RestAPI
import com.example.apodwallpaper.data.network.dto.ImageDTO
import com.example.apodwallpaper.imagelist.common.State
import com.example.apodwallpaper.imagelist.mvp.ImagePresenter
import com.example.apodwallpaper.imagelist.mvp.ImageView
import com.example.apodwallpaper.ui.adapter.ImageListAdapter
import com.example.apodwallpaper.utils.DateDisplayUtils
import com.example.apodwallpaper.widget.SimpleDatePickerDialog
import com.example.apodwallpaper.widget.SimpleDatePickerDialogFragment
import kotlinx.android.synthetic.main.fragment_main.*
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*


class MainActivity :  MvpAppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(LAYOUT)

        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout,MainFragment.newInstance())
            .commit()

    }

    companion object {
        private const val LAYOUT = R.layout.activity_host
    }


}
