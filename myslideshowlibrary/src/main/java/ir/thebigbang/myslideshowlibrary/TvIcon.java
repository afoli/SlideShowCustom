package ir.thebigbang.myslideshowlibrary;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class TvIcon extends TextView {

    public TvIcon(Context context) {
        super(context);
    }

    public TvIcon(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TvIcon(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setTypeface(Typeface tf) {
        tf = Typeface.createFromAsset(getContext().getAssets(), "MaterialIcons-Regular.ttf");
        super.setTypeface(tf);
    }

    @Override
    public boolean isInEditMode() {
        return super.isInEditMode();
    }
}
