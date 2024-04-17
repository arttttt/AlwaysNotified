package com.arttttt.alwaysnotified.utils.resources

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import com.arttttt.localization.ResourcesProvider

class ResourcesProviderImpl(
    private val context: Context
) : ResourcesProvider {

    private val pm: PackageManager
        get() = context.packageManager

    override fun getDrawable(pkg: String): Drawable? {
        return runCatching {
            pm.getApplicationIcon(pkg)
        }.getOrNull()
    }
}