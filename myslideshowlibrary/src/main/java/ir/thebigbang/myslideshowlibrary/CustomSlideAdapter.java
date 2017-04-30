package ir.thebigbang.myslideshowlibrary;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.ViewGroup;

/**
 * Created by DITOP on 4/18/2017.
 */

public class CustomSlideAdapter extends FragmentStatePagerAdapter {

    private int[] mResources;
    private int count;
    private FragmentManager fm;

    public CustomSlideAdapter(FragmentManager fm, int[] mResources, int count) {
        super(fm);
        this.mResources = mResources;
        this.count = count;
        this.fm = fm;
    }

    @Override
    public Fragment getItem(int position) {
        int realPosition = ((position) % count);

        SlideItem f = SlideItem.newInstance();
        f.setImageList(mResources[realPosition]);
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
