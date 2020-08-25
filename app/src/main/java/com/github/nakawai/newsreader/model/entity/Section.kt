package com.github.nakawai.newsreader.model.entity

import com.github.nakawai.newsreader.R

enum class Section(val key: String, val label: String, val iconResId: Int) {
    HOME("home", "Home", R.drawable.ic_home_black_24dp),
    WORLD("world", "World", R.drawable.ic_world_black_24dp),
    NATIONAL("national", "U.S.", R.drawable.ic_national_black_24dp),
    POLITICS("politics", "Politics", R.drawable.ic_politics_black_24dp),
    NY_REGION("nyregion", "New York", R.drawable.ic_ny_black_24dp),
    BUSINESS("business", "Business", R.drawable.ic_business_black_24dp),
    OPINION("opinion", "Opinion", R.drawable.ic_opinion_black_24dp),
    TECHNOLOGY("technology", "Technology", R.drawable.ic_home_black_24dp),
    SCIENCE("science", "Science", R.drawable.ic_home_black_24dp),
    HEALTH("health", "Health", R.drawable.ic_health_black_24dp),
    SPORTS("sports", "Sports", R.drawable.ic_sports_black_24dp),
    ARTS("arts", "Arts", R.drawable.ic_art_black_24dp),
    FASHION("fashion", "Fashion", R.drawable.ic_fashion_black_24dp),
    DINING("dining", "Dining", R.drawable.ic_dining_black_24dp),
    TRAVEL("travel", "Travel", R.drawable.ic_travel_black_24dp),
    MAGAZINE("magazine", "Magazine", R.drawable.ic_books_black_24dp),
    REAL_ESTATE("realestate", "Real Estate", R.drawable.ic_real_estate_black_24dp);

    companion object {
        fun valueOfApiSection(apiSection: String): Section {

            values().forEach { section ->
                if (section.key == apiSection) {
                    return section
                }
            }

            throw IllegalArgumentException("invalid key:$apiSection")
        }
    }


}
