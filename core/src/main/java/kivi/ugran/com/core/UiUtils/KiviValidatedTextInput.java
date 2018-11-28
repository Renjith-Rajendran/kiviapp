package kivi.ugran.com.core.UiUtils;

import android.content.Context;
import androidx.constraintlayout.widget.ConstraintLayout;
import kivi.ugran.com.core.R;

import android.util.AttributeSet;


@Deprecated
public class KiviValidatedTextInput extends ConstraintLayout {
    public KiviValidatedTextInput(Context context) {
        super(context);
    }

    public KiviValidatedTextInput(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public KiviValidatedTextInput(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }


    private void initView(Context context) {
        inflate(context, R.layout.kivi_inputtext_view, this);
    }
}
