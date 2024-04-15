package com.arttttt.permissions.impl.domain.entity

import com.arttttt.permissions.api.Permission2

internal interface StandardPermission : Permission2 {

    val permission: String
}