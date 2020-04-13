package com.example.apodwallpaper.ui

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.apodwallpaper.R
import com.example.apodwallpaper.data.network.RestAPI
import com.example.apodwallpaper.data.network.dto.ImageDTO
import com.example.apodwallpaper.imagelist.common.State
import com.example.apodwallpaper.imagelist.mvp.ImagePresenter
import com.example.apodwallpaper.imagelist.mvp.ImageView
import com.example.apodwallpaper.ui.adapter.ImageListAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.item_gif.*
import moxy.MvpActivity
import moxy.ktx.moxyPresenter


class MainActivity : MvpActivity(),ImageView {

    private val presenter by moxyPresenter { ImagePresenter(RestAPI) }

    private var imageListAdapter: ImageListAdapter? = null
    private val images = arrayListOf<ImageDTO>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(LAYOUT)


        imageListAdapter = ImageListAdapter(Glide.with(this))
        recyclerView.layoutManager = GridLayoutManager(this,2)
        recyclerView.adapter = imageListAdapter

        presenter.requestImageByDate("2020-01-01")
        presenter.requestImageByDate("2020-01-02")
        presenter.requestImageByDate("2020-01-03")
        presenter.requestImageByDate("2020-01-04")
        presenter.requestImageByDate("2020-01-05")
        presenter.requestImageByDate("2020-01-06")
        presenter.requestImageByDate("2020-01-07")
        presenter.requestImageByDate("2020-01-08")


    }





    override fun onStart() {
        super.onStart()
        //presenter.requestImage()

    }

    override fun showData(image: ImageDTO) {
//        val imageView = apod_image
//        val explanationTextView = explanation_text_view
//        val dateTextView = date_text_view
//
//
//        Glide.with(this).load(image.url).into(imageView)
//        explanationTextView.text = image.explanation
//        dateTextView.text = image.date

        images.add(image)
        imageListAdapter!!.replaceImages(images)
    }

    override fun showState(state: State) {

        when(state){
            State.HasData -> Toast.makeText(this,"HasData",Toast.LENGTH_LONG).show()
            State.NetworkError -> Toast.makeText(this,"NetworkError",Toast.LENGTH_LONG).show()
            State.ServerError -> Toast.makeText(this,"ServerError",Toast.LENGTH_LONG).show()
            State.Loading -> Toast.makeText(this,"Loading",Toast.LENGTH_LONG).show()
            State.HasNoData -> Toast.makeText(this,"HasNoData",Toast.LENGTH_LONG).show()
        }

    }

    companion object {
        private const val LAYOUT = R.layout.activity_main
    }
}
