
package com.mardous.booming.views

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import com.mardous.booming.R

class PermissionView(context: Context, attrs: AttributeSet? = null) : FrameLayout(context, attrs) {

    private var mNumberView: TextView? = null
    private var mTitleView: TextView? = null
    private var mDescView: TextView? = null
    private var mButton: MaterialButton? = null

    private var mTitle: String? = null
    private var mDescription: String? = null
    private var mButtonText: String? = null
    private var mButtonIcon: Drawable? = null
    private var mNumber: Int = 0

    private var mGranted: Boolean = false

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.PermissionView)
        mTitle = a.getString(R.styleable.PermissionView_permissionTitle)
        mDescription = a.getString(R.styleable.PermissionView_permissionDescription)
        mButtonIcon = a.getDrawable(R.styleable.PermissionView_buttonIcon)
        mButtonText = a.getString(R.styleable.PermissionView_buttonText)
        mNumber = a.getInteger(R.styleable.PermissionView_number, mNumber)
        mGranted = a.getBoolean(R.styleable.PermissionView_granted, mGranted)
        a.recycle()

        val contentView = View.inflate(getContext(), R.layout.permission_view, null)
        mNumberView = contentView.findViewById(R.id.number)
        mTitleView = contentView.findViewById(R.id.title)
        mDescView = contentView.findViewById(R.id.text)
        mButton = contentView.findViewById(R.id.button)
        addView(contentView)

        setTitle(mTitle)
        setDescription(mDescription)
        setButtonIcon(mButtonIcon)
        setButtonText(mButtonText)
        setGranted(mGranted)
    }

    fun isGranted(): Boolean = mGranted

    fun setGranted(isGranted: Boolean) {
        this.mGranted = isGranted
        updateButton()
    }

    fun setNumber(number: Int) {
        mNumber = number
        mNumberView?.text = number.toString()
    }

    fun setTitle(title: String?) {
        mTitle = title
        mTitleView?.text = title
    }

    fun setDescription(description: String?) {
        mDescription = description
        mDescView?.text = description
    }

    fun setButtonIcon(icon: Drawable?) {
        mButtonIcon = icon
        updateButton()
    }

    fun setButtonText(text: String?) {
        this.mButtonText = text
        updateButton()
    }

    fun setButtonOnClickListener(onClickListener: OnClickListener) {
        mButton?.setOnClickListener(onClickListener)
    }

    private fun updateButton() {
        if (mGranted) {
            mButton?.setIconResource(R.drawable.ic_check_24dp)
            mButton?.setText(R.string.granted)
        } else {
            mButton?.setIcon(mButtonIcon)
            mButton?.text = mButtonText
        }
    }
}
