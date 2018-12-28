package com.jhj.uiview.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jhj.uiview.R
import com.jhj.uiview.bean.HistogramBean
import kotlinx.android.synthetic.main.fragment_linechar_view.view.*

class LineChartFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_linechar_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val list = arrayListOf<HistogramBean>(
                HistogramBean("k", 0.1f),
                HistogramBean("2", 0.2f),
                HistogramBean("3", 0.15f),
                HistogramBean("4", 0.3f),
                HistogramBean("5", 0.25f)
        )

        view.lineChartView.setDataList(list)

    }


}