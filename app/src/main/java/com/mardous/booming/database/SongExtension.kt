
package com.mardous.booming.database

import com.mardous.booming.model.Song

fun List<HistoryEntity>.fromHistoryToSongs(): List<Song> {
    return map {
        it.toSong()
    }
}

fun List<SongEntity>.toSongs(): List<Song> {
    return map {
        it.toSong()
    }
}

fun Song.toHistoryEntity(timePlayed: Long): HistoryEntity {
    return HistoryEntity(
        id = id,
        data = data,
        title = title,
        trackNumber = trackNumber,
        year = year,
        size = size,
        duration = duration,
        dateAdded = dateAdded,
        dateModified = dateModified,
        albumId = albumId,
        albumName = albumName,
        artistId = artistId,
        artistName = artistName,
        albumArtistName = albumArtistName,
        genreName = genreName,
        timePlayed = timePlayed
    )
}

fun Song.toSongEntity(playListId: Long): SongEntity {
    return SongEntity(
        playlistCreatorId = playListId,
        id = id,
        data = data,
        title = title,
        trackNumber = trackNumber,
        year = year,
        size = size,
        duration = duration,
        dateAdded = dateAdded,
        dateModified = dateModified,
        albumId = albumId,
        albumName = albumName,
        artistId = artistId,
        artistName = artistName,
        albumArtist = albumArtistName,
        genreName = genreName,
    )
}

fun SongEntity.toSong(): Song {
    return Song(
        id = id,
        data = data,
        title = title,
        trackNumber = trackNumber,
        year = year,
        size = size,
        duration = duration,
        dateAdded = dateAdded,
        dateModified = dateModified,
        albumId = albumId,
        albumName = albumName,
        artistId = artistId,
        artistName = artistName,
        albumArtistName = albumArtist,
        genreName = genreName
    )
}

fun PlayCountEntity.toSong(): Song {
    return Song(
        id = id,
        data = data,
        title = title,
        trackNumber = trackNumber,
        year = year,
        size = size,
        duration = duration,
        dateAdded = dateAdded,
        dateModified = dateModified,
        albumId = albumId,
        albumName = albumName,
        artistId = artistId,
        artistName = artistName,
        albumArtistName = albumArtistName,
        genreName = genreName
    )
}

fun HistoryEntity.toSong(): Song {
    return Song(
        id = id,
        data = data,
        title = title,
        trackNumber = trackNumber,
        year = year,
        size = size,
        duration = duration,
        dateAdded = dateAdded,
        dateModified = dateModified,
        albumId = albumId,
        albumName = albumName,
        artistId = artistId,
        artistName = artistName,
        albumArtistName = albumArtistName,
        genreName = genreName
    )
}

fun Song.toPlayCount(timePlayed: Long = -1, playCount: Int = 0, skipCount: Int = 0): PlayCountEntity {
    return PlayCountEntity(
        id = id,
        data = data,
        title = title,
        trackNumber = trackNumber,
        year = year,
        size = size,
        duration = duration,
        dateAdded = dateAdded,
        dateModified = dateModified,
        albumId = albumId,
        albumName = albumName,
        artistId = artistId,
        artistName = artistName,
        albumArtistName = albumArtistName,
        genreName = genreName,
        timePlayed = timePlayed,
        playCount = playCount,
        skipCount = skipCount
    )
}

fun List<Song>.toSongsEntity(playlistEntity: PlaylistEntity): List<SongEntity> {
    return map {
        it.toSongEntity(playlistEntity.playListId)
    }
}

fun List<Song>.toSongsEntity(playlistId: Long): List<SongEntity> {
    return map {
        it.toSongEntity(playlistId)
    }
}