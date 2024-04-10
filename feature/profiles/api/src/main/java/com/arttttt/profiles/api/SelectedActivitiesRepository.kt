package com.arttttt.profiles.api

fun interface SelectedActivitiesRepository {

    fun getSelectedActivities(): List<SelectedActivity>
}