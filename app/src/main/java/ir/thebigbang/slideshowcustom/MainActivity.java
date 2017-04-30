package ir.thebigbang.slideshowcustom;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import ir.thebigbang.myslideshowlibrary.SlideView;
import ir.thebigbang.myslideshowlibrary.viewcustom.Consts;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int[] mResources = {
                R.drawable.f_one,
                R.drawable.f_two,
                R.drawable.f_three,
                R.drawable.f_four,
                R.drawable.f_five,
                R.drawable.f_six,
                R.drawable.f_seven
        };

        SlideView slide = (SlideView) findViewById(R.id.slide_view);
        slide.setWidthIndicator(LinearLayout.LayoutParams.WRAP_CONTENT);
        slide.setHeightIndicator(LinearLayout.LayoutParams.WRAP_CONTENT);
        slide.setPaddingIndicator(2);
        slide.setActivity(this);
        slide.setmResources(mResources);
        slide.setColorIndicator(Consts.Colors.Grey);
        slide.setColorIndicatorSelector(Consts.Colors.Blue);
        slide.setIndicatorNormalTextSize(8);
        slide.setIndicatorSelectedTextSize(11);
        slide.setPageSwitcherTime(4);
        slide.setPageTransformation(Consts.PageTransforms.RotationY);

    }
}
