package ir.thebigbang.myslideshowlibrary;

import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.ViewGroup;

public class CustomSlideAdapter extends FragmentStatePagerAdapter {

    private int[] mResources;
    private int count;
    private FragmentManager fm;
    private Bitmap[] bitmapImages;
    private int type;

    public CustomSlideAdapter(FragmentManager fm, int[] mResources, int count, int type) {
        super(fm);
        this.mResources = mResources;
        this.count = count;
        this.fm = fm;
        this.type = type;
    }

    public CustomSlideAdapter(FragmentManager fm, Bitmap[] bitmapImages, int count, int type) {
        super(fm);
        this.count = count;
        this.bitmapImages = bitmapImages;
        this.type = type;
    }

    @Override
    public Fragment getItem(int position) {
        int realPosition = ((position) % count);
        SlideItem f = SlideItem.newInstance(type);

        switch (type) {
            case 0:
                f.setImageList(mResources[realPosition]);
                break;
            case 1:
                f.setImageBitmapList(bitmapImages[realPosition]);
                break;
        }
        return f;
    }

    @Override
    public int getCount() {
        return 200;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

}
