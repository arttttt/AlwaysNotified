package com.arttttt.permissions.impl.domain.entity

import android.content.Context
import android.content.Intent
import com.arttttt.permissions.api.Permission2

interface IntentPermission : Permission2 {

    fun createIntent(context: Context): Intent
}