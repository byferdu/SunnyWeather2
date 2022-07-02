package com.example.sunnyweather2.ui.place

import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.sunnyweather2.R
import com.example.sunnyweather2.logic.model.Place

/*
class PlaceAdapter(private val fragment: Fragment, private val placeList: List<Place>) :
    RecyclerView.Adapter<PlaceAdapter.PlaceVH>() {

    inner class PlaceVH(view: View) : RecyclerView.ViewHolder(view) {
        val placeName: TextView = view.findViewById(R.id.place_name)
        val placeAddress: TextView = view.findViewById(R.id.place_address)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceVH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.place_item, parent, false)
        return PlaceVH(view)
    }

    override fun onBindViewHolder(holder: PlaceVH, position: Int) {
        val place = placeList[position]
        holder.placeName.text = place.name
        holder.placeAddress.text = place.address
    }

    override fun getItemCount() = placeList.size

}*/

class PlaceAdapter :
    BaseQuickAdapter<Place, BaseViewHolder>(R.layout.place_item) {


    override fun convert(holder: BaseViewHolder, item: Place) {
        //TODO("Not yet implemented")
        holder.setText(R.id.place_name,item.name)
        holder.setText(R.id.place_address,item.address)
    }
}