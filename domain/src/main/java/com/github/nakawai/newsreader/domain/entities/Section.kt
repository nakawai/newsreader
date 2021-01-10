package com.github.nakawai.newsreader.domain.entities

enum class Section(val value: String) {
    HOME("home"),

    WORLD("world"),

    NATIONAL("national"),

    POLITICS("politics"),

    NEW_YORK_REGION("nyregion"),

    BUSINESS("business"),

    OPINION("opinion"),

    TECHNOLOGY("technology"),

    SCIENCE("science"),

    HEALTH("health"),

    SPORTS("sports"),

    ARTS("arts"),

    FASHION("fashion"),

    DINING("dining"),

    TRAVEL("travel"),

    MAGAZINE("magazine"),

    REAL_ESTATE("realestate");

    companion object {
        fun fromRawValue(rawValue: String?): Section {
            return values().find { it.value == rawValue }
                ?: throw IllegalArgumentException("invalid apiSection:${rawValue}")
        }
    }

}
