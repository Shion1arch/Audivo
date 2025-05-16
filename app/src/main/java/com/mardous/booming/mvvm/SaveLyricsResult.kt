
package com.mardous.booming.mvvm

import android.net.Uri
import java.io.File

class SaveLyricsResult(
    val isPending: Boolean,
    val isSuccess: Boolean,
    val pendingWrite: List<Pair<File, Uri>>? = null
)