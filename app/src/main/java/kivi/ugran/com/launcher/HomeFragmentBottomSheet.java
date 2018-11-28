package kivi.ugran.com.launcher;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.core.widget.NestedScrollView;
import android.util.AttributeSet;

import kivi.ugran.com.launcher.R;

public class HomeFragmentBottomSheet extends NestedScrollView {

    public void setTopMarginOffset(int topMarginOffset) {
        this.topMarginOffset = topMarginOffset;
    }

    private int topMarginOffset = 0;

    public HomeFragmentBottomSheet(Context context, AttributeSet attrs) {
        super(context, attrs);

        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.HomeFragmentBottomSheet);
        topMarginOffset = typedArray.getDimensionPixelSize(R.styleable.HomeFragmentBottomSheet_bottomSheetTopMargin, 0);
        typedArray.recycle();
    }

    public HomeFragmentBottomSheet(Context context) {
        this(context, null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec - topMarginOffset);
    }
}
