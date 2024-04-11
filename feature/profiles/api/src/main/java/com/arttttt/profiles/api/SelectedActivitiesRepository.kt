package com.arttttt.profiles.api

fun interface SelectedActivitiesRepository {

    fun getSelectedActivities(): List<com.arttttt.alwaysnotified.SelectedActivity>
}