package com.example.sunnyweather2.ui.place

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.sunnyweather2.MainActivity
import com.example.sunnyweather2.R
import com.example.sunnyweather2.databinding.FragmentPlaceBinding
import com.example.sunnyweather2.ui.weather.WeatherActivity

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


/**
 * A simple [Fragment] subclass.
 * Use the [PlaceFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PlaceFragment : Fragment() {


    private lateinit var adapter: PlaceAdapter
    private val viewModel by lazy { ViewModelProvider(this).get(PlaceViewModel::class.java) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding = FragmentPlaceBinding.inflate(inflater, container, false)
        if (activity is MainActivity && viewModel.isPlaceSaved()) {
            val place = viewModel.getSavedPlace()
           val intent =Intent(this.requireContext(),WeatherActivity::class.java).apply {
               putExtra("location_lng", place.location.lng)
               putExtra("location_lat", place.location.lat)
               putExtra("place_name", place.name)
            }
            startActivity(intent)
            requireActivity().finish()
        }
        adapter = PlaceAdapter()
        binding.recyclerView.adapter = adapter
        binding.apply {
            searchPlaceEdit.addTextChangedListener {
                val content = it.toString()
                if (content.isNotEmpty()) {
                    viewModel.searchPlace(content)
                } else {
                    recyclerView.visibility = View.GONE
                    bgImage.visibility = View.VISIBLE
                    viewModel.placeList.clear()
                    adapter.notifyDataSetChanged()
                }
            }

            viewModel.placeLiveData.observe(requireActivity(), {
                val place = it.getOrNull()
                adapter.setEmptyView(R.layout.loading_layout)
                if (place != null) {
                    recyclerView.visibility = View.VISIBLE
                    bgImage.visibility = View.GONE
                    viewModel.placeList.clear()
                    viewModel.placeList.addAll(place)
                    adapter.data = viewModel.placeList
                    //adapter.setDiffNewData(viewModel.placeList)
                    adapter.notifyDataSetChanged()
                } else {
                    adapter.setEmptyView(R.layout.error_layout)
                    Toast.makeText(requireContext(), "未能找到任何地点", Toast.LENGTH_SHORT).show()
                    it.exceptionOrNull()?.printStackTrace()
                }
            })
        }
        adapter.setOnItemClickListener { ad, view, position ->
            val place = viewModel.placeList[position]
            val activity = requireActivity()
            if (activity is WeatherActivity) {
                activity.binding.drawerLayout.closeDrawers()
                activity.viewModel.locationLng = place.location.lng
                activity.viewModel.locationLat = place.location.lat
                activity.viewModel.placeName = place.name
                viewModel.savePlace(place)
                activity.refreshWeather()
            } else {
                val intent = Intent(requireContext(), WeatherActivity::class.java).apply {
                    putExtra("location_lng", place.location.lng)
                    putExtra("location_lat", place.location.lat)
                    putExtra("place_name", place.name)
                    viewModel.savePlace(place)
                }
                startActivity(intent)
                requireActivity().finish()
            }
        }

        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PlaceFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PlaceFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}