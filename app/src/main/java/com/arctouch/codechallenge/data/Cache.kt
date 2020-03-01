package com.arctouch.codechallenge.data

import com.arctouch.codechallenge.model.Genre

// @Revisor: I changed it from static object to a instance of class to increase of possibilities of test and mock
// Since this dependency will be managed by Koin as a single instance, I don't see any problems in making this change, what do you think?
class Cache {

    var genres = listOf<Genre>()

    fun cacheGenres(genres: List<Genre>) {
        this.genres = genres
    }
}
