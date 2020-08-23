package com.github.nakawai.newsreader.model.entity

import com.github.nakawai.newsreader.R

enum class Section(val key: String, val label: String, val iconResId: Int) {
    HOME("home", "Home", R.drawable.ic_home_black_24dp),
    WORLD("world", "World", R.drawable.ic_home_black_24dp),
    NATIONAL("national", "National", R.drawable.ic_home_black_24dp),
    POLITICS("politics", "Politics", R.drawable.ic_home_black_24dp),
    NY_REGION("nyregion", "NY Region", R.drawable.ic_home_black_24dp),
    BUSINESS("business", "Business", R.drawable.ic_home_black_24dp),
    OPINION("opinion", "Opinion", R.drawable.ic_home_black_24dp),
    TECHNOLOGY("technology", "Technology", R.drawable.ic_home_black_24dp),
    SCIENCE("science", "Science", R.drawable.ic_home_black_24dp),
    HEALTH("health", "Health", R.drawable.ic_home_black_24dp),
    SPORTS("sports", "Sports", R.drawable.ic_home_black_24dp),
    ARTS("arts", "Arts", R.drawable.ic_home_black_24dp),
    FASHION("fashion", "Fashion", R.drawable.ic_home_black_24dp),
    DINING("dining", "Dining", R.drawable.ic_home_black_24dp),
    TRAVEL("travel", "Travel", R.drawable.ic_home_black_24dp),
    MAGAZINE("magazine", "Magazine", R.drawable.ic_home_black_24dp),
    REAL_ESTATE("realestate", "Real Estate", R.drawable.ic_home_black_24dp)
}
