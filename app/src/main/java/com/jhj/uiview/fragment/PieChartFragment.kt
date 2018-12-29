package com.jhj.uiview.fragment

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jhj.uiview.R
import com.jhj.uiview.bean.PieChartBean
import kotlinx.android.synthetic.main.fragment_piechart_view.view.*

class PieChartFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_piechart_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val list = arrayListOf<PieChartBean>(
                PieChartBean("k", 0.1, Color.RED),
                PieChartBean("2", 0.2, Color.BLUE),
                PieChartBean("3", 0.15, Color.BLACK),
                PieChartBean("4", 0.3, Color.GREEN),
                PieChartBean("5", 0.25, Color.YELLOW)
        )

        view.pieChartView.setDataList(list)

    }


}