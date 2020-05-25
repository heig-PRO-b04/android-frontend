package ch.heigvd.pro.b04.android.utils;

import android.content.Context;
import android.util.AttributeSet;

import com.google.android.material.card.MaterialCardView;

/**
 * A specialization of a {@link MaterialCardView} that measures the height with the same spec as
 * the width, resulting in a (horizontally-calibrated) square card.
 */
public class SquareCardView extends MaterialCardView {

    public SquareCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
