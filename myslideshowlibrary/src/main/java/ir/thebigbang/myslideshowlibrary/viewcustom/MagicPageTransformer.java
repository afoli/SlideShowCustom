package ir.thebigbang.myslideshowlibrary.viewcustom;

import android.support.v4.view.ViewPager;
import android.view.View;

public class MagicPageTransformer implements ViewPager.PageTransformer {

    private static final float MIN_SCALE = 0.2f;
    private static final float MIN_ALPHA = 0.3f;

    @Override
    public void transformPage(View page, float position) {
        final float normalizedposition = Math.abs(Math.abs(position) - 1);
        page.setScaleX(normalizedposition / 2 + 0.5f);
        page.setScaleY(normalizedposition / 2 + 0.5f);
        //
//        page.setRotationY(position * -30);


        int pageWidth = page.getWidth();
        int pageHeight = page.getHeight();

        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            page.setAlpha(0);

        } else if (position <= 1) { // [-1,1]
            // Modify the default slide transition to shrink the page as well
            float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
            float vertMargin = pageHeight * (1 - scaleFactor) / 2;
            float horzMargin = pageWidth * (1 - scaleFactor) / 2;
            if (position < 0) {
                page.setTranslationX(horzMargin - vertMargin / 2);
            } else {
                page.setTranslationX(-horzMargin + vertMargin / 2);
            }

            // Scale the page down (between MIN_SCALE and 1)
            page.setScaleX(scaleFactor);
            page.setScaleY(scaleFactor);

            // Fade the page relative to its size.
            page.setAlpha(MIN_ALPHA +
                    (scaleFactor - MIN_SCALE) /
                            (1 - MIN_SCALE) * (1 - MIN_ALPHA));

        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            page.setAlpha(0);
        }

//        page.setRotationY(position * -30);
    }
}
