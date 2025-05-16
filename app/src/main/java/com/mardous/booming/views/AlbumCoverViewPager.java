
package com.mardous.booming.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import com.mardous.booming.util.Preferences;

public class AlbumCoverViewPager extends ViewPager {

	private boolean allowSwiping;

	public AlbumCoverViewPager(@NonNull Context context) {
		this(context, null);
	}

	public AlbumCoverViewPager(@NonNull Context context, AttributeSet attrs) {
		super(context, attrs);
		setAllowSwiping(Preferences.INSTANCE.getAllowCoverSwiping());
	}

	public void setAllowSwiping(boolean allowSwiping) {
		this.allowSwiping = allowSwiping;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (allowSwiping) {
			return super.onTouchEvent(ev);
		}
		return true;
	}
}
