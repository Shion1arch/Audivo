
package com.mardous.booming.interfaces

import android.view.MenuItem
import com.mardous.booming.model.ReleaseYear

interface IYearCallback {
    fun yearClick(year: ReleaseYear)
    fun yearMenuItemClick(year: ReleaseYear, menuItem: MenuItem): Boolean
    fun yearsMenuItemClick(selection: List<ReleaseYear>, menuItem: MenuItem): Boolean
}