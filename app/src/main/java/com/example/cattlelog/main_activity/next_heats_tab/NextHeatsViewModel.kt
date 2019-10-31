package com.example.cattlelog.main_activity.next_heats_tab

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.cattlelog.model.database.CattlelogDatabase
import com.example.cattlelog.model.entities.Cattle

class NextHeatsViewModel(application: Application) : AndroidViewModel(application) {
    // TODO: Replace the call to getAllCattle with the next-heats report
    val allHeatsMembers: LiveData<List<Cattle>> = CattlelogDatabase.getDatabase(application).cattleDao().getNextExpectedHeatsPreset()
}
