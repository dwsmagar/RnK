package com.susankya.rnk;


import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class GalleryFragment extends Fragment implements FragmentCodes {


    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String SN;
    private static final int PICKIMAGE = 12;
    @BindView(R.id.gallerygrid)RecyclerView galleryGridView;
    @BindView(R.id.floating_add_button)FloatingActionButton fab;
    @BindView(R.id.emptyView)TextViewPlus emptyView;

    public ArrayList<GalleryItem> galleryItems;
    ImageLoader imageLoader;
    private String filePath;
    private GVAdapter gvAdapter;
    public int size=0;
    GridLayoutManager gridLayoutManager;
    String link=FragmentCodes.MAIN_DATABASE+"viewGallery.php";

    public GalleryFragment() {
        // Required empty public constructor
    }

    public static GalleryFragment newInstance(String param1, String param2) {
        GalleryFragment fragment = new GalleryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
       // galleryItems=new ArrayList<>();
       // galleryItems=((ImmortalApplication)getActivity().getApplication()).galleryItemArrayList;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            SN=getArguments().getString(ARG_PARAM1);
        }
        imageLoader=ImageLoader.getInstance();
        DisplayImageOptions options = new DisplayImageOptions.Builder()
        .cacheInMemory(true).cacheOnDisk(true).displayer(new FadeInBitmapDisplayer(1000,true,true,true))
        .build();
        ImageLoaderConfiguration imageLoaderConfiguration=new ImageLoaderConfiguration.Builder(getActivity().getApplicationContext())
                .defaultDisplayImageOptions(options)
                .build();
        imageLoader.init(imageLoaderConfiguration);

        galleryItems=new ArrayList<>();

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(ActivityCompat.checkSelfPermission(getActivity(), permissions[0]) == PackageManager.PERMISSION_GRANTED){
            switch (requestCode) {
                case REQUEST_CODE_ASK_PERMISSIONS:
                    showFileChooser();
                    break;
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case PICKIMAGE:
                if(resultCode !=getActivity().RESULT_CANCELED){
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    // Toast.makeText(getActivity(),selectedImage.toString(),Toast.LENGTH_SHORT).show();
                    //Log.d("TAG", "onActivityResult:original "+selectedImage.toString());
                        CompressImage compressImage=new CompressImage(getActivity(),selectedImage);
                        filePath=compressImage.getFinalFilename();
                        //Log.d("TAG1", "onActivityResult: "+compressImage.getFinalFilename());
                       //Log.d("TAG", "onActivityResult:edited"+filePath);
                    String url=FragmentCodes.MAIN_DATABASE+"insertImages.php";
new NewFileUploader(new String[]{filePath},"images",url,new String[]{"cmdxxx","album_caption","dbName"},new String[]{FragmentCodes.CMDXXX,"none",Utilities.getDatabaseName(getActivity())},getActivity()).setFileUploadedListener(new NewFileUploader.FileUploadedListener() {
    @Override
    public void OnFileUploaded(String result) {
        //Log.d("Gallery", "OnFileUploaded: "+result);
        if(result.equals("insert successful")){
            Toast.makeText(getActivity(),"Image Uploaded",Toast.LENGTH_SHORT).show();
            fetchURLS();
        }
    }
});
                }
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_gallery, container, false);
        ButterKnife.bind(this,v);
fab.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
    /*    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICKIMAGE);*/
        checkPermission();
    }
});
        if(!UtilitiesAdi.isProfileOfAdmin(getActivity(),SN)){
            fab.setVisibility(View.GONE);
        }else
        fab.setVisibility(View.VISIBLE);
        gridLayoutManager=new GridLayoutManager(getActivity(),2);
        galleryGridView.setHasFixedSize(true);
        galleryGridView.setLayoutManager(gridLayoutManager);
        fetchURLS();

        return v;

    }

    public void fetchURLS()
    {

            new PhpConnect(link,"",getActivity(),0,false,
                    new String[]{CMDXXX,SchoolProfileActivity.dbName},
                    new String[]{"cmdxxx","dbName"}).setListener(
                    new PhpConnect.ConnectOnClickListener() {
                        @Override
                        public void onConnectListener(String res) {

                            try {
                                JSONArray jsonArray=(JSONArray) new JSONTokener(res).nextValue();
                                galleryItems=new ArrayList<GalleryItem>(jsonArray.length());
                                size=jsonArray.length();
                                if(size==0){
                                    emptyView.setVisibility(View.VISIBLE);
                                    if(Utilities.isAdmin(getActivity()))
                                    {
                                        emptyView.setText("You have not added any image yet. Scroll up and click on the add button to upload.");
                                    }else{
                                        emptyView.setText("Your institute has not uploaded any image yet.");

                                    }
                                }else
                                emptyView.setVisibility(View.GONE);
                                for (int i=0;i<jsonArray.length();i++)
                                {
                                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                                    GalleryItem g=new GalleryItem();
                                    int collegesn=jsonObject.getInt("college_sn");
                                    int albumno=jsonObject.getInt("album_no");
                                    int imgno=jsonObject.getInt("img_no");
                                    int imgSN=jsonObject.getInt("sn");
                                    String url=jsonObject.getString("url");
                                    String caption=jsonObject.getString("caption");
                                    g.setCollegeSN(collegesn);
                                    g.setAlbum_no(albumno);
                                    g.setImg_no(imgno);
                                    g.setImgSN(imgSN);
                                    g.setURL(url);
                                    g.setCaption(caption);
                                    galleryItems.add(g);
                                }
                                gvAdapter=new GVAdapter();
                                galleryGridView.setAdapter(gvAdapter);
                                ((ImmortalApplication)getActivity().getApplication()).galleryItemArrayList=galleryItems;

                            }
                            catch (Exception e)
                            {

                            }
                        }
                    }
            );


    }

    public class GVAdapter extends RecyclerView.Adapter<ViewHolder>
    {

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return galleryItems.size();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_grid_item, null);
            ViewHolder rcv = new ViewHolder(layoutView);
            return rcv;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            final GalleryItem gI=galleryItems.get(holder.getAdapterPosition());
            holder.view.setTag(position);
            if(UtilitiesAdi.isProfileOfAdmin(getContext(),SN))
                holder.rl.setVisibility(View.VISIBLE);
            else
            holder.rl.setVisibility(View.GONE);

            holder.view.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((ImmortalApplication)getActivity().getApplication()).galleryItemArrayList=galleryItems;

                            Intent i=new Intent(getActivity(),GalleryViewActivity.class);
                            i.putExtra("pos",holder.getAdapterPosition() );
                            //Log.d("sizeis",size+"");
                            i.putExtra("size",galleryItems.size());
                            getActivity().startActivity(i);
                        }
                    }
            );

            //holder.rl.setVisibility(View.GONE);
            holder.menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu=new PopupMenu(getActivity(),holder.menu);
                    popupMenu.getMenuInflater().inflate(R.menu.menu_delete_image,popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()){
                                case R.id.menu_delete_image:
                                {
String link=FragmentCodes.MAIN_DATABASE+"viewGallery.php";
                                    new PhpConnect(link,"Deleting image...",getActivity(),1,new String[]{FragmentCodes.CMDXXX,
                                    "delete",UtilitiesAdi.giveMeSN(getActivity(),Utilities.getDatabaseName(getActivity())),gI.getImgSN()+""},
                                            new String[]{"cmdxxx","action","college_sn","image_sn"}).setListener(new PhpConnect.ConnectOnClickListener() {
                                        @Override
                                        public void onConnectListener(String res) {
                                            if(res.trim().equals("1")){
                                                getActivity().runOnUiThread(
                                                        new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                galleryItems.remove(holder.getAdapterPosition());
                                                               notifyItemRemoved(holder.getAdapterPosition());
                                                                notifyItemRangeChanged(holder.getAdapterPosition(),galleryItems.size());
                                                                notifyDataSetChanged();
                                                                size=galleryItems.size();
                                                                Toast.makeText(getActivity(),"Image deleted",Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                );

                                            }
                                            //Log.d("TAG", "onConnectListener: "+res);
                                        }
                                    });
                                }
                            }
                            return false;
                        }
                    });
                    popupMenu.show();
                }
            });
            holder.img.setImageBitmap(null);
            if(!gI.bitmapFetched)
                imageLoader.displayImage(gI.getURL(), holder.img,
                        new ImageLoadingListener() {
                            @Override
                            public void onLoadingStarted(String imageUri, View view) {

                            }

                            @Override
                            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                            }

                            @Override
                            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                                gI.setLoadedBitmap(loadedImage);
                                  gI.setBitmapFetched(true);
                                  notifyDataSetChanged();

                            }

                            @Override
                            public void onLoadingCancelled(String imageUri, View view) {

                            }
                        });
            else
                holder.img.setImageBitmap(gI.getLoadedBitmap());


        }



    }
    class ViewHolder extends RecyclerView.ViewHolder implements RecyclerView.OnClickListener
    {
        ImageView img;
        ImageView menu;
        RelativeLayout rl;
        View view;
        public ViewHolder(View v)
        {
            super(v);
            view=v;
            //v.setOnClickListener(this);
            img=(ImageView)v.findViewById(R.id.gallerypic);
            menu=(ImageView)v.findViewById(R.id.dot_menu);
rl=(RelativeLayout)v.findViewById(R.id.headerOfPhoto);
        }

        @Override
        public void onClick(View v) {


          /*  ((ImmortalApplication)getActivity().getApplication()).galleryItemArrayList=galleryItems;
            Intent i=new Intent(getActivity(),GalleryViewActivity.class);
            i.putExtra("pos", getAdapterPosition());
            i.putExtra("size",size);
            getActivity().startActivity(i);*/
        }
    }
    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermission() {
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= 23){
            int hasReadPermission =getActivity().checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE);
            if (hasReadPermission!= PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[] {android.Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_ASK_PERMISSIONS);
                return;
            }
            showFileChooser();
        } else{
            showFileChooser();
        }
    }


    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    PICKIMAGE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(getActivity(), "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
