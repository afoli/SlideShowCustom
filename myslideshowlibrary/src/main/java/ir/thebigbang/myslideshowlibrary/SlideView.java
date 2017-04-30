package ir.thebigbang.myslideshowlibrary;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import java.lang.reflect.Field;
import java.util.Timer;
import java.util.TimerTask;

import ir.thebigbang.myslideshowlibrary.viewcustom.AlphaPageTransform;
import ir.thebigbang.myslideshowlibrary.viewcustom.Consts;
import ir.thebigbang.myslideshowlibrary.viewcustom.DepthPageTransformer;
import ir.thebigbang.myslideshowlibrary.viewcustom.MagicPageTransformer;
import ir.thebigbang.myslideshowlibrary.viewcustom.OnboardingPageTransformer;
import ir.thebigbang.myslideshowlibrary.viewcustom.RotationYPageTransform;
import ir.thebigbang.myslideshowlibrary.viewcustom.ScaleXYPageTransform;
import ir.thebigbang.myslideshowlibrary.viewcustom.ZoomOutPageTransformer;

public class SlideView extends LinearLayout {

    private static final String TAG = "Slide";

    private int position = 0, totalImage;
    private ViewPager viewPage;
    private CustomSlideAdapter adapter;

    private int[] mResources;
    private String colorIndicator;
    private String colorIndicatorSelector;
    private int indicatorNormalTextSize;
    private int indicatorSelectedTextSize;
    private int widthIndicator;
    private int heightIndicator;
    private int paddingIndicator;
    private boolean flag = true;
    private int pageSwitcherTime = 2;

    int pageTransformation = 0;

    int[] mResourcesDef = {
            R.drawable.f_five,
            R.drawable.f_three,
            R.drawable.f_four,
            R.drawable.f_one,
            R.drawable.f_seven,
            R.drawable.f_six,
            R.drawable.f_two
    };

    private Activity activity;

    private Timer timer;
    private int page = -1;


    public SlideView(Context context) {
        super(context);
        initialize(context);
    }

