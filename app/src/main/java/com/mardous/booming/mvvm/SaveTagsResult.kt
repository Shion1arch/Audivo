
package com.mardous.booming.mvvm

import java.io.File


class SaveTagsResult(
    val isLoading: Boolean,
    val isSuccess: Boolean,
    val cacheFiles: List<File>? = null
)