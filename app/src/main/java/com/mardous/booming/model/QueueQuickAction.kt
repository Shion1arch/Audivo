
package com.mardous.booming.model

import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import com.mardous.booming.R

enum class QueueQuickAction(
    @IdRes val menuItemId: Int,
    @DrawableRes val iconRes: Int,
    @StringRes val titleRes: Int
) {
    Save(R.id.action_save_playing_queue, R.drawable.ic_save_24dp, R.string.save_queue),
    Clear(R.id.action_clear_playing_queue, R.drawable.ic_clear_all_24dp, R.string.clear_queue),
    Shuffle(R.id.action_shuffle_queue, R.drawable.ic_shuffle_24dp, R.string.shuffle_queue),
    ShowCurrentTrack(R.id.action_move_to_current_track, R.drawable.ic_queue_next_24dp, R.string.action_move_to_current_track);
}