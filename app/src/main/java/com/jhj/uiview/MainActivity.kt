package com.jhj.uiview

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import com.jhj.uiview.fragment.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val list = arrayListOf("折线图", "柱状图", "饼状图", "水平偏移", "标签", "圆形图片")
        val fragmentList = arrayListOf<Fragment>()
        fragmentList.add(LineChartFragment())
        fragmentList.add(HistogramFragment())
        fragmentList.add(PieChartFragment())
        fragmentList.add(HorizontalOffsetFragment())
        fragmentList.add(FlowLayoutFragment())
        fragmentList.add(CircleImageViewFragment())

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
