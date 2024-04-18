package com.arttttt.profiles.api

import com.arttttt.appslist.SelectedActivity

fun interface SelectedActivitiesRepository {

    fun getSelectedActivities(): List<SelectedActivity>
}