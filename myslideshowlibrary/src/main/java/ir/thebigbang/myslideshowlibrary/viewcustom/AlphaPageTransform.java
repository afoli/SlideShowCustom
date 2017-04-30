package ir.thebigbang.myslideshowlibrary.viewcustom;

import android.support.v4.view.ViewPager;
import android.view.View;

public class AlphaPageTransform implements ViewPager.PageTransformer {
    @Override
    public void transformPage(View page, float position) {
        final float normalizedposition = Math.abs(Math.abs(position) - 1);
        page.setAlpha(normalizedposition);
    }
}
