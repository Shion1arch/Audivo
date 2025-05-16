
package com.mardous.booming.views;

import android.content.Context;
import android.util.AttributeSet;

import com.google.android.material.card.MaterialCardView;

public class WidthFitSquareCardView extends MaterialCardView {

    public WidthFitSquareCardView(Context context) {
        super(context);
    }

    public WidthFitSquareCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WidthFitSquareCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
