package com.example.mapsense.common.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.mapsense.R
import com.example.mapsense.data.model.WeatherResponse
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetDialog(var data: WeatherResponse?) : BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.weather_details_bottomsheet, container, false)

        val temp = view.findViewById<TextView>(R.id.tv_temp)
        val title = view.findViewById<TextView>(R.id.tv__title)
        val desc = view.findViewById<TextView>(R.id.tv_des)

        temp.text = " Temperature is - ${data?.main?.temp}  degree"
        title.text = "${data?.weather?.get(0)?.main}"
        desc.text = "${data?.weather?.get(0)?.description}"

        return view
    }
}