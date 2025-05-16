
package com.mardous.booming.interfaces

import android.view.MenuItem
import android.view.View
import java.io.File

interface IFileCallbacks {
    fun fileSelected(file: File)
    fun fileMenuClick(file: File, view: View)
    fun filesMenuClick(item: MenuItem, files: List<File>)
}