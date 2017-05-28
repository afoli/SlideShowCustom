package ir.thebigbang.myslideshowlibrary.viewcustom;

import android.support.v4.view.ViewPager;
import android.view.View;

public class OnboardingPageTransformer implements ViewPager.PageTransformer {

    @Override
    public void transformPage(View page, float position) {

        float absPosition = Math.abs(position);

        if (position <= -1.0f || position >= 1.0f) {

            // Page is not visible -- stop any running animations

        } else if (position == 0.0f) {

            // Page is selected -- reset any views if necessary

        } else {

            // Page is currently being swiped -- perform animations here
            page.setAlpha(1.0f - absPosition * 2);
            page.setScaleX(1.0f - absPosition * 2);
            page.setScaleY(1.0f - absPosition * 2);
            page.setAlpha(1.0f - absPosition * 2);
        }
    }
}