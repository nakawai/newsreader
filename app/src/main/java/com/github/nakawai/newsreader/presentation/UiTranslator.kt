package com.github.nakawai.newsreader.presentation

import com.github.nakawai.newsreader.R
import com.github.nakawai.newsreader.domain.entities.Section

class UiTranslator {
    companion object {
        fun translate(section: Section): SectionUi {
            return when (section) {
                Section.HOME -> SectionUi("Home", R.drawable.ic_home_black_24dp)

                Section.WORLD -> SectionUi("World", R.drawable.ic_world_black_24dp)

                Section.NATIONAL -> SectionUi("U.S.", R.drawable.ic_national_black_24dp)

                Section.POLITICS -> SectionUi("Politics", R.drawable.ic_politics_black_24dp)

                Section.NEW_YORK_REGION -> SectionUi("New York", R.drawable.ic_ny_black_24dp)

                Section.BUSINESS -> SectionUi("Business", R.drawable.ic_business_black_24dp)

                Section.OPINION -> SectionUi("Opinion", R.drawable.ic_opinion_black_24dp)

                Section.TECHNOLOGY -> SectionUi("Technology", R.drawable.ic_home_black_24dp)

                Section.SCIENCE -> SectionUi("Science", R.drawable.ic_home_black_24dp)

                Section.HEALTH -> SectionUi("Health", R.drawable.ic_health_black_24dp)

                Section.SPORTS -> SectionUi("Sports", R.drawable.ic_sports_black_24dp)

                Section.ARTS -> SectionUi("Arts", R.drawable.ic_art_black_24dp)

                Section.FASHION -> SectionUi("Fashion", R.drawable.ic_fashion_black_24dp)

                Section.DINING -> SectionUi("Dining", R.drawable.ic_dining_black_24dp)

                Section.TRAVEL -> SectionUi("Travel", R.drawable.ic_travel_black_24dp)

                Section.MAGAZINE -> SectionUi("Magazine", R.drawable.ic_books_black_24dp)

                Section.REAL_ESTATE -> SectionUi("Real Estate", R.drawable.ic_real_estate_black_24dp)
            }
        }
    }
}

data class SectionUi(val label: String, val iconResId: Int)

fun Section.translate(): SectionUi {
    return UiTranslator.translate(this)
}
