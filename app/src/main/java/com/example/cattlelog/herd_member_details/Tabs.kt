package com.example.cattlelog.herd_member_details

import com.example.cattlelog.R

// For use throughout the HerdMemberDetails-related files
enum class Tab(val position: Int, val title: Int) {
    OVERVIEW(0, R.string.tab_overview),
    HEALTH(1, R.string.tab_health),
    TREATMENT(2, R.string.tab_treatment);

    // Reverse lookup: https://stackoverflow.com/questions/37794850/effective-enums-in-kotlin-with-reverse-lookup
    // Basically lets us use bracket indexing to retrieve the title of each enum entry
    companion object {
        private val map = values().associateBy(Tab::ordinal)
        operator fun get(position: Int) = map[position]
    }
}
