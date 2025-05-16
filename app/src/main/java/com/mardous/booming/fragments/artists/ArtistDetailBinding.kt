
package com.mardous.booming.fragments.artists

import com.mardous.booming.databinding.FragmentArtistDetailBinding

class ArtistDetailBinding(binding: FragmentArtistDetailBinding) {
    val appBarLayout = binding.appBarLayout
    val toolbar = binding.toolbar
    val image = binding.image
    val artistTitle = binding.artistTitle
    val artistText = binding.artistText
    val playAction = binding.playAction
    val shuffleAction = binding.shuffleAction
    val searchAction = binding.searchAction
    val songTitle = binding.fragmentArtistContent.songTitle
    val songSortOrder = binding.fragmentArtistContent.songSortOrder
    val songRecyclerView = binding.fragmentArtistContent.recyclerView
    val albumTitle = binding.fragmentArtistContent.albumTitle
    val albumSortOrder = binding.fragmentArtistContent.albumSortOrder
    val albumRecyclerView = binding.fragmentArtistContent.albumRecyclerView
    val similarArtistTitle = binding.fragmentArtistContent.similarArtistTitle
    val similarArtistRecyclerView = binding.fragmentArtistContent.similarRecyclerView
    val biographyTitle = binding.fragmentArtistContent.biographyTitle
    val biography = binding.fragmentArtistContent.biography
    val container = binding.container
}