package com.github.nakawai.newsreader.domain.entities

// https://developer.nytimes.com/docs/top-stories-product/1/routes/%7Bsection%7D.json/get
enum class Section(val value: String) {

    ARTS("arts"),

    // automobile, books,

    BUSINESS("business"),

    DINING("dining"), // undefined?

    FASHION("fashion"),

    // food

    HEALTH("health"),

    HOME("home"),

    // insider

    MAGAZINE("magazine"),

    // movies

    NEW_YORK_REGION("nyregion"),


    NATIONAL("national"), // undefined?

    // obituaries

    OPINION("opinion"),

    POLITICS("politics"),

    REAL_ESTATE("realestate"),

    SCIENCE("science"),

    SPORTS("sports"),

    // sunday review

    TECHNOLOGY("technology"),

    // theater, t-magazine


    TRAVEL("travel"),

    // upshot, us

    WORLD("world");


    companion object {
        fun fromRawValue(rawValue: String?): Section {
            return values().find { it.value == rawValue }
                ?: HOME
            //throw IllegalArgumentException("invalid apiSection:${rawValue}")
        }
    }

}
