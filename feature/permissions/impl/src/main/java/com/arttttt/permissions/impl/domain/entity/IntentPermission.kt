package com.arttttt.permissions.impl.domain.entity

import android.content.Context
import android.content.Intent

internal interface IntentPermission : Permission2 {

    fun createIntent(context: Context): Intent
}