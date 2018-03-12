package com.susankya.schoolvalley;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GalleryPicFragment extends Fragment {

    @BindView(R.id.pic)ImageView pic;
    @BindView(R.id.picNum)TextViewPlus picNumTv;
    @BindView(R.id.captionLL)View captionLL;
    @BindView(R.id.picdate)TextViewPlus datetv;
    @BindView(R.id.captiontv)TextViewPlus captiontv;
   private int position;

    public GalleryPicFragment() {
        // Required empty public constructor
    }

    public static GalleryPicFragment newInstance(int pos) {
        GalleryPicFragment fragment = new GalleryPicFragment();
        Bundle args = new Bundle();
        args.putInt("position", pos);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
           position=getArguments().getInt("position");
            //Log.d("posPic",position+"");
        }
    }

    ArrayList<GalleryItem> galleryItems;
    GalleryItem g;
    ImageLoader imageLoader;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_gallery_pic, container, false);
        ButterKnife.bind(this,v);
        galleryItems=((ImmortalApplication)getActivity().getApplication()).galleryItemArrayList;
         g=galleryItems.get(position);
        picNumTv.setCustomFont(getActivity(),FragmentCodes.BOLD);
        picNumTv.setText((position+1)+" of "+GalleryViewActivity.PAGES_NUM);
        captiontv.setText(g.getCaption());
        imageLoader=ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(getContext()));
        if(g.isBitmapFetched())
        pic.setImageBitmap(g.getLoadedBitmap());
        else
        {
            imageLoader.displayImage(g.getURL(), pic,
                    new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {

                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                            g.setLoadedBitmap(loadedImage);
                            g.setBitmapFetched(true);
                         ///   ((ImmortalApplication)getActivity().getApplication()).galleryItemArrayList.set(position,g);

                        }

                        @Override
                        public void onLoadingCancelled(String imageUri, View view) {

                        }
                    });
        }
        return v;
    }


}
