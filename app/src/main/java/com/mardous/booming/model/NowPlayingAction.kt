
package com.mardous.booming.model

import androidx.annotation.StringRes
import com.mardous.booming.R

enum class NowPlayingAction(@StringRes val titleRes: Int) {
    Lyrics(R.string.action_show_lyrics),
    AddToPlaylist(R.string.action_add_to_playlist),
    TogglePlayState(R.string.action_play_pause),
    OpenAlbum(R.string.action_go_to_album),
    OpenArtist(R.string.action_go_to_artist),
    OpenPlayQueue(R.string.playing_queue_label),
    ToggleFavoriteState(R.string.toggle_favorite),
    ShufflePlayQueue(R.string.shuffle_queue),
    TagEditor(R.string.action_tag_editor),
    SleepTimer(R.string.action_sleep_timer),
    SoundSettings(R.string.sound_settings),
    WebSearch(R.string.web_search),
    SaveAlbumCover(R.string.save_cover),
    Nothing(R.string.label_nothing);
}