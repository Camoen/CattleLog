package com.example.cattlelog.main_activity

import com.example.cattlelog.R

enum class Tab(val position: Int, val title: Int) {
    HERD(0, R.string.tab_herd),
    NEXT_HEATS(1, R.string.tab_next_heats);

    // Reverse lookup: https://stackoverflow.com/questions/37794850/effective-enums-in-kotlin-with-reverse-lookup
    // Basically lets us use bracket indexing to retrieve the title of each enum entry
    companion object {
        private val map = values().associateBy(Tab::ordinal)
        operator fun get(position: Int) = map[position]
    }
}
