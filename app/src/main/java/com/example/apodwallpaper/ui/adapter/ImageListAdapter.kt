package com.example.apodwallpaper.ui.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.apodwallpaper.R
import com.example.apodwallpaper.data.network.dto.ImageDTO
import kotlinx.android.synthetic.main.item_gif.view.*

class ImageListAdapter(private val glideRequestManager: RequestManager) : RecyclerView.Adapter<ImageViewHolder>() {

    private var imageList = arrayListOf<ImageDTO>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder.create(parent,glideRequestManager)
    }

    override fun getItemCount(): Int = imageList.size

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val image = imageList[position]
        holder.bindImage(image)
    }

    fun replaceImages(images : ArrayList<ImageDTO>){
        imageList.clear()
        imageList.addAll(images)
        notifyDataSetChanged()
    }


}

public class ImageViewHolder(itemView : View, private val imageLoader: RequestManager) : RecyclerView.ViewHolder(itemView){
        val imageView = itemView.image
        val progressBar = itemView.progress_bar

        fun bindImage(image : ImageDTO){

            progressBar.visibility = View.VISIBLE

            imageLoader.load(image.url)
                .listener(object : RequestListener<Drawable?> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any,
                        target: Target<Drawable?>,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any,
                        target: Target<Drawable?>,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {

                        progressBar.visibility = View.GONE
                        return false
                    }
                })
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                .thumbnail(0.3f)
                .into(imageView)
        }

    companion object{
        fun create(parent : ViewGroup,glideRequestManager: RequestManager) : ImageViewHolder{
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_gif,parent,false)
            return ImageViewHolder(view,glideRequestManager)
        }
    }
}