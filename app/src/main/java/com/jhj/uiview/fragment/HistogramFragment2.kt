package com.jhj.uiview.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jhj.uiview.R
import com.jhj.uiview.bean.BimBean
import kotlinx.android.synthetic.main.fragment_histogram2_view.view.*

class HistogramFragment2 : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_histogram2_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val list = listOf<BimBean>(
                BimBean("标段1",5,6),
                BimBean("标段2",1,7),
                BimBean("标段3",2,5),
                BimBean("标段4",3,4)
                )
        view.histogramView.setChartList(list)

    }


}