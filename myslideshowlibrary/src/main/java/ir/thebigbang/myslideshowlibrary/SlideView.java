package ir.thebigbang.myslideshowlibrary;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
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
    private int gravityIndicator = Gravity.CENTER;

    int pageTransformation = 0;

    private AttributeSet attrs;
    private boolean indicatorWithStrock = false;
    private int indicatorStrockColor = Color.WHITE;
    private int indicatorBorderWidth = 4;

    private int colorIndicatorImage;
    private int colorIndicatorSelectorImage;

    private android.support.v4.app.FragmentManager fragmentManager;

    int[] mResourcesDef = {
            R.drawable.f_five,
            R.drawable.f_three,
            R.drawable.f_four,
            R.drawable.f_one,
            R.drawable.f_seven,
            R.drawable.f_six,
            R.drawable.f_two
    };

    Bitmap[] bitmapImages;

    private Activity activity;

    private Timer timer;
    private int page = -1;

    private int dotsCount;

    public SlideView(Context context) {
        super(context);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.gravity_indi);
        gravityIndicator = a.getInteger(R.styleable.gravity_indi_gravity_indicator, 1);
        indicatorWithStrock = a.getBoolean(R.styleable.gravity_indi_indicator_strok, false);
        indicatorStrockColor = a.getColor(R.styleable.gravity_indi_indicator_border_color, 0);
        a.recycle();

        initialize(context);
    }

    public SlideView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.gravity_indi);
        gravityIndicator = a.getInteger(R.styleable.gravity_indi_gravity_indicator, 1);
        indicatorWithStrock = a.getBoolean(R.styleable.gravity_indi_indicator_strok, false);
        indicatorStrockColor = a.getColor(R.styleable.gravity_indi_indicator_border_color, 0);
        a.recycle();

        initialize(context);
    }

    public SlideView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.gravity_indi);
        gravityIndicator = a.getInteger(R.styleable.gravity_indi_gravity_indicator, 1);
        indicatorWithStrock = a.getBoolean(R.styleable.gravity_indi_indicator_strok, false);
        indicatorStrockColor = a.getColor(R.styleable.gravity_indi_indicator_border_color, 0);
        a.recycle();

        initialize(context);
    }

    public SlideView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.gravity_indi);
        gravityIndicator = a.getInteger(R.styleable.gravity_indi_gravity_indicator, 1);
        indicatorWithStrock = a.getBoolean(R.styleable.gravity_indi_indicator_strok, false);
        indicatorStrockColor = a.getColor(R.styleable.gravity_indi_indicator_border_color, 0);
        a.recycle();

        initialize(context);
    }

    private void initialize(Context context) {
        inflate(context, R.layout.view_slider, this);

        bitmapImages = new Bitmap[2];
        bitmapImages[0] = BitmapFactory.decodeResource(getResources(), R.drawable.f_five);
        bitmapImages[1] = BitmapFactory.decodeResource(getResources(), R.drawable.f_four);

        viewPage = (ViewPager) findViewById(R.id.slide_show);
        fragmentManager = ((AppCompatActivity) getContext()).getSupportFragmentManager();

        if (mResources == null) {
            if (bitmapImages.length != 0) {
                adapter = new CustomSlideAdapter(fragmentManager, bitmapImages, bitmapImages.length, 1);
                dotsCount = bitmapImages.length;
            } else {
                bitmapImages = new Bitmap[2];
                bitmapImages[0] = BitmapFactory.decodeResource(getResources(), R.drawable.default_imges);
                bitmapImages[1] = BitmapFactory.decodeResource(getResources(), R.drawable.default_imges);
                adapter = new CustomSlideAdapter(fragmentManager, bitmapImages, bitmapImages.length, 1);
                dotsCount = bitmapImages.length;
            }

            return;
        } else {
            if (mResources.length != 0) {
                adapter = new CustomSlideAdapter(fragmentManager, mResources, mResources.length, 0);
                dotsCount = mResources.length;
            } else {
                adapter = new CustomSlideAdapter(fragmentManager, mResourcesDef, mResources.length, 0);
                dotsCount = mResources.length;
            }

        }

        viewPage.setAdapter(adapter);

        if (!indicatorWithStrock) {
            setIndicatorItemsText();
        } else {
            setIndicatorItemsImage();
        }

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

                switch (event.getAction()) {

                    case MotionEvent.EDGE_RIGHT:
                        viewPage.setCurrentItem(page++);
                        break;
                    case MotionEvent.EDGE_LEFT:
                        viewPage.setCurrentItem(page--);
                        break;
                }

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
                    int numPages;
                    if (mResources == null) {
                        numPages = bitmapImages.length;
                    } else {
                        numPages = mResources.length;
                    }

                    page = (page + 1) % 200;
                    viewPage.setCurrentItem(page);
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

    public boolean isIndicatorWithStrock() {
        return indicatorWithStrock;
    }

    public void setIndicatorWithStrock(boolean indicatorWithStrock) {
        this.indicatorWithStrock = indicatorWithStrock;
    }

    public int getColorIndicatorImage() {
        return colorIndicatorImage;
    }

    public void setColorIndicatorImage(int colorIndicatorImage) {
        this.colorIndicatorImage = colorIndicatorImage;
    }

    public int getColorIndicatorSelectorImage() {
        return colorIndicatorSelectorImage;
    }

    public void setColorIndicatorSelectorImage(int colorIndicatorSelectorImage) {
        this.colorIndicatorSelectorImage = colorIndicatorSelectorImage;
    }

    public int getIndicatorBorderWidth() {
        return indicatorBorderWidth;
    }

    public void setIndicatorBorderWidth(int indicatorBorderWidth) {
        this.indicatorBorderWidth = indicatorBorderWidth;
    }

    public Bitmap[] getBitmapImages() {
        return bitmapImages;
    }

    public void setBitmapImages(Bitmap[] bitmapImages) {
        this.bitmapImages = bitmapImages;
        setBitmapAdapter();
    }

    private void setPageTransformationCustom() {

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

    private void setIndicatorItemsText() {

        final TvIcon[] dots = new TvIcon[dotsCount];

        LinearLayout dotsLayout = (LinearLayout) findViewById(R.id.layout_indicator);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 50);
        params.weight = 1.0f;

        switch (gravityIndicator) {
            case 0:
                params.gravity = Gravity.RIGHT;
                break;
            case 1:
                params.gravity = Gravity.CENTER;
                break;
            case 2:
                params.gravity = Gravity.LEFT;
                break;
            case 3:
                params.gravity = Gravity.END;
                break;
            case 4:
                params.gravity = Gravity.START;
                break;
            default:
                break;
        }
        dotsLayout.setLayoutParams(params);

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
    }

    private void setIndicatorItemsImage() {

        final CircleImageView[] dots = new CircleImageView[dotsCount];

        LinearLayout dotsLayout = (LinearLayout) findViewById(R.id.layout_indicator);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 50);
        params.weight = 1.0f;

        switch (gravityIndicator) {
            case 0:
                params.gravity = Gravity.RIGHT;
                break;
            case 1:
                params.gravity = Gravity.CENTER;
                break;
            case 2:
                params.gravity = Gravity.LEFT;
                break;
            case 3:
                params.gravity = Gravity.END;
                break;
            case 4:
                params.gravity = Gravity.START;
                break;
            default:
                break;
        }
        dotsLayout.setLayoutParams(params);

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new CircleImageView(getContext());

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(getWidthIndicator(),
                    getHeightIndicator());

            lp.setMargins(getPaddingIndicator(), getPaddingIndicator(),
                    getPaddingIndicator(), getPaddingIndicator());
            dots[i].setLayoutParams(lp);
            dotsLayout.addView(dots[i]);
        }

        viewPage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if (flag) {
                    dots[0].setImageResource(getColorIndicatorSelectorImage());
                    dots[0].setBorderColor(indicatorStrockColor);
                    dots[0].setBorderWidth(getIndicatorBorderWidth());
                    for (int l = 1; l < dotsCount; l++) {
                        dots[l].setImageResource(getColorIndicatorImage());
                    }
                    flag = false;
                }
            }

            @Override
            public void onPageSelected(int position) {

                int realPosition = position % dotsCount;

                for (int l = 0; l < dotsCount; l++) {
                    dots[l].setImageResource(getColorIndicatorImage());
                }
                dots[realPosition].setImageResource(getColorIndicatorSelectorImage());
                dots[realPosition].setBorderColor(indicatorStrockColor);
                dots[realPosition].setBorderWidth(getIndicatorBorderWidth());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setBitmapAdapter() {

        adapter = new CustomSlideAdapter(fragmentManager, bitmapImages, bitmapImages.length, 1);
        dotsCount = getBitmapImages().length;
        viewPage.setAdapter(adapter);

        if (!indicatorWithStrock) {
            setIndicatorItemsText();
        } else {
            setIndicatorItemsImage();
        }
    }
}
