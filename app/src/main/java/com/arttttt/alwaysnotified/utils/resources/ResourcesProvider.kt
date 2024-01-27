package com.arttttt.alwaysnotified.utils.resources

import android.graphics.drawable.Drawable

interface ResourcesProvider {

    fun getDrawable(pkg: String): Drawable?
}