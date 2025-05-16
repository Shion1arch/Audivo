
package com.mardous.booming.interfaces;

public interface IMusicServiceEventListener {
    void onServiceConnected();

    void onServiceDisconnected();

    void onPlayingMetaChanged();

    void onPlayStateChanged();

    void onRepeatModeChanged();

    void onShuffleModeChanged();

    void onQueueChanged();

    void onMediaStoreChanged();

    void onFavoritesStoreChanged();
}
