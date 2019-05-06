package com.jhj.uiview

import android.content.Context

val Context.density: Float
    get() = this.resources.displayMetrics.density

val Context.scaleDensity: Float
    get() = this.resources.displayMetrics.scaledDensity

val Context.screenHeight: Int
    get() = resources.displayMetrics.heightPixels

val Context.screenWidth: Int
    get() = resources.displayMetrics.widthPixels