    public SlideView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public SlideView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context);
    }

    public SlideView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize(context);
    }

    private void initialize(Context context) {
        inflate(context, R.layout.view_slider, this);

        viewPage = (ViewPager) findViewById(R.id.slide_show);
        android.support.v4.app.FragmentManager fragmentManager
                = ((AppCompatActivity) getContext()).getSupportFragmentManager();

        final int dotsCount;

        if (mResources == null) {
            adapter = new CustomSlideAdapter(fragmentManager, mResourcesDef, mResourcesDef.length);
            dotsCount = mResourcesDef.length;
            return;
        } else {
            adapter = new CustomSlideAdapter(fragmentManager, mResources, mResources.length);
            dotsCount = mResources.length;
        }

        viewPage.setAdapter(adapter);
//        viewPage.setSwipeable(false);

        final TvIcon[] dots = new TvIcon[dotsCount];

        LinearLayout dotsLayout = (LinearLayout) findViewById(R.id.layout_indicator);

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new TvIcon(getActivity());
            dots[i].setTextSize(getIndicatorNormalTextSize());
            dots[i].setText(R.string.iconCircleFill);

            LayoutParams lp = new LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);

            lp.setMargins(getPaddingIndicator(), getPaddingIndicator(),
                    getPaddingIndicator(), getPaddingIndicator());

            lp.gravity = Gravity.CENTER;

            dots[i].setLayoutParams(lp);
            dotsLayout.addView(dots[i]);
        }

        viewPage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if (flag) {
                    dots[0].setText(R.string.iconCircleFill);
                    dots[0].setTextSize(getIndicatorSelectedTextSize());
                    dots[0].setTextColor(Color.parseColor("#" + getColorIndicatorSelector()));
                    for (int l = 1; l < dotsCount; l++) {
                        dots[l].setText(R.string.iconCircleFill);
                        dots[l].setTextSize(getIndicatorNormalTextSize());
                        dots[l].setTextColor(Color.parseColor("#" + getColorIndicator()));
                    }
                    flag = false;
                }
            }

            @Override
            public void onPageSelected(int pos) {

                int realPosition = pos % dotsCount;

                for (int l = 0; l < dotsCount; l++) {
                    dots[l].setText(R.string.iconCircleFill);
                    dots[l].setTextSize(getIndicatorNormalTextSize());
                    dots[l].setTextColor(Color.parseColor("#" + getColorIndicator()));
                }
                dots[realPosition].setText(R.string.iconCircleFill);
                dots[realPosition].setTextSize(getIndicatorSelectedTextSize());
                dots[realPosition].setTextColor(Color.parseColor("#" + getColorIndicatorSelector()));

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        try {
            Field mScroller;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            FixedSpeedScroller scroller =
                    new FixedSpeedScroller(viewPage.getContext(),
                            new DecelerateInterpolator(0.75f));
            // scroller.setFixedDuration(5000);
            mScroller.set(viewPage, scroller);

        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
        }

        viewPage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Log.d(TAG, "getCurrentItem:  " + viewPage.getCurrentItem());

//                switch (event.getAction()) {
//
//                    case MotionEvent.EDGE_RIGHT:
//                        viewPage.setCurrentItem(page++);
//                        Log.d(TAG, "onTouch: EDGE_RIGHT " + page);
//                        break;
//                    case MotionEvent.EDGE_LEFT:
//                        viewPage.setCurrentItem(page--);
//                        Log.d(TAG, "onTouch: EDGE_LEFT " + page);
//                        break;
//                }

                viewPage.setCurrentItem(page);

                return false;
            }
        });

    }

    public void pageSwitcher(int seconds) {
        timer = new Timer(); // At this line a new Thread will be created
        timer.scheduleAtFixedRate(new RemindTask(), 0, seconds * 500); // delay
    }

    class RemindTask extends TimerTask {

        @Override
        public void run() {
            ((Activity) getContext()).runOnUiThread(new Runnable() {
                public void run() {
//                    viewPage.setCurrentItem((page++) % mResources.length);
                    int numPages = viewPage.getAdapter().getCount();
                    page = (page + 1) % numPages;
                    viewPage.setCurrentItem(page);
                    Log.d(TAG, "run: page: " + page);
                }
            });
        }
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getTotalImage() {
        return totalImage;
    }

    public void setTotalImage(int totalImage) {
        this.totalImage = totalImage;
    }

    public int getIndicatorNormalTextSize() {
        return indicatorNormalTextSize;
    }

    public void setIndicatorNormalTextSize(int indicatorNormalTextSize) {
        this.indicatorNormalTextSize = indicatorNormalTextSize;
    }

    public int getIndicatorSelectedTextSize() {
        return indicatorSelectedTextSize;
    }

    public void setIndicatorSelectedTextSize(int indicatorSelectedTextSize) {
        this.indicatorSelectedTextSize = indicatorSelectedTextSize;
    }

    public String getColorIndicator() {
        return colorIndicator;
    }

    public void setColorIndicator(String colorIndicator) {
        this.colorIndicator = colorIndicator;
    }

    public String getColorIndicatorSelector() {
        return colorIndicatorSelector;
    }

    public void setColorIndicatorSelector(String colorIndicatorSelector) {
        this.colorIndicatorSelector = colorIndicatorSelector;
    }

    public int[] getmResources() {
        return mResources;
    }

    public void setmResources(int[] mResources) {
        this.mResources = mResources;
        initialize(getContext());
    }

    public int getWidthIndicator() {
        return widthIndicator;
    }

    public void setWidthIndicator(int widthIndicator) {
        this.widthIndicator = widthIndicator;
        initialize(getContext());
    }

    public int getHeightIndicator() {
        return heightIndicator;
    }

    public void setHeightIndicator(int heightIndicator) {
        this.heightIndicator = heightIndicator;
        initialize(getContext());
    }

    public int getPaddingIndicator() {
        return paddingIndicator;
    }

    public void setPaddingIndicator(int paddingIndicator) {
        this.paddingIndicator = paddingIndicator;
        initialize(getContext());
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public int getPageSwitcherTime() {
        return pageSwitcherTime;
    }

    public void setPageSwitcherTime(int pageSwitcherTime) {
        this.pageSwitcherTime = pageSwitcherTime;
        pageSwitcher(getPageSwitcherTime());
    }

    public int getPageTransformation() {
        return pageTransformation;
    }

    public void setPageTransformation(int pageTransformation) {
        this.pageTransformation = pageTransformation;
        setPageTransformationCustom();
    }

    public void setPageTransformationCustom() {

        switch (getPageTransformation()) {

            case Consts.PageTransforms.Depth:
                viewPage.setPageTransformer(true, new DepthPageTransformer());
                break;

            case Consts.PageTransforms.ZoomOut:
                viewPage.setPageTransformer(true, new ZoomOutPageTransformer());
                break;

            case Consts.PageTransforms.Alpha:
                viewPage.setPageTransformer(true, new AlphaPageTransform());
                break;

            case Consts.PageTransforms.ScaleXY:
                viewPage.setPageTransformer(true, new ScaleXYPageTransform());
                break;

            case Consts.PageTransforms.RotationY:
                viewPage.setPageTransformer(true, new RotationYPageTransform());
                break;

            case Consts.PageTransforms.Magic:
                viewPage.setPageTransformer(true, new MagicPageTransformer());
                break;

            case Consts.PageTransforms.Onboarding:
                viewPage.setPageTransformer(true, new OnboardingPageTransformer());
                break;
        }
    }

}
