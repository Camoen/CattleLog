package com.example.cattlelog.herd_member_details.health_tab

import androidx.lifecycle.ViewModel
import android.app.Application
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException



// https://codelabs.developers.google.com/codelabs/kotlin-android-training-view-model/index.html#7
class HealthViewModelFactory(
    private val application: Application,
    private val tagNumber: Int,
    private val birthDate: String
):  ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HealthViewModel::class.java)){
            return HealthViewModel(
                application,
                tagNumber,
                birthDate
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}