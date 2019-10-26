package com.example.cattlelog.herd_member_details.overview_tab

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.cattlelog.model.database.CattlelogDatabase
import com.example.cattlelog.model.entities.Cattle
import com.example.cattlelog.model.entities.UserFields


// Class extends AndroidViewModel and requires application as a parameter.
class OverviewViewModel(application: Application, tagNumber: Int, birthDate: String) : AndroidViewModel(application) {

    // LiveData gives us updated treatments when they change.
    val allAnimalDetails: LiveData<List<Cattle>>
    val allUserFields: LiveData<List<UserFields>>

    init {
        allAnimalDetails = CattlelogDatabase.getDatabase(application).cattleDao().getUniqueHerdMember(tagNumber, birthDate)
        allUserFields = CattlelogDatabase.getDatabase(application).userFieldsDao().getAllUserFields()
    }

}