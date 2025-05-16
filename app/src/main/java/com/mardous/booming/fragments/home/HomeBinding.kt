
package com.mardous.booming.fragments.home

import com.mardous.booming.databinding.FragmentHomeBinding

class HomeBinding(homeBinding: FragmentHomeBinding) {
    val root = homeBinding.root
    val container = homeBinding.container
    val appBarLayout = homeBinding.appBarLayout
    val toolbar = homeBinding.appBarLayout.toolbar
    val lastAdded = homeBinding.homeContent.absPlaylists.lastAdded
    val myTopTracks = homeBinding.homeContent.absPlaylists.myTopTracks
    val shuffleButton = homeBinding.homeContent.absPlaylists.actionShuffle
    val history = homeBinding.homeContent.absPlaylists.history
    val recyclerView = homeBinding.homeContent.recyclerView
    val progressIndicator = homeBinding.homeContent.progressIndicator
    val empty = homeBinding.homeContent.empty
}