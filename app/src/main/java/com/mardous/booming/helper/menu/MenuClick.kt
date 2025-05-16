
package com.mardous.booming.helper.menu

import android.view.Menu
import android.view.View
import androidx.appcompat.widget.PopupMenu

typealias MenuConsumer = (Menu) -> Unit

fun newPopupMenu(anchor: View, menuRes: Int, menuConsumer: MenuConsumer? = null): PopupMenu {
    return PopupMenu(anchor.context, anchor).apply {
        inflate(menuRes)
        if (menuConsumer != null) {
            menuConsumer(menu)
        }
    }
}

abstract class OnClickMenu : View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    override fun onClick(v: View) {
        newPopupMenu(v, popupMenuRes) { menu ->
            onPreparePopup(menu)
        }.also { popup ->
            popup.setOnMenuItemClickListener(this)
            popup.show()
        }
    }

    protected abstract val popupMenuRes: Int

    protected open fun onPreparePopup(menu: Menu) {}
}