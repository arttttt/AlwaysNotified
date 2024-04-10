package com.arttttt.profiles.impl.utils

import android.graphics.Color
import com.arttttt.profiles.api.Profile
import java.util.UUID
import kotlin.random.Random

private const val DEFAULT_PROFILE_TITLE = "Default"

internal fun Profile.Companion.createDefault(): Profile {
    return Profile(
        uuid = UUID.randomUUID().toString(),
        title = DEFAULT_PROFILE_TITLE,
        color = Color.argb(255,
            Random.nextInt(256),
            Random.nextInt(256),
            Random.nextInt(256),
        )
    )
}