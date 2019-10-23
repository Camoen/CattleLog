package com.example.cattlelog.herd

import androidx.lifecycle.ViewModel
import android.app.Application
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException



// https://codelabs.developers.google.com/codelabs/kotlin-android-training-view-model/index.html#7
class TreatmentViewModelFactory(
    private val application: Application,
    private val tagNumber: Int,
    private val birthDate: String
):  ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TreatmentViewModel::class.java)){
            return TreatmentViewModel(application, tagNumber, birthDate) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}