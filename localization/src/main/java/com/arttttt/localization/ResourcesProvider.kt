package com.arttttt.localization

import android.graphics.drawable.Drawable

interface ResourcesProvider {

    fun getDrawable(pkg: String): Drawable?
}