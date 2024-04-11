package com.arttttt.profiles.impl.utils

import android.graphics.Color
import com.arttttt.alwaysnotified.Profile
import java.util.UUID
import kotlin.random.Random

private const val DEFAULT_PROFILE_TITLE = "Default"

internal fun com.arttttt.alwaysnotified.Profile.Companion.createDefault(): com.arttttt.alwaysnotified.Profile {
    return com.arttttt.alwaysnotified.Profile(
        uuid = UUID.randomUUID().toString(),
        title = DEFAULT_PROFILE_TITLE,
        color = Color.argb(
            255,
            Random.nextInt(256),
            Random.nextInt(256),
            Random.nextInt(256),
        )
    )
}