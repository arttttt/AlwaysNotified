package com.arttttt.appholder.utils.resources

import android.graphics.drawable.Drawable

interface ResourcesProvider {

    fun getDrawable(pkg: String): Drawable?
}