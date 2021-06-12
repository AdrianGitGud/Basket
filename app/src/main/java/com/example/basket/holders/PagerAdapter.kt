package com.example.basket.holders

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.airbnb.lottie.LottieAnimationView
import com.example.basket.R
import com.example.basket.models.OnBoardingData
import org.w3c.dom.Text

class PagerAdapter(private var context: Context, private var onBoardingDataList: List<OnBoardingData>): PagerAdapter() {
    override fun getCount(): Int {
        return onBoardingDataList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {

        return view == `object`

    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {

        container.removeView(`object` as View)

    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val view = LayoutInflater.from(context).inflate(R.layout.slide_screen, null)


        val animationView: LottieAnimationView = view.findViewById(R.id.animationView)
        val titleOnBoarding: TextView = view.findViewById(R.id.titleOnBoarding)
        val descOngBoarding: TextView = view.findViewById(R.id.descOnBoarding)

        animationView.setAnimationFromUrl(onBoardingDataList[position].animationView)
        titleOnBoarding.text = onBoardingDataList[position].titleOnBoarding
        descOngBoarding.text = onBoardingDataList[position].descOnBoarding

        container.addView(view)
        return view
    }
}