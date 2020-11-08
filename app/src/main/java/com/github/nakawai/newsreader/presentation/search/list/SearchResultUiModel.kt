package com.github.nakawai.newsreader.presentation.search.list

import com.github.nakawai.newsreader.domain.entities.Story
import com.github.nakawai.newsreader.domain.entities.StoryUrl

/**
 * Ui Model class for Article
 */
data class SearchResultUiModel(
    val title: String,
    val storyAbstract: String,
    val url: StoryUrl
) {

    constructor(story: Story) : this(
        title = story.title,
        storyAbstract = story.storyAbstract,
        url = story.url

    )
}
