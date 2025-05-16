
package com.mardous.booming.fragments.albums

import com.mardous.booming.databinding.FragmentAlbumDetailBinding

class AlbumDetailBinding(binding: FragmentAlbumDetailBinding) {
    val appBarLayout = binding.appBarLayout
    val toolbar = binding.toolbar
    val image = binding.image
    val albumTitle = binding.albumTitle
    val albumText = binding.albumText
    val playAction = binding.playAction
    val shuffleAction = binding.shuffleAction
    val searchAction = binding.searchAction
    val songTitle = binding.fragmentAlbumContent.songTitle
    val songSortOrder = binding.fragmentAlbumContent.songSortOrder
    val songRecyclerView = binding.fragmentAlbumContent.recyclerView
    val similarAlbumTitle = binding.fragmentAlbumContent.moreTitle
    val similarAlbumSortOrder = binding.fragmentAlbumContent.similarAlbumSortOrder
    val similarAlbumRecyclerView = binding.fragmentAlbumContent.moreRecyclerView
    val wikiTitle = binding.fragmentAlbumContent.wikiTitle
    val wiki = binding.fragmentAlbumContent.wiki
    val container = binding.container
}