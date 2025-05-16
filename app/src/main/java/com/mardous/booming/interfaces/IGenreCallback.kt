
package com.mardous.booming.interfaces

import com.mardous.booming.model.Genre

interface IGenreCallback {
    fun genreClick(genre: Genre)
}