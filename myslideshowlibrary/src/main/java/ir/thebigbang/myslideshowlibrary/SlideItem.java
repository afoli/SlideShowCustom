package ir.thebigbang.myslideshowlibrary;


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SlideItem extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private int mParam1;

    private int itemData;
    private Bitmap itemDataBitmap;
    private Bitmap myBitmap;
    public ProgressDialog pd;
    private ImageView ivImage;

    private OnFragmentInteractionListener mListener;

    public SlideItem() {
        // Required empty public constructor
    }

    public static SlideItem newInstance(int param1) {
        SlideItem fragment = new SlideItem();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_slide_item, container, false);
        ivImage = (ImageView) root.findViewById(R.id.ivImageView);
        setImageInViewPager();
        return root;
    }

    public void setImageList(int integer) {
        this.itemData = integer;
    }

    public void setImageBitmapList(Bitmap bitmap) {
        this.itemDataBitmap = bitmap;
    }

    public void setImageInViewPager() {
        switch (mParam1) {
            case 0:
                ivImage.setImageResource(itemData);
                break;
            case 1:
                ivImage.setImageBitmap(itemDataBitmap);
                break;
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
