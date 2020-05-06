package com.example.apodwallpaper.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.apodwallpaper.R
import com.example.apodwallpaper.data.network.RestAPI
import com.example.apodwallpaper.data.network.dto.ImageDTO
import com.example.apodwallpaper.imagelist.common.State
import com.example.apodwallpaper.imagelist.mvp.ImagePresenter
import com.example.apodwallpaper.imagelist.mvp.ImageView
import com.example.apodwallpaper.ui.adapter.ImageListAdapter
import com.example.apodwallpaper.ui.adapter.RecyclerItemClickListener
import com.example.apodwallpaper.widget.SimpleDatePickerDialog
import com.example.apodwallpaper.widget.SimpleDatePickerDialogFragment
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_main.view.*
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import java.util.*

class MainFragment : MvpAppCompatFragment(), ImageView, SimpleDatePickerDialog.OnDateSetListener {

    private val presenter by moxyPresenter { ImagePresenter(RestAPI) }

    private var imageListAdapter: ImageListAdapter? = null
    private val images = sortedSetOf<ImageDTO>(compareByDescending {it.date})

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_main,container,false)

        val toolbar = view.toolbar
        toolbar.title = resources.getString(R.string.app_name)
        toolbar.inflateMenu(R.menu.menu_calendar)
        toolbar.setOnMenuItemClickListener {
            if(it.itemId == R.id.calendar_button){
                displaySimpleDatePickerDialogFragment()
            }
            return@setOnMenuItemClickListener true
        }

        val recyclerView = view.recyclerView

        imageListAdapter = ImageListAdapter(Glide.with(this))
        recyclerView.layoutManager = GridLayoutManager(context,2)
        recyclerView.adapter = imageListAdapter

        recyclerView.addOnItemTouchListener(
            RecyclerItemClickListener(
                recyclerView,
                object :
                    RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        activity!!.supportFragmentManager.beginTransaction()
                            .replace(R.id.frame_layout,DetailsFragment.newInstance(images.toTypedArray()[position]))
                            .addToBackStack("Frag1")
                            .commit()
                    }
                })
        )

        val tryAgainButton = view.btn_try_again
        tryAgainButton.setOnClickListener {
            presenter.showImagesOfCurrentMonth()
        }

        return view
    }

    private fun displaySimpleDatePickerDialogFragment() {
        val datePickerDialogFragment: SimpleDatePickerDialogFragment
        val calendar =
            Calendar.getInstance(Locale.getDefault())
        datePickerDialogFragment = SimpleDatePickerDialogFragment.getInstance(
            calendar[Calendar.YEAR], calendar[Calendar.MONTH]
        )
        datePickerDialogFragment.setOnDateSetListener(this)
        datePickerDialogFragment.show(childFragmentManager, null)
    }



    companion object {
        @JvmStatic
        fun newInstance() = MainFragment()
    }

    override fun showData(image: ImageDTO) {
        images.add(image)
        imageListAdapter!!.replaceImages(images.toTypedArray())
    }

    override fun showState(state: State) {
        when(state){
            State.NetworkError -> error_frame_layout.visibility = View.VISIBLE
            else -> error_frame_layout.visibility = View.GONE
        }
    }

    override fun onDateSet(year: Int, monthOfYear: Int) {

        images.clear()

        presenter.showImagesByDate(year, monthOfYear)

    }


}
