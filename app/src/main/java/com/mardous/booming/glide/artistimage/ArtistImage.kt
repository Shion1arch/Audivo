
package com.mardous.booming.glide.artistimage

import com.mardous.booming.model.Artist

class ArtistImage(val artist: Artist) {
    override fun equals(other: Any?): Boolean {
        if (other is ArtistImage){
            return other.artist == artist
        }
        return false
    }

    override fun hashCode(): Int {
        return artist.hashCode()
    }
}