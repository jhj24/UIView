package com.jhj.uiview

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import com.jhj.uiview.fragment.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    private val map = mapOf(
            "折线图" to LineChartFragment(),
            "柱状图" to HistogramFragment(),
            "柱状图3" to HistogramFragment2(),
            "饼状图" to PieChartFragment(),
            "环状图" to RingChartFragment(),
            "水平偏移" to HorizontalOffsetFragment(),
            "标签" to FlowLayoutFragment(),
            "圆形图片" to CircleImageViewFragment(),
            "图片覆盖" to CoverImageFragment(),
            "柱状图2" to BarChartViewFragment(),
            "流量球" to WaveViewFragment()
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val list = map.keys.toList()
        val fragmentList = map.values.toList()
        viewPager.adapter = PageAdapter(fragmentList, list, supportFragmentManager)
        tabLayout.setupWithViewPager(viewPager)
    }


    class PageAdapter(val fragmentList: List<Fragment>, val title: List<String>, manager: FragmentManager) : FragmentPagerAdapter(manager) {
        override fun getItem(p0: Int): Fragment {
            return fragmentList[p0]
        }

        override fun getCount(): Int {
            return fragmentList.size
        }

        //配置标题的方法
        override fun getPageTitle(position: Int): CharSequence? {
            return title.get(position)
        }

    }
}
