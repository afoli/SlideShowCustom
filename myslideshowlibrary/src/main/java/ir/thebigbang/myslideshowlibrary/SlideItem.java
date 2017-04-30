package ir.thebigbang.myslideshowlibrary;


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SlideItem extends Fragment {

    private int itemData;
    private Bitmap myBitmap;
    public ProgressDialog pd;
    private ImageView ivImage;

    private OnFragmentInteractionListener mListener;

    public SlideItem() {
        // Required empty public constructor
    }

    public static SlideItem newInstance() {
        SlideItem f = new SlideItem();
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_slide_item, container, false);
        ivImage = (ImageView) root.findViewById(R.id.ivImageView);
        setImageInViewPager();

//            runSimpleAnimation(getContext(), ivImage,);

        return root;
    }

    public void setImageList(int integer) {
        this.itemData = integer;
    }

    public void setImageInViewPager() {
        try {

            //if image size is too large. Need to scale as below code.
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            myBitmap = BitmapFactory.decodeResource(getResources(), itemData, options);
            if (options.outWidth > 3000 || options.outHeight > 2000) {
                options.inSampleSize = 4;
            } else if (options.outWidth > 2000 || options.outHeight > 1500) {
                options.inSampleSize = 3;
            } else if (options.outWidth > 1000 || options.outHeight > 1000) {
                options.inSampleSize = 2;
            }
            options.inJustDecodeBounds = false;
            myBitmap = BitmapFactory.decodeResource(getResources(), itemData, options);
            if (myBitmap != null) {
                try {
                    if (ivImage != null) {
                        ivImage.setImageBitmap(myBitmap);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            System.gc();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (myBitmap != null) {
            myBitmap.recycle();
            myBitmap = null;
        }
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private static void runSimpleAnimation(Context context, View view, int animationId) {
        view.startAnimation(AnimationUtils.loadAnimation(
                context, animationId
        ));
    }
}
