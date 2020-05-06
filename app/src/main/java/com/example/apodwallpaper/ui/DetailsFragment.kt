package com.example.apodwallpaper.ui

import android.app.WallpaperManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition

import com.example.apodwallpaper.R
import com.example.apodwallpaper.data.network.dto.ImageDTO
import kotlinx.android.synthetic.main.fragment_details.*
import kotlinx.android.synthetic.main.fragment_details.view.*
import moxy.MvpAppCompatFragment
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.OnPermissionDenied
import permissions.dispatcher.RuntimePermissions
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*
import java.util.jar.Manifest

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


/**
 * A simple [Fragment] subclass.
 * Use the [DetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

@RuntimePermissions
class DetailsFragment : MvpAppCompatFragment() {
    private var image: ImageDTO? = null

    private var bitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        arguments?.let {
            image = it.getParcelable<ImageDTO>(ARG_PARAM1)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_details, container, false)

        //Toast.makeText(context,image!!.explanation,Toast.LENGTH_SHORT).show()
        val toolbar = view.toolbar_details

        toolbar.title = image!!.title
        toolbar.navigationIcon = resources.getDrawable(R.drawable.ic_arrow_back_white_24dp,resources.newTheme());
        toolbar.setNavigationOnClickListener {
            activity!!.onBackPressed()
        }

        view.date_text_view.text = image!!.date
        view.explanation_text_view.text = image!!.explanation
        loadImage(view)

        val setWallpaperButton = view.button

        setWallpaperButton.setOnClickListener {

            //if has permisson
            setWallpaperWithPermissionCheck()
        }

        return view
    }

    @NeedsPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun setWallpaper(){
        val uri = bitmap?.let { it1 -> getImageUri(activity!!.applicationContext, it1) };
        val intent = Intent(Intent.ACTION_ATTACH_DATA)
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.setDataAndType(uri,"image/jpeg")
        intent.putExtra("mimeType", "image/jpeg")
        startActivity(Intent.createChooser(intent, "Set as:"));
    }
    @OnPermissionDenied(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun permissionDenied(){
        Toast.makeText(context,"permissionDenied",Toast.LENGTH_SHORT).show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode,grantResults)
    }


    private fun getImageUri(context: Context,bitmap: Bitmap): Uri{
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =  MediaStore.Images.Media.insertImage(activity!!.contentResolver, bitmap, UUID.randomUUID().toString(), "drawing")
        return Uri.parse(path)
    }

    private fun loadImage(view : View){
        context?.let {
            Glide.with(it).asBitmap().load(image!!.url)
                .listener(object : RequestListener<Bitmap?> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any,
                        target: Target<Bitmap?>,
                        isFirstResource: Boolean
                    ): Boolean {
                        view.progressBar.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Bitmap?,
                        model: Any?,
                        target: Target<Bitmap?>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        view.progressBar.visibility = View.GONE
                        view.button.visibility = View.VISIBLE
                        bitmap = resource
                        return false
                    }
                })
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                //.thumbnail(0.2f)
                .into(view.hd_image)
        }

    }


    companion object {

        @JvmStatic
        fun newInstance(param1: Parcelable) =
            DetailsFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PARAM1,param1)
                }
            }
    }
}
