package com.github.nakawai.newsreader.model.db

import com.github.nakawai.newsreader.model.entity.Article
import com.github.nakawai.newsreader.model.entity.Multimedia
import com.github.nakawai.newsreader.model.entity.NYTimesStory
import com.github.nakawai.newsreader.model.entity.Section

class Translator {
    companion object {
        fun translate(story: NYTimesStory): Article {
            val mediaArray = mutableListOf<Multimedia>()
            story.multimedia?.forEach {
                mediaArray.add(Multimedia(it.url.orEmpty()))
            }
            return Article(
                title = story.title.orEmpty(),
                url = story.url.orEmpty(),
                storyAbstract = story.storyAbstract.orEmpty(),
                multimedia = mediaArray,
                publishedDate = story.publishedDate,
                isRead = story.isRead,
                section = Section.valueOfApiSection(story.apiSection.orEmpty())
            )
        }
    }
}

fun NYTimesStory.translate(): Article {
    return Translator.translate(this)
}
