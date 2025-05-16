
package com.mardous.booming.fragments.albums

import androidx.lifecycle.*
import com.mardous.booming.http.Result
import com.mardous.booming.http.lastfm.LastFmAlbum
import com.mardous.booming.model.Album
import com.mardous.booming.repository.Repository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class AlbumDetailViewModel(private val repository: Repository, private val albumId: Long) : ViewModel() {

    private val _albumDetail = MutableLiveData<Album>()

    fun getAlbumDetail(): LiveData<Album> = _albumDetail

    fun getAlbum() = getAlbumDetail().value ?: Album.empty

    fun loadAlbumDetail() = viewModelScope.launch(IO) {
        _albumDetail.postValue(repository.albumById(albumId))
    }

    fun getSimilarAlbums(album: Album): LiveData<List<Album>> = liveData(IO) {
        repository.similarAlbums(album).let {
            if (it.isNotEmpty()) emit(it)
        }
    }

    fun getAlbumWiki(album: Album, lang: String?): LiveData<Result<LastFmAlbum>> = liveData(IO) {
        emit(Result.Loading)
        emit(repository.albumInfo(album.albumArtistName ?: album.artistName, album.name, lang))
    }
}