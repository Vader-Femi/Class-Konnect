package com.femi.e_class.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.viewpager.widget.PagerAdapter
import com.femi.e_class.R
import com.femi.e_class.visible

class OnBoardingViewPagerAdapter(val context: Context) :
    PagerAdapter() {

    var layoutInflater: LayoutInflater? = null

    private val indexes = arrayOf(1,2,3)

    override fun getCount(): Int = indexes.size

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object` as RelativeLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = layoutInflater!!.inflate(R.layout.item_view_onboarding,container, false)

        view.let {
            val layout1: LinearLayout = view.findViewById(R.id.onBoardingLayout1)
            val layout2: LinearLayout = view.findViewById(R.id.onBoardingLayout2)
            val layout3: LinearLayout = view.findViewById(R.id.onBoardingLayout3)

            when(position){
                0 -> layout1.visible(true)
                1 -> layout2.visible(true)
                2 -> layout3.visible(true)
            }
        }

        container.addView(view)

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as RelativeLayout)
    }

}