package com.arttttt.addprofile.api

fun interface ProfileExistsChecker {

    suspend fun isProfileExist(title: String): Boolean
}