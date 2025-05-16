
package com.mardous.booming.views

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.TextView
import com.mardous.booming.R

class InfoView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    FrameLayout(context, attrs, defStyleAttr) {

    private var titleView: TextView? = null
    private var textView: TextView? = null

    init {
        val ta = getContext().obtainStyledAttributes(attrs, R.styleable.InfoView)
        val style = ta.getInt(R.styleable.InfoView_infoStyle, 0)
        val view = if (style == STYLE_HOR) {
            inflate(context, R.layout.info_item_horizontal, null)
        } else {
            inflate(context, R.layout.info_item_vertical, null)
        }
        titleView = view.findViewById(R.id.title)
        titleView?.text = ta.getString(R.styleable.InfoView_infoTitle)
        if (ta.hasValue(R.styleable.InfoView_infoTitleColor)) {
            titleView?.setTextColor(ta.getColorStateList(R.styleable.InfoView_infoTitleColor))
        }
        textView = view.findViewById(R.id.text)
        textView?.text = ta.getString(R.styleable.InfoView_infoText)
        if (ta.hasValue(R.styleable.InfoView_infoTextColor)) {
            textView?.setTextColor(ta.getColorStateList(R.styleable.InfoView_infoTextColor))
        }
        ta.recycle()
        addView(view)
    }

    fun setTitle(title: CharSequence) {
        titleView?.text = title
    }

    fun setText(textRes: Int) {
        textView?.setText(textRes)
    }

    fun setText(text: CharSequence) {
        textView?.text = text
    }

    companion object {
        private const val STYLE_HOR = 0
        private const val STYLE_VER = 1
    }
}